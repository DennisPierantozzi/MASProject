import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class WaitForInformBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        try {

            MessageTemplate informTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("update-state"),
                                                            MessageTemplate.MatchPerformative(ACLMessage.INFORM));

            ACLMessage msg = myAgent.receive(informTemplate);
            if (msg != null) {
                    try {
                        SimulationState updatedState = (SimulationState) msg.getContentObject();
                        ((AgentUtils) myAgent).updateSimulationState(updatedState);
                        System.out.println("Position updated:");
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
            } else {
                block(); // Wait for messages
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
