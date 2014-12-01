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

public class Rotations {

    float xRotation;
    float yRotation;
    float zRotation;

    public Rotations(float xRotation, float yRotation, float zRotation) {
        this.xRotation = xRotation;
        this.yRotation = yRotation;
        this.zRotation = zRotation;
    }

    public float getXRotation() {
        return xRotation;
    }

    public Rotations setXRotation(float xRotation) {
        return new Rotations(xRotation, yRotation, zRotation);
    }

    public float getYRotation() {
        return yRotation;
    }

    public Rotations setYRotation(float yRotation) {
        return new Rotations(xRotation, yRotation, zRotation);
    }

    public float getZRotation() {
        return zRotation;
    }

    public Rotations setZRotation(float zRotation) {
        return new Rotations(xRotation, yRotation, zRotation);
    }

    public boolean equals(Object object) {
        if (object instanceof Rotations) {
            Rotations rotations = (Rotations) object;
            return getXRotation() == rotations.getXRotation() && getYRotation() == rotations.getYRotation() && getZRotation() == rotations.getZRotation();
        }
        return false;
    }

    public String toString() {
        return "{xRotation:" + getXRotation() + ",yRotation:" + getYRotation() + ",zRotation:" + getZRotation() + "}";
    }

}
