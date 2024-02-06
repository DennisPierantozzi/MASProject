import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class App {
    public static void main(String[] args) {
        try {
            // Create the Jade platform
            jade.core.Runtime runtime = jade.core.Runtime.instance();
            Profile profile = new ProfileImpl();
            AgentContainer mainContainer = runtime.createMainContainer(profile);

            // Create and start the SimulatorAgent
            AgentController agentController = mainContainer.createNewAgent("SimulatorAgent", SimulatorAgent.class.getName(), null);
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
