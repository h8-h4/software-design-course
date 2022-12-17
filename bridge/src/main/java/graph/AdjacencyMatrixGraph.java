package graph;

import drawing.DrawingApi;
import model.Edge;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph extends Graph implements CliGraphBuilder {
    private final List<List<Integer>> matrix = new ArrayList<>();

    public AdjacencyMatrixGraph(DrawingApi drawingApi) {
        super(drawingApi);
    }

    @Override
    public List<Edge> edges() {
        ArrayList<Edge> result = new ArrayList<>();

        for (int i = 0; i < matrix.size(); i++) {
            List<Integer> row = matrix.get(i);

            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) == 0) {
                    continue;
                }

                result.add(new Edge(i, j));
            }
        }

        return result;
    }

    @Override
    public int nodeCount() {
        return matrix.size();
    }

    @Override
    public void addNode(List<Integer> input) {
        matrix.add(input);
    }
}
