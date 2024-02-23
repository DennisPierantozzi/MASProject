import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;

/*
 * This agent reach the closest prize using A* search algorithm without caring of traps
 * When the commitment*backtocenterrate == stepsDone it will come back to the center using A* as well
 * 
 */

public class RewardBasedAgent extends AgentUtils {

    private double auxMap[][] = new double[10][10];
    private Position lastPosition;
    private LinkedList<Position> itemsPosition;
    private LinkedList<Position> originalItemsPosition = new LinkedList<>();

    private double alpha = 0.1;
    private double gamma= 1;
    private double trapReward = -200;
    private double itemReward = 20; //If I got an item, there won't be another item in the same cell

    @Override
    protected void setup() {
        // Get commitment from command line arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            int commitment = (Integer) args[0];
            
            // Add behavior to periodically check for Simulator Agent availability every 5 seconds
            addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));
            addBehaviour(new TwoStepBehaviour(this));
            //addBehaviour(new WaitForInformBehaviour());
            addBehaviour(new WaitForSimulationCompleteBehaviour());

        } else {
            System.err.println("ParticipantAgent: No commitment provided.");
            doDelete(); // Terminate the agent if commitment is not provided
        }
    }


    //initialize the auxmap
    public void updateAuxMap(Map map){
        LinkedList<Position> items = map.getItemPositions();
        LinkedList<Position> traps = map.getTrapsPositions();

        for (Position itemPosition : items) {
            auxMap[itemPosition.x][itemPosition.y] = 100;
        }
    
        for (Position trapPosition : traps) {
            auxMap[trapPosition.x][trapPosition.y] = -200;
        }

        itemsPosition = items;
    }


    public void updateLastPosition(Position pos) {
        lastPosition = pos;
    }
    public void updateItemPosition(LinkedList<Position> items) {
        itemsPosition = items;
        originalItemsPosition = items;
    }

    @Override
    public String toString()
    {
        String mapStr = "";
        for (int k = 0; k < auxMap.length; k++) {
            for (int k1 = 0; k1 < auxMap[k].length; k1++) {
                mapStr += auxMap[k][k1] + " ";
            }
            mapStr += "\n";
        }
        return mapStr;
    }


    public void updateRewards(Position pos){
        System.out.println("Sto updatando il valore della posizione:" + lastPosition.toString());
        System.out.println("La lista degli items è:" + itemsPosition.toString());
        //(1 - alpha) * currentQValue + alpha * (reward + gamma * maxNextQValue);
        Position bestPosition = getPosMaxQValue();
        double maxNextQValue = auxMap[bestPosition.x][bestPosition.y];
        System.out.println("bestposition is:" + bestPosition.toString() + "maxValue"  + String.valueOf(maxNextQValue));

        if(!(originalItemsPosition.equals(simulationState.getMap().getItemPositions()))){
            //!!World changed
            for(Position p : simulationState.getMap().getItemPositions()) {
                if(!itemsPosition.contains(p)) {
                    itemsPosition.add(p);
                }
            }
            originalItemsPosition = simulationState.getMap().getItemPositions();
        }

       if(pos == lastPosition) {
            // I tried to go in a trap! Im trapped!
            auxMap[lastPosition.x][lastPosition.y] = (1 - alpha) * auxMap[lastPosition.x][lastPosition.y] + alpha * (trapReward + gamma * maxNextQValue);
       }
       else if(itemsPosition.contains(pos)) {
            System.out.println("ti ho portato in un item!");
            // I got an item, but Im not sure I really got it. Maybe the world changed in the meantime
            auxMap[pos.x][pos.y] = 0;
            auxMap[lastPosition.x][lastPosition.y] = auxMap[lastPosition.x][lastPosition.y] = (1 - alpha) * auxMap[lastPosition.x][lastPosition.y] + alpha * (itemReward + gamma * maxNextQValue);
            itemsPosition.remove(pos);
       }
       else {
            double emptyCellReward = euclideanDistance(lastPosition, findClosestPrize(lastPosition))/10;
            System.out.println("emptycellreward:" + String.valueOf(emptyCellReward));
            auxMap[lastPosition.x][lastPosition.y] = auxMap[lastPosition.x][lastPosition.y] = (1 - alpha) * auxMap[lastPosition.x][lastPosition.y] + alpha * (emptyCellReward + gamma * maxNextQValue);
            // I got an empty cell. I think, maybe the world changed. Better than nothing.
        }
        lastPosition = pos;
        System.out.println("il valore della posizione è" + String.valueOf(auxMap[lastPosition.x][lastPosition.y]));
    }

    public int manhattanDistance(Position from, Position to) {
        System.out.println("distanza manhattan tra:" + from.toString() + to.toString());
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    public double euclideanDistance(Position pos1, Position pos2) {
        int deltaX = pos2.x - pos1.x;
        int deltaY = pos2.y - pos1.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    
    public Position getPosMaxQValue() {
        
        MapNavigator navigator = new MapNavigator();
        LinkedList<Position> possiblePos = new LinkedList<Position>();
        possiblePos = navigator.getNextPossiblePositions(simulationState.getMap(), simulationState.getPosition());
        double bestReward = Double.NEGATIVE_INFINITY;
        Position bestPos = null;

        for(Position pos : possiblePos) {
            //System.out.println("rewardpos:" + pos.toString() + " is " + String.valueOf(auxMap[pos.x][pos.y]));
            if(auxMap[pos.x][pos.y] > bestReward) {
                bestReward = auxMap[pos.x][pos.y];
                bestPos = pos;
            }
        }

        return bestPos;

    }

    @Override
    public Position computeNextPosition() {
        return getPosMaxQValue();
    }

}
