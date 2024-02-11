import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;

// il simulatorAgent call the 

public class RandomAgent extends Agent {
    private CountDownLatch stateUpdatedLatch = new CountDownLatch(1);

    private SimulationState simulationState;

        @Override
        protected void setup() {
            // Get commitment from command line arguments
            Object[] args = getArguments();
            if (args != null && args.length > 0) {
                int commitment = (Integer) args[0];

                // Add behavior to periodically check for Simulator Agent availability every 5 seconds
                addBehaviour(new CheckSimulatorAvailabilityBehaviour(this, 5000, commitment));
                addBehaviour(new WaitForRequestActionBehaviour());
                addBehaviour(new WaitForInformBehaviour());
                addBehaviour(new WaitForSimulationCompleteBehaviour());

            } else {
                System.err.println("ParticipantAgent: No commitment provided.");
                doDelete(); // Terminate the agent if commitment is not provided
            }
        }

        public Position calculateRandomPosition() {
            try {
                stateUpdatedLatch.await(); // Wait until the state has been updated
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MapNavigator navigator = new MapNavigator();
            LinkedList<Position> possiblePos = new LinkedList<Position>();
            System.out.println("calcolo le posizioni passando:" + simulationState.getPosition().toString());
            possiblePos = navigator.getNextPossiblePositions(simulationState.getMap(), simulationState.getPosition());
            Random rand = new Random();
            int randomIndex = rand.nextInt(possiblePos.size()); // Generate a random index
            for(Position p: possiblePos) {
                System.out.println(p.toString());
            }
            return possiblePos.get(randomIndex);
        }

    // Define a method to update the simulation state
    public void updateSimulationState(SimulationState newState) {
        this.simulationState = newState;
        stateUpdatedLatch.countDown();
    }

    // Define a method to access the simulation state
    public SimulationState getSimulationState() {
        return simulationState;
    }


}
