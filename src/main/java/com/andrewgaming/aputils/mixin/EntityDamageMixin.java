package com.andrewgaming.aputils.mixin;

import net.minecraft.world.entity.Entity; // Target Entity instead of LivingEntity
import net.minecraft.world.damagesource.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class) // Target Entity
public abstract class EntityDamageMixin implements com.andrewgaming.aputils.IEntityDamageAccessor {
    @Unique
    private DamageSource lastDamageSourceThisTick = null;

    @Unique
    private boolean hasTakenDamageThisTick = false;

    @Shadow
    public abstract boolean isAlive();

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true) // Use "hurt" and ensure cancellable if needed
    private void onHurt(DamageSource damageSource, float amount, CallbackInfo ci) { // Return boolean
        if (isAlive()) {
            this.lastDamageSourceThisTick = damageSource;
            this.hasTakenDamageThisTick = true;
        }
    }

    @Unique
    public DamageSource getLastDamageSourceThisTick() {
        return this.lastDamageSourceThisTick;
    }

    @Unique
    public boolean hasTakenDamageThisTick() {
        return this.hasTakenDamageThisTick;
    }

    @Unique
    public void resetDamageFlags() {
        this.lastDamageSourceThisTick = null;
        this.hasTakenDamageThisTick = false;
    }
}