package drawing;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class DrawingArea {
    public static final int DEFAULT_DRAWING_AREA_WIDTH = 600;
    public static final int DEFAULT_DRAWING_AREA_HEIGHT = 600;

    private int drawingAreaWidth = DEFAULT_DRAWING_AREA_WIDTH;
    private int drawingAreaHeight = DEFAULT_DRAWING_AREA_HEIGHT;

    private final List<Runnable> listeners = new ArrayList<>();

    public int getWidth() {
        return drawingAreaWidth;
    }

    public void setWidth(int width) {
        drawingAreaWidth = width;
        notifyListeners();
    }

    public int getHeight() {
        return drawingAreaHeight;
    }

    public void setHeight(int height) {
        drawingAreaHeight = height;
        notifyListeners();
    }

    public void addOnChangeListener(Runnable action) {
        listeners.add(action);
    }

    private void notifyListeners() {
        listeners.forEach(Runnable::run);
    }
}
