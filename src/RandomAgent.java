
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.core.Agent;

// il simulatorAgent call the 

public class RandomAgent extends Agent {
    protected void setup() {
    System.out.println("Prova messaggio" +getLocalName());
    
    doDelete();
    }
}
