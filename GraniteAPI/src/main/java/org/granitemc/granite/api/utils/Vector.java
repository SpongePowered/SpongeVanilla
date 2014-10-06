package org.granitemc.granite.api.utils;

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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class Vector {
    double x;
    double y;
    double z;

    public static Vector zero = new Vector(0, 0, 0);

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public Vector normalize() {
        Vector clone = copy();

        double scaling = Math.sqrt(x * x + y * y + z * z);

        if (scaling < 1.0E-4D) {
            clone.x = 0;
            clone.y = 0;
            clone.z = 0;
        } else {
            clone.x /= scaling;
            clone.y /= scaling;
            clone.z /= scaling;
        }
        return clone;
    }

    public Vector copy() {
        return new Vector(x, y, z);
    }

    public double distanceSquared(Vector other) {
        double xx = other.x - x;
        double yy = other.y - y;
        double zz = other.z - z;
        return Math.abs(xx * xx + yy * yy + zz * zz);
    }

    public double distance(Vector other) {
        return Math.sqrt(distanceSquared(other));
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y, z + other.z);
    }

    public Vector subtract(Vector other) {
        return new Vector(x - other.x, y - other.y, z - other.z);
    }

    public Vector scale(double scalar) {
        return new Vector(x * scalar, y * scalar, z * scalar);
    }

    public Vector scale(double xScalar, double yScalar, double zScalar) {
        return new Vector(x * xScalar, y * yScalar, z * zScalar);
    }
}
