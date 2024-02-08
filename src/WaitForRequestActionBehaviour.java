import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class WaitForRequestActionBehaviour extends CyclicBehaviour {

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        AID senderAID = msg.getSender();
        if (msg != null) {
            if (msg.getPerformative() == ACLMessage.REQUEST && msg.getConversationId().equals("request-action")) {
                // Calculate random position
                Position nextPosition = ((RandomAgent) myAgent).calculateRandomPosition();
                
                // Create PROPOSE/request-action message with the requested next Position
                ACLMessage proposeMsg = new ACLMessage(ACLMessage.PROPOSE);
                proposeMsg.setSender(myAgent.getAID());
                proposeMsg.addReceiver(senderAID); // Assuming you have the AID of the Simulator Agent
                proposeMsg.setConversationId("request-action");
                proposeMsg.setContent(nextPosition.toString());
                
                // Send the proposeMsg
                myAgent.send(proposeMsg);
            } else if (msg.getPerformative() == ACLMessage.INFORM && msg.getConversationId().equals("update-state")) {
                // Parse SimulationState from the message content and update internal perception
                // The simulatoragent inform about the new state! Take it for the other agents!                
                //SimulationState updatedState = parseSimulationState(msg.getContent());
                //((RandomAgent) myAgent).updatePerception(updatedState);
                
                // Process the updated state (if needed)
            }
        } else{
           System.out.println("fefe");; // Wait for messages
        }
    }

    //private SimulationState parseSimulationState(String content) {
        // Implement logic to parse SimulationState from the message content
        // This depends on how SimulationState is serialized and sent in the message
        // Return the parsed SimulationState object
    //}
}
