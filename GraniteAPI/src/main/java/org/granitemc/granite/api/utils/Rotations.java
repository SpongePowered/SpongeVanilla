package org.granitemc.granite.api.utils;

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
        return "{xRotation:" + getXRotation() + ",yRotation:" + getYRotation() +",zRotation:" + getZRotation() + "}";
    }

}
