import java.util.Random;
import jade.core.Agent;

// il simulatorAgent call the 

public class RandomAgent extends Agent {
        @Override
        protected void setup() {
            // Get commitment from command line arguments
            Object[] args = getArguments();
            if (args != null && args.length > 0) {
                int commitment = Integer.parseInt((String) args[0]);

                // Add behavior to periodically check for Simulator Agent availability every 5 seconds
                addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));

                addBehaviour(new WaitForRequestActionBehaviour());

                addBehaviour(new WaitForSimulationCompleteBehaviour());

            } else {
                System.err.println("ParticipantAgent: No commitment provided.");
                doDelete(); // Terminate the agent if commitment is not provided
            }
        }

        public Position calculateRandomPosition() {
        Random random = new Random();
        int x = random.nextInt(10); // Assuming mapSize is accessible in this class
        int y = random.nextInt(10);
        return new Position(x, y);
    }
}
