package graph;

import drawing.DrawingApi;
import model.Edge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdgeListGraph extends Graph implements CliGraphBuilder {
    private final Map<Integer, List<Integer>> edgeList = new HashMap<>();

    public EdgeListGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }

    @Override
    public List<Edge> edges() {
        return edgeList
                .entrySet()
                .stream()
                .flatMap(edges -> edges.getValue()
                        .stream()
                        .map(to -> new Edge(edges.getKey(), to))
                )
                .toList();
    }

    @Override
    public int nodeCount() {
        return edgeList.size();
    }

    @Override
    public void addNode(List<Integer> input) {
        edgeList.put(input.get(0), input.subList(1, input.size()));
    }
}
