import jade.core.behaviours.Behaviour;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class WaitForRequestActionBehaviour extends Behaviour {
    private AgentUtils myAgent;

    public WaitForRequestActionBehaviour(AgentUtils agent) {
        super(agent);
        myAgent = agent;
    }
    // Message template for request-action messages
    private MessageTemplate requestTemplate = MessageTemplate.and(
        MessageTemplate.MatchConversationId("request-action"),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );

    private boolean requestReceived = false;

    public void action() {
        ACLMessage msg = myAgent.receive(requestTemplate);
        if (msg != null) {
            handleRequestAction(msg);
            requestReceived = true;
        }
    }

    public boolean done() {
        return requestReceived;
    }

    private void handleRequestAction(ACLMessage requestMsg) {
        ACLMessage proposeMsg = requestMsg.createReply();
        proposeMsg.setPerformative(ACLMessage.PROPOSE);

        try {
            if (myAgent instanceof GreedyAgent) {
                Position nextPosition = ((GreedyAgent) myAgent).computeNextPosition();
                proposeMsg.setContentObject(nextPosition);
            } else if (myAgent instanceof RandomAgent) {
                Position nextPosition = ((RandomAgent) myAgent).doRandomAction();
                proposeMsg.setContentObject(nextPosition);
            }
            else if (myAgent instanceof RewardBasedAgent) {
                Position nextPosition = ((RewardBasedAgent) myAgent).computeNextPosition();
                proposeMsg.setContentObject(nextPosition);
            }
            // Send the proposeMsg
            myAgent.send(proposeMsg);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }
}
