package drawing;

import com.indvd00m.ascii.render.Point;
import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Circle;
import com.indvd00m.ascii.render.elements.Line;

public class AsciiDrawingApi implements DrawingApi {
    private final static int DEFAULT_AREA = 100;
    private final DrawingArea drawingArea;
    private final IRender render = new Render();
    private final IContextBuilder builder = render.newBuilder();

    public AsciiDrawingApi() {
        drawingArea = new DrawingArea();
        drawingArea.addOnChangeListener(this::updateBuilderArea);

        drawingArea.setWidth(DEFAULT_AREA);
        drawingArea.setHeight(DEFAULT_AREA);

        updateBuilderArea();
    }

    @Override
    public DrawingArea getDrawingArea() {
        return drawingArea;
    }

    @Override
    public void drawCircle(model.Point point, double radius) {
        builder.element(new Circle(
                unsafeDoubleToInt(point.getX()),
                unsafeDoubleToInt(point.getY()),
                unsafeDoubleToInt(radius)
        ));
    }

    @Override
    public void drawLine(model.Point start, model.Point end) {
        builder.element(new Line(
                toAsciiPoint(start),
                toAsciiPoint(end)
        ));
    }

    @Override
    public void draw() {
        ICanvas canvas = render.render(builder.build());

        System.out.println(canvas.getText());
    }

    private void updateBuilderArea() {
        builder.width(drawingArea.getWidth())
                .height(drawingArea.getHeight());
    }

    private static int unsafeDoubleToInt(double d) {
        return (int) Math.round(d);
    }

    private static Point toAsciiPoint(model.Point point) {
        return new Point(
                (int) Math.round(point.getX()),
                (int) Math.round(point.getY())
        );
    }
}
