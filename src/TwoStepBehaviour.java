import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

public class TwoStepBehaviour extends SequentialBehaviour {
    private AgentUtils myAgent;

    public TwoStepBehaviour(AgentUtils agent) {
        super(agent);
        myAgent = agent;
        addSubBehaviour(new WaitForRequestActionBehaviour(agent));
        addSubBehaviour(new HandleUpdateBehaviour(agent));
    }

    // Override this method to reset the behaviour when it is done
    @Override
    public int onEnd() {
        reset();
        myAgent.addBehaviour(this);
        return super.onEnd();
    }

    // Override this method to handle incoming messages
    @Override
    public void onStart() {
        // Start with the first step
        super.onStart();
    }
}
