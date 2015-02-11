package org.granitepowered.granite.mc;

@Implement(name = "EntityZombie")
public interface MCEntityZombie extends MCEntityMob {

    void setChild(boolean child);

    void setScale(float scale);
}
