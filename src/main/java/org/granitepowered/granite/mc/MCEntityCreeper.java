package org.granitepowered.granite.mc;

@Implement(name = "EntityCreeper")
public interface MCEntityCreeper extends MCEntityMob {

    int fieldGet$explosionRadius();

    void fieldSet$explosionRadius(int explosionRadius);

    int fieldGet$fuseTime();

    void fieldSet$fuseTime(int fuseTime);

    int fieldGet$lastActiveTime();

    void fieldSet$lastActiveTime(int lastActiveTime);

    void fieldSet$timeSinceIgnited(int timeSinceIgnited);

    void detonate();
}
