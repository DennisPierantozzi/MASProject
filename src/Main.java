import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class Main {
    public static void main(String[] args) {
        // Create the main container
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        runtime.setCloseVM(true);
        Profile profile = new ProfileImpl(null, 8888, null);
        //profile.setParameter(Profile.CONTAINER_NAME, "MainContainer");
        //profile.setParameter(Profile.GUI, "true");
        AgentContainer mainContainer = runtime.createMainContainer(profile);
        System.out.println("ere");

        try {
            System.out.println("eere");
            //gui to shut down the entire system !!!!!!!
            AgentController rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rma.start();
            //Start the SimulatorAgent
            AgentController simulatorAgentController = mainContainer.createNewAgent("simulatorAgent", "SimulatorAgent", null);
            simulatorAgentController.start();

            // Start the first RandomAgent
            AgentController randomAgent1Controller = mainContainer.createNewAgent("randomAgent1", "RandomAgent", new Object[]{5}); // Pass commitment parameter 5
            randomAgent1Controller.start();

            // Start the second RandomAgent
            AgentController randomAgent2Controller = mainContainer.createNewAgent("randomAgent2", "RandomAgent", new Object[]{10}); // Pass commitment parameter 10
            randomAgent2Controller.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
