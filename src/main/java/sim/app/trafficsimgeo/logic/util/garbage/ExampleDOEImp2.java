package sim.app.trafficsimgeo.logic.util.garbage;

import sim.app.trafficsimgeo.model.entity.Node;

import java.util.LinkedList;
import java.util.List;

public class ExampleDOEImp2 {
    private List<Node> nodesCache;

    public List<Node> example() {
        List<Node> mList = new LinkedList<>();
        for (Node node : nodesCache) {
            boolean condition = true;
            if (condition)
                mList.add(node);
        }
        return mList;
    }
}
