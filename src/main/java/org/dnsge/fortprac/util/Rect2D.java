package org.dnsge.fortprac.util;

public class Rect2D {
    public final Point2D position;
    public final int width;
    public final int height;

    public Rect2D(Point2D position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Rect2D(int x, int y, int width, int height) {
        this.position = new Point2D(x, y);
        this.width = width;
        this.height = height;
    }

    public static Rect2D centered(Point2D position, int width, int height) {
        int left = position.x() - width / 2;
        int top = position.y() - height / 2;
        return new Rect2D(new Point2D(left, top), width, height);
    }

    public Point2D center() {
        return new Point2D(this.position.x() + this.width / 2, this.position.y() + this.height / 2);
    }

    public Rect2D inset(int amount) {
        return Rect2D.centered(this.center(), this.width - amount * 2, this.height - amount * 2);
    }

    public Rect2D outset(int amount) {
        return Rect2D.centered(this.center(), this.width + amount * 2, this.height + amount * 2);
    }
}
