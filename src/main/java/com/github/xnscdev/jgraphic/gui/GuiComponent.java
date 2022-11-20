package com.github.xnscdev.jgraphic.gui;

import com.github.xnscdev.jgraphic.util.DisplayManager;
import org.joml.Vector2f;

/**
 * Base class for all two-dimensional GUI components. This class manages common GUI-related data including
 * conversions between device coordinates and viewport coordinates, keyboard and mouse input, and GUI focusing.
 * @author XNSC
 * @see Gui
 */
public abstract class GuiComponent {
    protected GuiComponent parent;
    protected Vector2f position;
    protected Vector2f screenPosition;
    protected Vector2f size = new Vector2f();
    protected Vector2f screenSize = new Vector2f();

    protected GuiComponent(Vector2f position) {
        setPosition(position);
    }

    public abstract void render(GuiView view, Vector2f screenPos);

    /**
     * Gets the parent GUI. If a GUI's parent is not set, the GUI will not be rendered. All top-level GUIs have
     * a parent set to the root component.
     * @return the parent GUI, or {@code null} if the GUI has no parent
     * @see RootComponent
     */
    public final GuiComponent getParent() {
        return parent;
    }

    /**
     * Sets the parent GUI. This method should normally not be called directly, as unexpected behavior may occur
     * if the GUI is not in its parent GUI's list of children.
     * @param parent the parent GUI
     */
    public final void setParent(GuiComponent parent) {
        this.parent = parent;
    }

    /**
     * Gets the position of the GUI in device coordinates relative to its parent.
     * @return the position vector
     */
    public final Vector2f getPosition() {
        return position;
    }

    /**
     * Gets the position of the GUI in device coordinates relative to the top left corner of the screen.
     * @return the position vector
     */
    public Vector2f getAbsolutePosition() {
        return parent.getAbsolutePosition().add(position);
    }

    /**
     * Sets the position of the GUI. This function should always be called to change a GUI's position, since it updates
     * the internal viewport position. Never directly modify a GUI's position vector.
     * @param position the GUI position in device coordinates
     */
    public final void setPosition(Vector2f position) {
        this.position = position;
        this.screenPosition = new Vector2f(position.x / DisplayManager.getWidth() * 2, position.y / DisplayManager.getHeight() * -2);
    }

    /**
     * Gets the position of the GUI in viewport coordinates relative to its parent.
     * @return the position vector
     */
    public final Vector2f getScreenPosition() {
        return screenPosition;
    }

    /**
     * Gets the absolute position of the GUI in viewport coordinates. The returned result is the location
     * where the GUI will be rendered on-screen.
     * @return the position vector
     */
    public Vector2f getAbsoluteScreenPosition() {
        return parent.getAbsoluteScreenPosition().add(screenPosition);
    }

    /**
     * Gets the size of the GUI in device coordinates.
     * @return the size vector
     */
    public final Vector2f getSize() {
        return size;
    }

    /**
     * Sets the size of the GUI. This function should always be called to change a GUI's size, since it updates
     * the internal viewport size. Never directly modify a GUI's size vector.
     * @param size the GUI size in device coordinates
     */
    public final void setSize(Vector2f size) {
        this.size = size;
        this.screenSize = new Vector2f(size.x / DisplayManager.getWidth() * 2, size.y / DisplayManager.getHeight() * 2);
    }

    /**
     * Gets the size of the GUI in viewport coordinates.
     * @return the size vector
     */
    public final Vector2f getScreenSize() {
        return screenSize;
    }

    /**
     * This method is called every world tick and can be overridden by a GUI class for custom functionality.
     * @param delta number of seconds since the last world tick
     */
    public void tick(double delta) {
    }

    /**
     * This method is used to handle mouse presses. JGraphic handles mouse events by directing them only to the
     * lowest GUI covering the mouse cursor (i.e. no event bubbling occurs). Implementations of this method
     * should return {@code true} if their GUIs handle mouse events in any way, so both this method and
     * {@link GuiComponent#mouseReleased(float, float)} should always return the same value.
     * @param x X coordinate of the mouse cursor in device coordinates
     * @param y Y coordinate of the mouse cursor in device coordinates
     * @return whether a mouse event was handled
     */
    public boolean mousePressed(float x, float y) {
        return false;
    }

    /**
     * This method is used to handle mouse releases. JGraphic handles mouse events by directing them only to the
     * lowest GUI covering the mouse cursor (i.e. no event bubbling occurs). Implementations of this method
     * should return {@code true} if their GUIs handle mouse events in any way, so both this method and
     * {@link GuiComponent#mousePressed(float, float)} should always return the same value.
     * @param x X coordinate of the mouse cursor in device coordinates
     * @param y Y coordinate of the mouse cursor in device coordinates
     * @return whether a mouse event was handled
     */
    public boolean mouseReleased(float x, float y) {
        return false;
    }

    /**
     * This method may be overridden if special action must be taken when a GUI loses focus.
     */
    public void unfocused() {
    }

    /**
     * This method is called when the GUI receives a keyboard event. All keyboard events will trigger this function
     * even if no actual character would be inputted as a result of the event. Only the focused GUI can receive
     * keyboard events.
     * @param key constant representing the key pressed
     * @param action keyboard event action (press, release, or repeat)
     * @param mods bitwise OR of any modifier keys pressed during the event
     * @see com.github.xnscdev.jgraphic.util.Inputs
     */
    public void receiveKey(int key, int action, int mods) {
    }

    /**
     * This method is called when the GUI receives a character typed. This method is only called if a keyboard press
     * would cause an actual character to be output. Only the focused GUI can receive keyboard events.
     * @param codepoint the Unicode codepoint of the character typed
     */
    public void receiveChar(int codepoint) {
    }

    /**
     * Makes the current GUI become the focused GUI. This method is typically called from a mouse press or release
     * handler.
     */
    public final void takeFocus() {
        if (GuiManager.focusedComponent != null)
            GuiManager.focusedComponent.unfocused();
        GuiManager.focusedComponent = this;
    }

    /**
     * Drops focus from the current GUI, calling any unfocused handler if present. This method should not be called
     * if the current GUI is not the focused GUI.
     * @see GuiComponent#unfocused()
     */
    public final void dropFocus() {
        unfocused();
        GuiManager.focusedComponent = null;
    }

    /**
     * Determines whether a point lies within the bounds of a GUI.
     * @param x X coordinate
     * @param y Y coordinate
     * @return {@code true} if the point is contained within the GUI
     */
    public final boolean containsAbsolutePoint(float x, float y) {
        Vector2f pos = getAbsolutePosition();
        return x >= pos.x && x <= pos.x + size.x && y >= pos.y && y <= pos.y + size.y;
    }
}
