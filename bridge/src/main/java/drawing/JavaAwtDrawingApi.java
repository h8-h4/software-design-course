package drawing;

import model.Point;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class JavaAwtDrawingApi extends Frame implements DrawingApi {
    private final List<Ellipse2D.Double> circles = new ArrayList<>();
    private final List<Line2D.Double> lines = new ArrayList<>();
    private final DrawingArea drawingArea = new DrawingArea();

    @Override
    public DrawingArea getDrawingArea() {
        return drawingArea;
    }

    @Override
    public void drawCircle(Point point, double radius) {
        circles.add(new Ellipse2D.Double(
                point.getX() - radius,
                point.getY() - radius,
                radius * 2,
                radius * 2
        ));
    }

    @Override
    public void drawLine(Point start, Point end) {
        lines.add(new Line2D.Double(
                start.getX(),
                start.getY(),
                end.getX(),
                end.getY()
        ));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setPaint(Color.black);
        circles.forEach(graphics::fill);
        lines.forEach(graphics::draw);
    }

    @Override
    public void draw() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setSize(drawingArea.getHeight(), drawingArea.getHeight());
        setVisible(true);
    }
}
