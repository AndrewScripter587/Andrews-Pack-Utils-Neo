package com.andrewgaming.aputils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.*;

import java.util.Objects;
import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SetupCommands {

    public static void registerCommands() {
        NeoForge.EVENT_BUS.register(SetupCommands.class);
        NeoForge.EVENT_BUS.addListener(SetupCommands::onServerTick);
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(literal("aputils")
                .then(literal("calc")
                        .then(literal("add")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 + value2;
                                                    context.getSource().sendSuccess(() -> Component.literal(String.valueOf(value1) + " + " + String.valueOf(value2) + " = " + result), false);
                                                    return (int) result;
                                                })
                                        )
                                ))
                        .then(literal("sub")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 - value2;
                                                    context.getSource().sendSuccess(() -> Component.literal(String.valueOf(value1) + " - " + String.valueOf(value2) + " = " + result), false);
                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("mul")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 * value2;
                                                    context.getSource().sendSuccess(() -> Component.literal(String.valueOf(value1) + " * " + String.valueOf(value2) + " = " + result), false);
                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("div")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = value1 / value2;
                                                    context.getSource().sendSuccess(() -> Component.literal(String.valueOf(value1) + " / " + String.valueOf(value2) + " = " + result), false);
                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("power")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .then(argument("value2", DoubleArgumentType.doubleArg())
                                                .executes(context -> {
                                                    final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                                    final double value2 = DoubleArgumentType.getDouble(context, "value2");
                                                    final double result = Math.pow(value1, value2);
                                                    context.getSource().sendSuccess(() -> Component.literal(value1 + " ^ " + value2 + " = " + result), false);
                                                    return (int) result;
                                                })
                                        )
                                )
                        )
                        .then(literal("sqrt")
                                .then(argument("value1", DoubleArgumentType.doubleArg())
                                        .executes(context -> {
                                            final double value1 = DoubleArgumentType.getDouble(context, "value1");
                                            final double result = Math.sqrt(value1);
                                            context.getSource().sendSuccess(() -> Component.literal("Sqrt of " + value1 + " = " + result), false);
                                            return (int) result;
                                        })
                                )
                        )
                )
                .then(literal("heartbeat")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal("This subcommand exists for datapacks to detect this mod being installed. This subcommand does nothing else other than return 1."), false);
                            return 1;
                        })
                )
                .then(literal("velocity")
                        .requires(source -> source.hasPermission(2))
                        .then(argument("entities", EntityArgument.entities())
                                .then(argument("Vector", Vec3Argument.vec3(false))
                                        .executes(context -> {
                                            try {
                                                Vec3 velocity = Vec3Argument.getVec3(context, "Vector");
                                                Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                                for (Entity entityIndex : entities) {
                                                    entityIndex.setDeltaMovement(velocity);
                                                    entityIndex.hasImpulse = true;
                                                    if (entityIndex instanceof ServerPlayer) { // Only send packet to players
                                                        ((ServerPlayer) entityIndex).connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityIndex));
                                                    }
                                                    System.out.println("The new velocity is " + entityIndex.getDeltaMovement().toString());
                                                }
                                            } catch (Throwable e) {
                                                context.getSource().sendFailure(Component.literal("An error occurred: " + e));
                                                return -1;
                                            }
                                            return 1;
                                        })
                                        .then(literal("add")
                                                .executes(context -> {
                                                    try {
                                                        Vec3 velocity = Vec3Argument.getVec3(context, "Vector");
                                                        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                                        for (Entity entityIndex : entities) {
                                                            entityIndex.hasImpulse = true;
                                                            entityIndex.addDeltaMovement(velocity);
                                                            if (entityIndex instanceof ServerPlayer) { // Only send packet to players
                                                                ((ServerPlayer) entityIndex).connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityIndex));
                                                            }
                                                            System.out.println("The new velocity is " + entityIndex.getDeltaMovement().toString());
                                                        }
                                                    } catch (Throwable e) {
                                                        context.getSource().sendFailure(Component.literal("An error occurred: " + e));
                                                        return -1;
                                                    }
                                                    return 1;
                                                })
                                        )
                                        .then(literal("set")
                                                .executes(context -> {
                                                    try {
                                                        Vec3 velocity = Vec3Argument.getVec3(context, "Vector");
                                                        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                                        for (Entity entityIndex : entities) {
                                                            // For setting velocity:
                                                            entityIndex.setDeltaMovement(velocity);
                                                            entityIndex.hasImpulse = true;
                                                            if (entityIndex instanceof ServerPlayer) { // Only send packet to players
                                                                ((ServerPlayer) entityIndex).connection.send(new net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket(entityIndex));
                                                            }
                                                            System.out.println("The new velocity is " + entityIndex.getDeltaMovement().toString());
                                                        }
                                                    } catch (Throwable e) {
                                                        context.getSource().sendFailure(Component.literal("An error occurred: " + e));
                                                        return -1;
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
                .then(literal("attack_cooldown")
                        .then(argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    float cooldown = player.getAttackStrengthScale(0.0F);
                                    context.getSource().sendSuccess(() -> Component.literal("The attack cooldown progress of %s is %s".formatted(context.getSource().getDisplayName().getString(), cooldown)), true);
                                    return (int) (cooldown * 100);
                                })
                        )
                )
                .then(literal("despawn")
                .then(literal("check_damage")
                        .requires(source -> source.hasPermission(2))
                        .then(argument("target", EntityArgument.entity())
                                .executes(context -> checkDamage(context.getSource(), EntityArgument.getEntity(context, "target"), null))
                                .then(argument("damage_predicate", StringArgumentType.string())
                                        .executes(context -> checkDamage(context.getSource(), EntityArgument.getEntity(context, "target"), StringArgumentType.getString(context, "damage_predicate")))
                                )
                        )
                )
        );
    }

    public static void onServerTick(ServerTickEvent event) {
            for (ServerLevel world : event.getServer().getAllLevels()) {
                for (Entity entity : world.getEntities().getAll()) {
                    if (entity instanceof IEntityDamageAccessor damageAccessor) {
                        damageAccessor.resetDamageFlags();
                    }
                }
            }
    }

    private static int checkDamage(CommandSourceStack source, Entity target, String damagePredicate) throws CommandSyntaxException {
        if (!(target instanceof IEntityDamageAccessor damageAccessor)) {
            source.sendFailure(Component.literal("Target entity does not support damage checking."));
            return 0;
        }

        if (damageAccessor.hasTakenDamageThisTick()) {
            if (damagePredicate == null) {
                source.sendSuccess(() -> Component.literal("The provided entity took damage!"), false);
                return 1;
            } else {
                DamageSource lastSource = damageAccessor.getLastDamageSourceThisTick();
                if (lastSource != null) {
                    DamageType damageTypeId = lastSource.type();
                    String damageTypeIdStringified = damageTypeId.toString();

                    if (damagePredicate.equals(damageTypeIdStringified)) {
                        source.sendSuccess(() -> Component.literal("The provided entity took damage of type: " + damageTypeIdStringified), true);
                        return 1;
                    } else {
                        source.sendFailure(Component.literal("The entity took damage, but it did not match the specified damage type."));
                        source.sendSuccess(() -> Component.literal("The type of damage that WAS taken is: " + damageTypeIdStringified), true);
                        return 0;
                    }
                } else {
                    source.sendFailure(Component.literal("The entity took damage, but the damage source could not be determined."));
                    return 0;
                }
            }
        } else {
            source.sendFailure(Component.literal("The entity didn't take damage!"));
            return 0;
        }
    }
}