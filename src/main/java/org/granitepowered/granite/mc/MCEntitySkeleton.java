package org.granitepowered.granite.mc;

@Implement(name = "EntitySkeleton")
public interface MCEntitySkeleton extends MCEntityMob {

    void setSkeletonType(int type);
}
