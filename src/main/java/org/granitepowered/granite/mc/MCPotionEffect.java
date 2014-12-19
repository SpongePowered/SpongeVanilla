package org.granitepowered.granite.mc;

@Implement(name = "PotionEffect")
public interface MCPotionEffect extends MCInterface {

    int fieldGet$duration();

    void fieldSet$duration(int duration);

    int fieldGet$amplifier();

    void fieldSet$amplifier(int amplifier);

    boolean fieldGet$isAmbient();

    void fieldSet$isAmbient(boolean ambient);

    boolean fieldGet$showParticles();

    void fieldSet$showParticles(boolean particles);

}
