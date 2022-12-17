package graph;

import drawing.DrawingApi;
import model.Edge;
import model.Point;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public abstract class Graph implements CliGraphBuilder {
    private final static double GRAPH_CIRCLE_RADIUS_RATIO = 0.3;

    private final static double NODE_CIRCLE_RADIUS_TO_GRAPH_RADIUS_RATIO = 0.05;

    private final DrawingApi drawingApi;

    public Graph(DrawingApi drawingApi) {
        this.drawingApi = drawingApi;
    }

    public abstract List<Edge> edges();

    public abstract int nodeCount();

    public void drawGraph() {
        long maxWidth = drawingApi.getDrawingArea().getWidth();
        long maxHeight = drawingApi.getDrawingArea().getHeight();

        Point center = new Point(maxWidth / 2.0, maxHeight / 2.0);

        long minDimension = Math.min(maxWidth, maxHeight);
        double graphRadius = minDimension * GRAPH_CIRCLE_RADIUS_RATIO;
        double nodeRadius = minDimension * NODE_CIRCLE_RADIUS_TO_GRAPH_RADIUS_RATIO;


        ArrayList<Point> nodeCoordinates = new ArrayList<>();
        int nodeCount = nodeCount();

        for (int i = 0; i < nodeCount; i++) {
            double ro = i * 2 * Math.PI / nodeCount;
            Point nodeCenter = center.plus(
                    new Point(graphRadius * cos(ro), graphRadius * sin(ro))
            );

            drawingApi.drawCircle(nodeCenter, nodeRadius);
            nodeCoordinates.add(nodeCenter);
        }

        for (Edge edge : edges()) {
            drawingApi.drawLine(nodeCoordinates.get(edge.getFrom()), nodeCoordinates.get(edge.getTo()));
        }


        drawingApi.draw();
    }
}