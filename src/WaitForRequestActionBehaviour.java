import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class WaitForRequestActionBehaviour extends CyclicBehaviour {


    @Override
    public void action() {

        try {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                AID senderAID = msg.getSender();
                if (msg.getPerformative() == ACLMessage.REQUEST && msg.getConversationId().equals("request-action")) {

                    // Create PROPOSE/request-action message with the requested next Position
                    ACLMessage proposeMsg = msg.createReply();
                    proposeMsg.setPerformative(ACLMessage.PROPOSE);
                    proposeMsg.addReceiver(senderAID); // Assuming you have the AID of the Simulator Agent
                    proposeMsg.setInReplyTo(msg.getReplyWith());

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
            } else{
                block(); // Wait for messages
            }
            
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    

    //private SimulationState parseSimulationState(String content) {
        // Implement logic to parse SimulationState from the message content
        // This depends on how SimulationState is serialized and sent in the message
        // Return the parsed SimulationState object
    //}
}
