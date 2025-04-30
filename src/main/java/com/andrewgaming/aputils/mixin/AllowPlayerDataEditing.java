package com.andrewgaming.aputils.mixin;

import net.minecraft.server.commands.data.EntityDataAccessor; // Corrected import and class name location
import net.minecraft.world.entity.player.Player; // Note the package change
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(EntityDataAccessor.class)
public class AllowPlayerDataEditing {


    @ModifyConstant(method = "setData", constant = @Constant(ordinal = 0, classValue = Player.class)) // ClassValue needs to be updated
    private static boolean playerCheckBypass(Object obj, Class<?> objclass){ // It's good practice to use wildcard or specific type
        return false;
    }

}