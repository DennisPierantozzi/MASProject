import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class CheckSimulatorAvailabilityBehaviour extends TickerBehaviour{

    private int commitment;

    public CheckSimulatorAvailabilityBehaviour(Agent agent, long period, int commitment) {
        super(agent, period);
        this.commitment = commitment;
    }

    @Override
    protected void onTick() {
        // Perform the periodic check for Simulator Agent availability
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("SimulatorService");
        dfd.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(myAgent, dfd);
            if (result != null && result.length > 0) {
                // Simulator Agent found, take appropriate action
                System.out.println("Simulator Agent found!");
                // Here I have to ask to play!!
                sendJoinSimulationRequest(result[0].getName());

                // Wait for reply
                ACLMessage reply = myAgent.blockingReceive(MessageTemplate.or(
                        MessageTemplate.MatchPerformative(ACLMessage.AGREE),
                        MessageTemplate.MatchPerformative(ACLMessage.REFUSE)),
                        5000); // Timeout after 5 seconds

                if (reply != null) {
                    // Process the reply
                    if (reply.getPerformative() == ACLMessage.AGREE) {
                        // Simulator Agent agreed to join simulation
                        System.out.println("Simulator Agent agreed to join simulation");
                        try {
                            SimulationState initialState = (SimulationState) reply.getContentObject();
                            ((AgentUtils) myAgent).updateSimulationState(initialState);
                            if (myAgent instanceof RewardBasedAgent) {
                                ((RewardBasedAgent) myAgent).updateAuxMap(initialState.getMap());
                                ((RewardBasedAgent) myAgent).updateLastPosition(initialState.getPosition());
                            }
                            stop();

                        } catch (Exception e) {
                            System.out.println("Simulation State not readable");
                        }
                    } else if (reply.getPerformative() == ACLMessage.REFUSE) {
                        // Simulator Agent refused to join simulation
                        System.out.println("Simulator Agent refused to join simulation");
                    }
                } else {
                    // Timeout occurred, no reply received
                    System.out.println("Timeout: No reply received from Simulator Agent");
                    // Handle further actions if needed
                }

            } else {
                System.out.println("Simulator Agent not found.");
                // Add your logic here if Simulator Agent is not found
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    private void sendJoinSimulationRequest(AID simulatorAgentAID) {
        ACLMessage joinRequest = new ACLMessage(ACLMessage.REQUEST);
        joinRequest.setSender(myAgent.getAID());
        joinRequest.addReceiver(simulatorAgentAID);
        joinRequest.setContent(Integer.toString(commitment)); // Send commitment as content
        joinRequest.setConversationId("join-simulation-request");
        myAgent.send(joinRequest);
    }
}

