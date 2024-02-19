import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;;

public class WaitForSimulationCompleteBehaviour extends CyclicBehaviour{
    @Override
    public void action() {

        MessageTemplate endTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("simulation-complete"),
                                                            MessageTemplate.MatchPerformative(ACLMessage.INFORM));

        ACLMessage msg = myAgent.receive(endTemplate);
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
