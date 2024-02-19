import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class WaitForRequestActionBehaviour extends CyclicBehaviour {
    // Message template for request-action messages
    MessageTemplate requestTemplate = MessageTemplate.and(
        MessageTemplate.MatchConversationId("request-action"),
        MessageTemplate.MatchPerformative(ACLMessage.REQUEST)
    );

    // Message template for update-state messages
    MessageTemplate informTemplate = MessageTemplate.and(
        MessageTemplate.MatchConversationId("update-state"),
        MessageTemplate.MatchPerformative(ACLMessage.INFORM)
    );

    MessageTemplate combinedTemplate = MessageTemplate.or(requestTemplate, informTemplate);

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(combinedTemplate);

        try {

            if (msg != null) {
                switch (msg.getConversationId()) {
                    case "request-action":
                        handleRequestAction(msg);
                        // Code for handling request-action messages goes here
                        break;
                    case "update-state":
                        handleUpdateState(msg);
                        // Code for handling update-state messages goes here
                        break;
                    default:
                        // Code for handling unexpected messages goes here
                        break;
                }
            } else {
                block();
            }

            /*  Receive and process request-action messages
            if (requestMsg != null) {
                handleRequestAction(requestMsg);
                return; // Exit action() after processing request-action message
            }

            // Receive and process update-state messages
            ACLMessage informMsg = myAgent.receive(informTemplate);
            System.out.println(informMsg);
            if (informMsg != null) {
                handleUpdateState(informMsg);
                return; // Exit action() after processing update-state message
            }

            // If no messages matching templates, block and wait
            block();*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRequestAction(ACLMessage requestMsg) throws UnreadableException {
        AID senderAID = requestMsg.getSender();
        // Create PROPOSE/request-action message with the requested next Position
        ACLMessage proposeMsg = requestMsg.createReply();
        proposeMsg.setPerformative(ACLMessage.PROPOSE);
//       proposeMsg.addReceiver(senderAID); // Assuming you have the AID of the Simulator Agent
//       proposeMsg.setInReplyTo(requestMsg.getReplyWith());

        try{
            if (myAgent instanceof GreedyAgent) {
                        Position nextPosition = ((GreedyAgent) myAgent).computeNextPosition();
                        proposeMsg.setContentObject(nextPosition);
                    } else if (myAgent instanceof RandomAgent) {
                        Position nextPosition = ((RandomAgent) myAgent).computeNextPosition();
                        proposeMsg.setContentObject(nextPosition);
                    } else if (myAgent instanceof BackToCenterAgent) {
                        Position nextPosition = ((BackToCenterAgent) myAgent).computeNextPosition();
                        proposeMsg.setContentObject(nextPosition);
                    }
            // Send the proposeMsg
            myAgent.send(proposeMsg);
        }
        catch(Exception e){
            System.out.println("Excpetion:" + e.getMessage());
        }
        
    }

    private void handleUpdateState(ACLMessage informMsg) throws UnreadableException {
        SimulationState updatedState = (SimulationState) informMsg.getContentObject();
        ((AgentUtils) myAgent).updateSimulationState(updatedState);
        System.out.println("Position updated:");
    }
}
