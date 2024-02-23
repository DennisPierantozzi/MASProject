import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

public class GreedyAgent extends AgentUtils{

    @Override
    protected void setup() {
        // Get commitment from command line arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            int commitment = (Integer) args[0];

            // Add behavior to periodically check for Simulator Agent availability every 5 seconds
            addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));
            addBehaviour(new TwoStepBehaviour(this));
            addBehaviour(new WaitForSimulationCompleteBehaviour());

        } else {
            System.err.println("ParticipantAgent: No commitment provided.");
            doDelete(); // Terminate the agent if commitment is not provided
        }
    }

    private LinkedList<Position> reconstructPath(Node goal) {
        LinkedList<Position> path = new LinkedList<>();
        Node current = goal;
        while (current != null) {
            path.addFirst(current.getPosition());
            current = current.getParent();
        }
        path.removeFirst();
        return path;
    }

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
                if(!simulationState.getMap().isTrapPosition(neighbor)){
                    int tentativeCost = current.getCost() + 1; // Cost to move to the neighbor
                    queue.add(new Node(neighbor, tentativeCost, tentativeCost + manhattanDistance(neighbor, goal), current));
                }
            }
        }
        
        // If no path is found, return an empty list
        return new LinkedList<>();
    }

    public int manhattanDistance(Position from, Position to) {
        return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
    }

    //
    @Override
    public Position computeNextPosition() {
        try {
            return AStarSearch(simulationState.getMap(), simulationState.getPosition(), findClosestPrize(simulationState.getPosition())).get(0);
        }
        catch(Exception e){
            // Just in case
            return doRandomAction();
        }
    }

   
}
