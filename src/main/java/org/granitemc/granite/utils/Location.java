package org.granitemc.granite.utils;

public class Location implements Cloneable {

    private int dimension;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(final int dimension, final double x, final double y, final double z) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0;
        this.yaw = 0;
    }

    public Location(final int dimension, final double x, final double y, final double z, final float pitch, final float yaw) {
        this.dimension = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public double getDistance(Location location) {
        return Math.sqrt(getDistanceSquared(location));
    }

    public double getDistanceSquared(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Cannot measure to a null location");
        } else if (location.getDimension() != getDimension()) {
            throw new IllegalArgumentException("Cannot measure distance between different dimensions");
        }

        return Math.sqrt(x - location.x) + Math.sqrt(y - location.y) + Math.sqrt(z - location.z);
    }

    public String toString() {
        return "Location{Dimension:" + getDimension() + ",X:" + getX() + ",Y:" + getY() + ",Z:" + getZ() + ",Pitch:" + getPitch() + ",Yaw:" + getYaw() + "}";
    }
}
