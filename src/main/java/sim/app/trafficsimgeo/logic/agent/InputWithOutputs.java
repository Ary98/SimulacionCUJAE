package sim.app.trafficsimgeo.logic.agent;

import sim.app.trafficsimgeo.model.entity.Edge;

import java.util.List;

/**
 * this class represents each entry with all possible outputs (from that entry)
 */
class InputWithOutputs {
    private Edge inputEdge;
    private List<Edge> outputEdges;

    InputWithOutputs(Edge inputEdge, List<Edge> outputEdges) {
        this.inputEdge = inputEdge;
        this.outputEdges = outputEdges;
    }

    Edge getInputEdge() {
        return inputEdge;
    }

    public void setInputEdge(Edge inputEdge) {
        this.inputEdge = inputEdge;
    }

    List<Edge> getOutputEdges() {
        return outputEdges;
    }

    public void setOutputEdges(List<Edge> outputEdges) {
        this.outputEdges = outputEdges;
    }
}
