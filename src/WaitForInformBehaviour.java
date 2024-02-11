import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class WaitForInformBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        try {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                if (msg.getPerformative() == ACLMessage.INFORM && msg.getConversationId().equals("update-state")) {
                    try {
                        SimulationState updatedState = (SimulationState) msg.getContentObject();
                        ((RandomAgent) myAgent).updateSimulationState(updatedState);
                        SimulationState internal = ((RandomAgent) myAgent).getSimulationState();
                        System.out.println("UPDATE POSIZIONE FATTO:" + internal.getPosition().toString());
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                block(); // Wait for messages
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
