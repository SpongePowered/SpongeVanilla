/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.api.utils;

import org.granitemc.granite.api.world.World;

public class Location {

    private World world;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public Location(final double x, final double y, final double z) {
        this.world = null;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = 0;
        this.yaw = 0;
    }

    public Location(final double x, final double y, final double z, final float pitch, final float yaw) {
        this.world = null;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

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
     * Returns a new Location with the world set
     *
     * @param world The world
     */
    public Location setWorld(World world) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the X position of this location
     */
    public double getX() {
        return x;
    }

    /**
     * Returns a new Location with the X position set
     *
     * @param x The X position
     */
    public Location setX(double x) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the Y position of this location
     */
    public double getY() {
        return y;
    }

    /**
     * Returns a new Location with the Y position set
     *
     * @param y The Y position
     */
    public Location setY(double y) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the Z position of this location
     */
    public double getZ() {
        return z;
    }

    /**
     * Returns a new Location with the Z position set
     *
     * @param z The Z position
     */
    public Location setZ(double z) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the pitch of this location
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Returns a new Location with the pitch (up/down) rotation set
     *
     * @param pitch The pitch
     */
    public Location setPitch(float pitch) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the yaw of this location
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Returns a new Location with the yaw (left/right) rotation set
     *
     * @param yaw The yaw
     */
    public Location setYaw(float yaw) {
        return new Location(world, x, y, z, pitch, yaw);
    }

    /**
     * Returns the distance between this and another location
     *
     * @param location The other location
     */
    public double getDistance(Location location) {
        return Math.sqrt(getDistanceSquared(location));
    }

    /**
     * Returns the squared distance between this and another location - this is MUCH faster than {@link #getDistance(Location)}
     *
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return Float.compare(location.pitch, pitch) == 0 && Double.compare(location.x, x) == 0 && Double.compare(location.y, y) == 0
                && Float.compare(location.yaw, yaw) == 0 && Double.compare(location.z, z) == 0 && world.equals(location.world);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = world.hashCode();
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + (yaw != +0.0f ? Float.floatToIntBits(yaw) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "world=" + world +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                '}';
    }
}
