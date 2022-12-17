package model;

import lombok.Value;

@Value
public class Point {
    double x, y;

    public Point plus(Point another) {
        return new Point(x + another.x, y + another.y);
    }
}
