import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main {
    public static void main(String[] args) {
        // Create the main container
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        runtime.setCloseVM(true);
        Profile profile = new ProfileImpl(null, 8000, null);
        //profile.setParameter(Profile.CONTAINER_NAME, "MainContainer");
        //profile.setParameter(Profile.GUI, "true");
        AgentContainer mainContainer = runtime.createMainContainer(profile);
        
        try {
            //gui to shut down the entire system !!!!!!!
            AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();
            //Start the SimulatorAgent
            AgentController simulatorAgentController = mainContainer.createNewAgent("simulatorAgent", "SimulatorAgent", null);
            simulatorAgentController.start();

            AgentController greedyAgentController = mainContainer.createNewAgent("greedyAgent", "GreedyAgent", new Object[]{1});
            greedyAgentController.start();

            // Start the first RandomAgent
            //AgentController randomAgent1Controller = mainContainer.createNewAgent("randomAgent1", "RandomAgent", new Object[]{5}); // Pass commitment parameter 5
            //randomAgent1Controller.start();

            // Start the backToCenter Agent
            //AgentController backToCenterAgentController = mainContainer.createNewAgent("backtocenterAgent", "BackToCenterAgent", new Object[]{1}); // Pass commitment parameter 10
            //backToCenterAgentController.start();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
