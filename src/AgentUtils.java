import java.util.LinkedList;
import java.util.Random;

import jade.core.Agent;

public class AgentUtils extends Agent {
    protected SimulationState simulationState;

    public void updateSimulationState(SimulationState state) {
        this.simulationState = state;
    }

    public SimulationState getSimulationState() {
        return this.simulationState;
    }

    public Position computeNextPosition() {
        // Default implementation
        System.out.println("Default next position");
        return new Position(0, 0);
    }

    public Position findClosestPrize(Position actualPosition) {
        Position closestPrize = null;
        double minDistance = Double.MAX_VALUE;
        for (Position prize : simulationState.getMap().getItemPositions()) {
            double distance = Math.sqrt(Math.pow(prize.x - actualPosition.x, 2) + Math.pow(prize.y - actualPosition.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestPrize = prize;
            }
        }
        return closestPrize;
    }

    public Position doRandomAction() {
        MapNavigator navigator = new MapNavigator();
        LinkedList<Position> possiblePos = new LinkedList<Position>();
        possiblePos = navigator.getNextPossiblePositions(simulationState.getMap(), simulationState.getPosition());
        Random rand = new Random();
        int randomIndex = rand.nextInt(possiblePos.size()); // Generate a random index
        return possiblePos.get(randomIndex);
    }

}