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

    public void setXRotation(float xRotation) {
        this.xRotation = xRotation;
    }

    public float getYRotation() {
        return yRotation;
    }

    public void setYRotation(float yRotation) {
        this.yRotation = yRotation;
    }

    public float getZRotation() {
        return zRotation;
    }

    public void setZRotation(float zRotation) {
        this.zRotation = zRotation;
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
