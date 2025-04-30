package com.andrewgaming.aputils.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.DataAccessor;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Debug(
        export = true
)
@Mixin({EntityDataAccessor.class})
public abstract class AllowPlayerDataEditing implements DataAccessor {
    @Shadow
    private Entity entity;

    public void setData(CompoundTag other) throws CommandSyntaxException {
        UUID uuid = this.entity.getUUID();
        this.entity.load(other);
        this.entity.setUUID(uuid);
    }
}