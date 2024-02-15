import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class FirstAgent extends Agent{
    private CountDownLatch stateUpdatedLatch = new CountDownLatch(1);
    private SimulationState simulationState;
    private LinkedList<Position> trapPositions;
    private LinkedList<Position> itemPosition;


    @Override

    protected void setup() {
        // Get commitment from command line arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            int commitment = (Integer) args[0];

            // Add behavior to periodically check for Simulator Agent availability every 5 seconds
            addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));
            addBehaviour(new WaitForRequestActionGoal_Trap());
            addBehaviour(new WaitForInformBehaviour());
            addBehaviour(new WaitForSimulationCompleteBehaviour());



        } else {
            System.err.println("ParticipantAgent: No commitment provided.");
            doDelete(); // Terminate the agent if commitment is not provided
        }
    }

    public Position NearestItem(){
        findTrapAndItem();
        Position p = simulationState.getPosition();
        System.out.println(trapPositions);
        System.out.println(itemPosition);
        return p;
    }


    private void findTrapAndItem() {
       System.out.println("orcidu");
       try {
        stateUpdatedLatch.await(); // Wait until the state has been updated
    }catch (InterruptedException e) {
        e.printStackTrace();

        Map map = simulationState.getMap();
        trapPositions = map.getTrapsPositions();
        itemPosition = map.getItemPositions();

        System.out.println(trapPositions);
        System.out.println(itemPosition);
        }
    }

    public void makeGreedyMove() {
        Position actualPosition = simulationState.getPosition();
        Position closestPrize = findClosestPrize(actualPosition);
        if (closestPrize != null) {
            // Move towards the closest prize (simplified logic)
            if (closestPrize.x > actualPosition.x) {
                actualPosition.x++;
            } else if (closestPrize.x < actualPosition.x) {
                actualPosition.x--;
            } else if (closestPrize.y > actualPosition.y) {
                actualPosition.y++;
            } else if (closestPrize.y < actualPosition.y) {
                actualPosition.y--;
            }
        }
    }

    private Position findClosestPrize(Position actualPosition) {
        Position closestPrize = null;
        double minDistance = Double.MAX_VALUE;
        for (Position prize : itemPosition) {
            double distance = Math.sqrt(Math.pow(prize.x - actualPosition.x, 2) + Math.pow(prize.y - actualPosition.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestPrize = prize;
            }
        }
        return closestPrize;
    }

    



    

   
}
