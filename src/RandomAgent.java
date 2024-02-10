import java.util.LinkedList;
import java.util.Random;
import jade.core.Agent;

// il simulatorAgent call the 

public class RandomAgent extends Agent {

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
                addBehaviour(new WaitForSimulationCompleteBehaviour());

            } else {
                System.err.println("ParticipantAgent: No commitment provided.");
                doDelete(); // Terminate the agent if commitment is not provided
            }
        }

        public Position calculateRandomPosition() {
            MapNavigator navigator = new MapNavigator();
            LinkedList<Position> possiblePos = new LinkedList<Position>();
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
    }

    // Define a method to access the simulation state
    public SimulationState getSimulationState() {
        return simulationState;
    }


}
