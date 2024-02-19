import java.util.LinkedList;
import java.util.Random;

// il simulatorAgent call the 

public class RandomAgent extends AgentUtils {
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
            MapNavigator navigator = new MapNavigator();
            LinkedList<Position> possiblePos = new LinkedList<Position>();
            possiblePos = navigator.getNextPossiblePositions(simulationState.getMap(), simulationState.getPosition());
            Random rand = new Random();
            int randomIndex = rand.nextInt(possiblePos.size()); // Generate a random index
            return possiblePos.get(randomIndex);
        }


}
