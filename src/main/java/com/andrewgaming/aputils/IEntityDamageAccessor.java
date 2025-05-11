package com.andrewgaming.aputils;

import net.minecraft.world.damagesource.DamageSource;

public interface IEntityDamageAccessor {
    DamageSource getLastDamageSourceThisTick();
    boolean hasTakenDamageThisTick();
    void resetDamageFlags();
}