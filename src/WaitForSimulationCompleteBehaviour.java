import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;;

public class WaitForSimulationCompleteBehaviour extends CyclicBehaviour{
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.INFORM && msg.getConversationId().equals("simulation-complete")) {
                System.out.println("Received simulation-complete message. Stopping execution.");
                // Stop the agent's execution
                myAgent.doDelete();
            }
        } else {
            block(); // Wait for messages
        }
    }
}
