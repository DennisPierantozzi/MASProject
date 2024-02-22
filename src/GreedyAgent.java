import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.tools.sniffer.Message;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class GreedyAgent extends AgentUtils{

    private double eps = 0.2;

    @Override
    protected void setup() {
        // Get commitment from command line arguments
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            int commitment = (Integer) args[0];

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

    
    @Override
    public Position computeNextPosition() {

        Position actualPosition = simulationState.getPosition();
        Position closestPrize = findClosestPrize(actualPosition);

        if (closestPrize != null) {

            if (Math.random() < eps) {
                // Exploration: choose a random action
                return doRandomAction();
            }
            else {
                // Exploitation: make the greedy move
                int deltaX = Integer.compare(closestPrize.x, actualPosition.x);
                int deltaY = Integer.compare(closestPrize.y, actualPosition.y);

                // Check if moving along the x-axis or y-axis is more beneficial
                if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                    // Move along the x-axis
                    if (isValidMove(actualPosition.x + deltaX, actualPosition.y)) {
                        actualPosition.x += deltaX;
                    } else if (isValidMove(actualPosition.x, actualPosition.y + deltaY)) {
                        // If moving along the x-axis is blocked, try moving along the y-axis
                        actualPosition.y += deltaY;
                    }
                } else {
                    // Move along the y-axis
                    if (isValidMove(actualPosition.x, actualPosition.y + deltaY)) {
                        actualPosition.y += deltaY;
                    } else if (isValidMove(actualPosition.x + deltaX, actualPosition.y)) {
                        // If moving along the y-axis is blocked, try moving along the x-axis
                        actualPosition.x += deltaX;
                    }
                }
            }
        }
        
        return actualPosition;
    }
    
    // Check if the move is valid (not blocked by traps)
    private boolean isValidMove(int x, int y) {
        Position newPos = new Position(x, y);
        return simulationState.getMap().withinMapLimits(newPos);
    }
   
}
