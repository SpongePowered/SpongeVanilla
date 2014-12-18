package org.granitepowered.granite.mc;

@Implement(name = "WorldBorder")
public interface MCWorldBorder extends MCInterface {

    double getDiameter();

    double fieldGet$startDiameter();

    long getTimeRemaining();

    double fieldGet$centerX();

    double fieldGet$centerZ();

    void setCenter(double x, double z);

    int fieldGet$warningTime();

    void setWarningTime(int ticks);

    int fieldGet$warningDistance();

    void setWarningDistance(int blocks);

    double fieldGet$blockBuffer();

    void setBlockBuffer(int buffer);

    double fieldGet$damageAmount();

    void setDamageAmount(int damage);

}
