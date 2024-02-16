import jade.core.Agent;

public class AgentUtils extends Agent {
    protected SimulationState simulationState;

    public void updateSimulationState(SimulationState state) {
        this.simulationState = state;
    }

    public SimulationState getSimulationState() {
        return this.simulationState;
    }

    public Position computeNextPosition() {
        // Default implementation
        System.out.println("Default next position");
        return new Position(0, 0);
    }

    public Position findClosestPrize(Position actualPosition) {
        Position closestPrize = null;
        double minDistance = Double.MAX_VALUE;
        for (Position prize : simulationState.getMap().getItemPositions()) {
            double distance = Math.sqrt(Math.pow(prize.x - actualPosition.x, 2) + Math.pow(prize.y - actualPosition.y, 2));
            if (distance < minDistance) {
                minDistance = distance;
                closestPrize = prize;
            }
        }
        return closestPrize;
    }

    // Other common functionalities related to simulation state management
}