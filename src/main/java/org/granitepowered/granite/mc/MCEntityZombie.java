package org.granitepowered.granite.mc;

@Implement(name = "EntityZombie")
public interface MCEntityZombie extends MCEntityMob {

    MCBaseAttribute fieldGet$reinforcementsAttribute();

    void setChild(boolean child);

    void setScale(float scale);
}
