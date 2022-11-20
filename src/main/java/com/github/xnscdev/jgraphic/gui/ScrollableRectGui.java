package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ListIterator;

public class ScrollableRectGui extends Gui {
    private final boolean scrollHorizontal;
    private final boolean scrollVertical;
    private final Vector2f scrollOffset = new Vector2f();
    private final Vector2f lastScrollPos = new Vector2f();
    private boolean scrolling;

    public ScrollableRectGui(Vector2f position, Vector2f size, Vector4f backgroundColor, boolean scrollHorizontal, boolean scrollVertical) {
        super(GuiManager.RECT_MODEL, position, size, backgroundColor);
        this.scrollHorizontal = scrollHorizontal;
        this.scrollVertical = scrollVertical;
    }

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

    public Vector2f getScrollOffset() {
        return scrollOffset;
    }

    @Override
    public void render(GuiView view, Vector2f offset) {
        GuiView newView = background.render(this, view, offset);
        if (newView == null)
            return;
        Vector2f newOffset = new Vector2f(offset).add(scrollOffset);
        for (GuiComponent gui : children) {
            gui.render(newView, newOffset);
        }
    }

    @Override
    public void tick(double delta) {
        if (scrolling) {
            Vector2f pos = DisplayManager.getMousePos();
            if (scrollHorizontal)
                scrollOffset.x += pos.x - lastScrollPos.x;
            if (scrollVertical)
                scrollOffset.y += pos.y - lastScrollPos.y;
            lastScrollPos.set(pos.x, pos.y);
        }
    }

    @Override
    public boolean mousePressed(float x, float y) {
        ListIterator<GuiComponent> iter = children.listIterator(children.size());
        while (iter.hasPrevious()) {
            GuiComponent gui = iter.previous();
            if (gui.containsAbsolutePoint(x - scrollOffset.x, y - scrollOffset.y) && gui.mousePressed(x - scrollOffset.x, y - scrollOffset.y))
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
            if (gui.containsAbsolutePoint(x - scrollOffset.x, y - scrollOffset.y) && gui.mouseReleased(x - scrollOffset.x, y - scrollOffset.y))
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
