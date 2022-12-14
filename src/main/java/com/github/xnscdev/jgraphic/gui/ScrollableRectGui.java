package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ListIterator;

/**
 * A rectangular GUI component with contents that may be scrolled in the horizontal and/or vertical directions by
 * clicking and dragging the mouse.
 * @author XNSC
 */
public class ScrollableRectGui extends Gui {
    private final boolean scrollHorizontal;
    private final boolean scrollVertical;
    private final Vector2f scrollOffset = new Vector2f();
    private final Vector2f lastScrollPos = new Vector2f();
    private boolean scrolling;
    private float minScrollX = -Float.MAX_VALUE;
    private float maxScrollX = Float.MAX_VALUE;
    private float minScrollY = -Float.MAX_VALUE;
    private float maxScrollY = Float.MAX_VALUE;

    /**
     * Creates a new scrollable GUI with a solid-color background.
     * @param position position in device coordinates relative to the parent GUI
     * @param size size in device coordinates
     * @param backgroundColor the background color
     * @param scrollHorizontal whether horizontal scrolling is enabled
     * @param scrollVertical whether vertical scrolling is enabled
     */
    public ScrollableRectGui(Vector2f position, Vector2f size, Vector4f backgroundColor, boolean scrollHorizontal, boolean scrollVertical) {
        super(GuiManager.RECT_MODEL, position, size, backgroundColor);
        this.scrollHorizontal = scrollHorizontal;
        this.scrollVertical = scrollVertical;
    }

    /**
     * Creates a new scrollable GUI with a textured background.
     * @param position position in device coordinates relative to the parent GUI
     * @param size size in device coordinates
     * @param texture name of the background texture
     * @param scrollHorizontal whether horizontal scrolling is enabled
     * @param scrollVertical whether vertical scrolling is enabled
     */
    public ScrollableRectGui(Vector2f position, Vector2f size, String texture, boolean scrollHorizontal, boolean scrollVertical) {
        super(GuiManager.RECT_MODEL, position, size, texture);
        this.scrollHorizontal = scrollHorizontal;
        this.scrollVertical = scrollVertical;
    }

    public boolean canScrollHorizontal() {
        return scrollHorizontal;
    }

    public boolean canScrollVertical() {
        return scrollVertical;
    }

    public boolean isScrolling() {
        return scrolling;
    }

    /**
     * Gets the scrolled offset of the GUI. The scrolled offset is the distance scrolled from the original position,
     * with positive values meaning right or down.
     * @return the scroll offset vector
     */
    public Vector2f getScrollOffset() {
        return scrollOffset;
    }

    public float getMinScrollX() {
        return minScrollX;
    }

    /**
     * Sets the minimum scroll offset in the X direction. The GUI will not allow scrolling left once the scroll
     * offset for the left side of the visible area reaches this value.
     * @param minScrollX minimum scroll offset
     */
    public void setMinScrollX(float minScrollX) {
        this.minScrollX = minScrollX;
    }

    public float getMaxScrollX() {
        return maxScrollX;
    }

    /**
     * Sets the maximum scroll offset in the X direction. The GUI will not allow scrolling right once the scroll
     * offset for the right side of the visible area reaches this value.
     * @param maxScrollX maximum scroll offset
     */
    public void setMaxScrollX(float maxScrollX) {
        this.maxScrollX = maxScrollX;
    }

    public float getMinScrollY() {
        return minScrollY;
    }

    /**
     * Sets the minimum scroll offset in the Y direction. The GUI will not allow scrolling up once the scroll
     * offset for the top of the visible area reaches this value.
     * @param minScrollY minimum scroll offset
     */
    public void setMinScrollY(float minScrollY) {
        this.minScrollY = minScrollY;
    }

    public float getMaxScrollY() {
        return maxScrollY;
    }

    /**
     * Sets the maximum scroll offset in the Y direction. The GUI will not allow scrolling down once the scroll
     * offset for the bottom of the visible area reaches this value.
     * @param maxScrollY maximum scroll offset
     */
    public void setMaxScrollY(float maxScrollY) {
        this.maxScrollY = maxScrollY;
    }

    @Override
    public void render(GuiView view, Vector2f screenPos) {
        GuiView newView = background.render(this, view, screenPos);
        if (newView == null)
            return;
        Vector2f newScreenPos = new Vector2f(screenPos).add(position).sub(scrollOffset);
        for (GuiComponent gui : children) {
            gui.render(newView, newScreenPos);
        }
    }

    @Override
    public void tick(double delta) {
        if (scrolling) {
            Vector2f pos = DisplayManager.getMousePos();
            if (scrollHorizontal) {
                scrollOffset.x -= pos.x - lastScrollPos.x;
                if (scrollOffset.x < minScrollX)
                    scrollOffset.x = minScrollX;
                if (scrollOffset.x > maxScrollX - size.x)
                    scrollOffset.x = maxScrollX - size.x;
            }
            if (scrollVertical) {
                scrollOffset.y -= pos.y - lastScrollPos.y;
                if (scrollOffset.y < minScrollY)
                    scrollOffset.y = minScrollY;
                if (scrollOffset.y > maxScrollY - size.y)
                    scrollOffset.y = maxScrollY - size.y;
            }
            lastScrollPos.set(pos.x, pos.y);
        }
    }

    @Override
    public boolean mousePressed(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x + scrollOffset.x, y + scrollOffset.y) && gui.mousePressed(x + scrollOffset.x, y + scrollOffset.y))
                return true;
        }
        lastScrollPos.set(x, y);
        scrolling = true;
        takeFocus();
        return true;
    }

    @Override
    public boolean mouseReleased(float x, float y) {
        if (scrolling) {
            dropFocus();
            return true;
        }
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x + scrollOffset.x, y + scrollOffset.y) && gui.mouseReleased(x + scrollOffset.x, y + scrollOffset.y))
                return true;
        }
        GuiManager.resetFocusedComponent();
        return false;
    }

    @Override
    public void unfocused() {
        scrolling = false;
    }
}
