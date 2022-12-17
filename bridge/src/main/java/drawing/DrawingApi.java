package drawing;

import model.Point;

public interface DrawingApi {
    DrawingArea getDrawingArea();

    void drawCircle(Point point, double radius);

    void drawLine(Point start, Point end);

    void draw();
}
