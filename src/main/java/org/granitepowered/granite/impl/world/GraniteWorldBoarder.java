package org.granitepowered.granite.impl.world;

import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector3d;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCWorldBorder;
import org.spongepowered.api.world.WorldBorder;

public class GraniteWorldBoarder extends Composite<MCWorldBorder> implements WorldBorder {
    public GraniteWorldBoarder(Object obj) {
        super(obj);
    }

    @Override
    public double getNewRadius() {
        return obj.getDiameter();
    }

    @Override
    public double getRadius() {
        return obj.fieldGet$startDiameter();
    }

    @Override
    public void setRadius(double size) {
        throw new NotImplementedException("");
    }

    @Override
    public void setRadius(double v, long l) {
        throw new NotImplementedException("");
    }

    @Override
    public long getTimeRemaining() {
        return obj.getTimeRemaining();
    }

    @Override
    public void setCenter(double x, double z) {
        obj.setCenter(x, z);
    }

    @Override
    public Vector3d getCenter() {
        return new Vector3d(new Vector2d(obj.fieldGet$centerX(), obj.fieldGet$centerZ()));
    }

    @Override
    public int getWarningTime() {
        return obj.fieldGet$warningTime();
    }

    @Override
    public void setWarningTime(int ticks) {
        obj.setWarningTime(ticks);
    }

    @Override
    public int getWarningDistance() {
        return obj.fieldGet$warningDistance();
    }

    @Override
    public void setWarningDistance(int blocks) {
        obj.setWarningDistance(blocks);
    }

    @Override
    public int getBlockBuffer() {
        return (int) obj.fieldGet$blockBuffer();
    }

    @Override
    public void setBlockBuffer(int buffer) {
        obj.setBlockBuffer(buffer);
    }

    @Override
    public int getDamageAmount() {
        return (int) obj.fieldGet$damageAmount();
    }

    @Override
    public void setDamageAmount(int damage) {
        obj.setDamageAmount(damage);
    }
}
