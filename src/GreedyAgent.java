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
        LinkedList<Position> trapPositions = simulationState.getMap().getTrapsPositions();
        Position actualPosition = simulationState.getPosition();
        Position closestPrize = findClosestPrize(actualPosition);
        if (closestPrize != null) {
            // Move towards the closest prize while avoiding traps (simplified logic)
            if (closestPrize.x > actualPosition.x && !trapPositions.contains(new Position(actualPosition.x + 1, actualPosition.y))) {
                actualPosition.x++;
            } else if (closestPrize.x < actualPosition.x && !trapPositions.contains(new Position(actualPosition.x - 1, actualPosition.y))) {
                actualPosition.x--;
            } else if (closestPrize.y > actualPosition.y && !trapPositions.contains(new Position(actualPosition.x, actualPosition.y + 1))) {
                actualPosition.y++;
            } else if (closestPrize.y < actualPosition.y && !trapPositions.contains(new Position(actualPosition.x, actualPosition.y - 1))) {
                actualPosition.y--;
            }
        }
        return actualPosition;
    }

    
   
}
