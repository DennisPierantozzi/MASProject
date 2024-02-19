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

public class BackToCenterAgent extends AgentUtils {
    private CountDownLatch stateUpdatedLatch = new CountDownLatch(1);

    private boolean policyToCenter = true;
    private boolean goingToCenter = false;

    private int commitment;
    private int stepsDone;
    private final int backToCenterRate = 2; //at the 20th step must be in the center


        @Override
        protected void setup() {
            // Get commitment from command line arguments
            Object[] args = getArguments();
            if (args != null && args.length > 0) {
                int commitment = (Integer) args[0];
                this.commitment = commitment;
                if(commitment<10) {
                    policyToCenter = false;
                }
                
                // Add behavior to periodically check for Simulator Agent availability every 5 seconds
                addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));
                addBehaviour(new WaitForRequestActionBehaviour());
                //addBehaviour(new WaitForInformBehaviour());
                addBehaviour(new WaitForSimulationCompleteBehaviour());

            } else {
                System.err.println("ParticipantAgent: No commitment provided.");
                doDelete(); // Terminate the agent if commitment is not provided
            }
        }

    // Method to create a path to come back to the center of the map
    public LinkedList<Position> findPathToCenter(Map map, Position currentPosition) {
        Position center = new Position(5, 5);
        return AStarSearch(map, currentPosition, center);
    }

    // Helper method to reconstruct the path from the goal node
    private LinkedList<Position> reconstructPath(Node goal) {
        LinkedList<Position> path = new LinkedList<>();
        Node current = goal;
        while (current != null) {
            path.addFirst(current.getPosition());
            current = current.getParent();
        }
        return path;
    }

    // A* search algorithm
    private LinkedList<Position> AStarSearch(Map map, Position start, Position goal) {
        Set<Position> visited = new HashSet<>(); // Set to keep track of visited positions
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost)); // Priority queue for open list
        
        // Add starting node to the queue
        queue.add(new Node(start, 0, manhattanDistance(start, goal)));

        while (!queue.isEmpty()) {
            Node current = queue.poll(); // Get the node with the lowest cost
            Position currentPosition = current.getPosition();
            
            // If the current position is the goal, return the path
            if (currentPosition.equals(goal)) {
                return reconstructPath(current);
            }
            
            // Mark the current position as visited
            visited.add(currentPosition);
            
            // Get neighbors of the current position
            MapNavigator navigator = new MapNavigator();
            LinkedList<Position> neighbors = navigator.getNextPossiblePositions(map, currentPosition);
            for (Position neighbor : neighbors) {
                // Skip if neighbor is already visited
                if (visited.contains(neighbor)) {
                    continue;
                }
                
                int tentativeCost = current.getCost() + 1; // Cost to move to the neighbor
                
                // Add neighbor to the queue with its total cost
                queue.add(new Node(neighbor, tentativeCost, tentativeCost + manhattanDistance(neighbor, goal), current));
            }
        }
        
        // If no path is found, return an empty list
        return new LinkedList<>();
    }

    public int manhattanDistance(Position from, Position to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }


    public Position computeNextPosition() {
        LinkedList<Position> pathToCenter = findPathToCenter(simulationState.getMap(), simulationState.getPosition());
        if(policyToCenter && pathToCenter.size() > 1 && goingToCenter) {
            System.out.println("tocenter:" + findPathToCenter(simulationState.getMap(), simulationState.getPosition()).toString());
            return pathToCenter.get(1);
        }
        else if (policyToCenter && pathToCenter.size() == 0 && goingToCenter){
            goingToCenter = false;
            stepsDone = 1;
            System.out.println(AStarSearch(simulationState.getMap(), simulationState.getPosition(), findClosestPrize(simulationState.getPosition())).toString());
            return AStarSearch(simulationState.getMap(), simulationState.getPosition(), findClosestPrize(simulationState.getPosition())).get(1);
        }
        else {
            if(stepsDone == commitment*backToCenterRate)
            {
                stepsDone = 0;
                goingToCenter = true;
            }
            stepsDone++;
            System.out.println(AStarSearch(simulationState.getMap(), simulationState.getPosition(), findClosestPrize(simulationState.getPosition())).toString());
            return AStarSearch(simulationState.getMap(), simulationState.getPosition(), findClosestPrize(simulationState.getPosition())).get(1);
        }

    }

    // Node class for A* search
    private static class Node {
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
