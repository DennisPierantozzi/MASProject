import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class HandleUpdateBehaviour extends Behaviour {
    private AgentUtils myAgent;


    public HandleUpdateBehaviour(AgentUtils agent) {
        super(agent);
        myAgent = agent;
    }

    // Message template for update-state messages
    private MessageTemplate informTemplate = MessageTemplate.and(
        MessageTemplate.MatchConversationId("update-state"),
        MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );

    private boolean updateReceived = false;

    public void action() {
        ACLMessage msg = myAgent.receive(informTemplate);
        if (msg != null) {
            handleUpdateState(msg);
            updateReceived = true;
        }
    }

    public boolean done() {
        return updateReceived;
    }

    private void handleUpdateState(ACLMessage informMsg) {
        try {
            SimulationState updatedState = (SimulationState) informMsg.getContentObject();
            ((AgentUtils) myAgent).updateSimulationState(updatedState);
            System.out.println("Position updated:");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}