import java.util.Comparator;
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
        LinkedList<Position> itemsPosition = simulationState.getMap().getItemPositions();
        itemsPosition.remove(actualPosition);

        for (Position prize : itemsPosition) {
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


    // Node class for A* search
    static class Node {
        private final Position position;
        private final int cost;
        private final int totalCost;
        private final Node parent;

        public Node(Position position, int cost, int totalCost, Node parent) {
            this.position = position;
            this.cost = cost;
            this.totalCost = totalCost;
            this.parent = parent;
        }

        public Node(Position position, int cost, int totalCost) {
            this(position, cost, totalCost, null);
        }

        public Position getPosition() {
            return position;
        }

        public int getCost() {
            return cost;
        }

        public int getTotalCost() {
            return totalCost;
        }

        public Node getParent() {
            return parent;
        }
    }

}