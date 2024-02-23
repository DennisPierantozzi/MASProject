import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class GreedyAgent extends AgentUtils{

    @Override
    protected void setup() {
        // Get commitment from command line arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            int commitment = (Integer) args[0];
            //int commitment = Integer.valueOf((String)(args[0]));

            // Add behavior to periodically check for Simulator Agent availability every 5 seconds
            addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));

            addBehaviour(new TwoStepBehaviour(this));
            
            addBehaviour(new WaitForSimulationCompleteBehaviour());

        } else {
            System.err.println("ParticipantAgent: No commitment provided.");
            doDelete(); // Terminate the agent if commitment is not provided
        }
    }

    
    @Override
    public Position computeNextPosition() {
        LinkedList<Position> trapPositions = simulationState.getMap().getTrapsPositions();
        Position actualPosition = simulationState.getPosition();
        Position closestPrize = findClosestPrize(actualPosition);

        if(closestPrize == actualPosition) {
            return doRandomAction();
        }

        if (closestPrize != null) {
            System.out.println("Il closest prize che voglio è in:" + closestPrize.toString());
            System.out.println("Penso di essere in:" + actualPosition.toString());
            int deltaX = Integer.compare(closestPrize.x, actualPosition.x);
            int deltaY = Integer.compare(closestPrize.y, actualPosition.y);

            // Check if moving along the x-axis or y-axis is more beneficial
            if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                // Move along the x-axis
                if (isValidMove(actualPosition.x + deltaX, actualPosition.y, trapPositions)) {
                    actualPosition.x += deltaX;
                } else if (isValidMove(actualPosition.x, actualPosition.y + deltaY, trapPositions)) {
                    // If moving along the x-axis is blocked, try moving along the y-axis
                    actualPosition.y += deltaY;
                }
            } else {
                // Move along the y-axis
                if (isValidMove(actualPosition.x, actualPosition.y + deltaY, trapPositions)) {
                    actualPosition.y += deltaY;
                } else if (isValidMove(actualPosition.x + deltaX, actualPosition.y, trapPositions)) {
                    // If moving along the y-axis is blocked, try moving along the x-axis
                    actualPosition.x += deltaX;
                }
            }
        }
        
        return actualPosition;
    }
    
    // Check if the move is valid (not blocked by traps)
    private boolean isValidMove(int x, int y, LinkedList<Position> trapPositions) {
        Position newPos = new Position(x, y);
        return !trapPositions.contains(newPos) && simulationState.getMap().withinMapLimits(newPos);
    }
   
}
