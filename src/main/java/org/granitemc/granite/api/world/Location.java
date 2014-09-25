package org.granitemc.granite.api.world;

public class Location implements Cloneable {

    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(final World world, final double x, final double y, final double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0;
        this.yaw = 0;
    }

    public Location(final World world, final double x, final double y, final double z, final float pitch, final float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
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
        } else if (location.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between different worlds");
        }

        return Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2) + Math.pow(z - location.z, 2);
    }

    public String toString() {
        return "Location{World:" + getWorld() + ",X:" + getX() + ",Y:" + getY() + ",Z:" + getZ() + ",Pitch:" + getPitch() + ",Yaw:" + getYaw() + "}";
    }
}
