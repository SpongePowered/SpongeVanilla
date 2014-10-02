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

    /**
     * Returns the world of this location
     */
    public World getWorld() {
        return world;
    }

    /**
     * Sets the world of this location
     *
     * @param world The world
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Returns the X position of this location
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the X position of this location
     *
     * @param x The X position
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the Y position of this location
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y position of this location
     *
     * @param y The Y position
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the Z position of this location
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the Z position of this location
     *
     * @param z The Z position
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Returns the pitch of this location
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Sets the pitch of this location (up/down rotation)
     *
     * @param pitch The pitch
     */
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    /**
     * Returns the yaw of this location
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Sets the yaw of this location (left/right rotation)
     *
     * @param yaw The yaw
     */
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    /**
     * Returns the distance between this and another location
     * @param location The other location
     */
    public double getDistance(Location location) {
        return Math.sqrt(getDistanceSquared(location));
    }

    /**
     * Returns the squared distance between this and another location - this is MUCH faster than {@link #getDistance(Location)}
     * @param location The other location
     */
    public double getDistanceSquared(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Cannot measure to a null location");
        } else if (location.getWorld() != getWorld()) {
            throw new IllegalArgumentException("Cannot measure distance between different worlds");
        }

        return Math.pow(x - location.x, 2) + Math.pow(y - location.y, 2) + Math.pow(z - location.z, 2);
    }

    public String toString() {
        return "Location{World:" + getWorld().getLevelName() + ",X:" + getX() + ",Y:" + getY() + ",Z:" + getZ() + ",Pitch:" + getPitch() + ",Yaw:" + getYaw() + "}";
    }
}
