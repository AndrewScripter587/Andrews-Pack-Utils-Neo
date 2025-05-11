package com.andrewgaming.aputils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class SetupCommands {

    public static void registerCommands() {
        NeoForge.EVENT_BUS.register(SetupCommands.class);
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
                .then(literal("loader")
                        .executes(context -> {
                            context.getSource().sendSuccess(() -> Component.literal("This subcommand exists for datapacks to detect which loader version of the mod is installed. This subcommand does nothing else other than return 1 on neoforge, and 0 on fabric."), false);
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
                        .requires(source -> source.hasPermission(2))
                        .then(argument("entities", EntityArgument.entities())
                                .executes(context -> {
                                    Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                    for (Entity entity : entities) {
                                        if (entity instanceof ServerPlayer) {
                                            context.getSource().sendFailure(Component.literal("§cPlayers aren't allowed, but the provided target selector references one or more player(s)."));
                                            AndrewsDatapackUtilities.LOGGER.info("Command failed because a player was included in the target selector. To bypass this (Don't unless you want crashes and/or major issues to happen!), add 'force' to the end of the command you used. Requires permission level 4 to use 'force'.");
                                            return -1;
                                        }
                                    }
                                    for (Entity entity : entities) {
                                        AndrewsDatapackUtilities.LOGGER.info("Despawning %s (UUID %s)".formatted(entity.getType().toString(), entity.getUUID()));
                                        entity.discard();
                                    }
                                    context.getSource().sendSuccess(() -> Component.literal(("Successfully despawned %s " + (entities.size() == 1 ? "entity" : "entities") + ".").formatted(entities.size())), true);
                                    return 1;
                                })
                                .then(literal("force")
                                        .requires(serverCommandSource -> serverCommandSource.hasPermission(4))
                                        .executes(context -> {
                                            Collection<? extends Entity> entities = EntityArgument.getEntities(context, "entities");
                                            context.getSource().sendSuccess(() -> Component.literal("Forcefully attempting to despawn. This WILL cause issues if a player is in the provided target selector."), true);
                                            for (Entity entity : entities) {
                                                AndrewsDatapackUtilities.LOGGER.info("Despawning %s (UUID %s)".formatted(entity.getType().toString(), entity.getUUID()));
                                                entity.discard();
                                            }
                                            context.getSource().sendSuccess(() -> Component.literal(("Successfully despawned %s " + (entities.size() == 1 ? "entity" : "entities") + ".").formatted(entities.size())), true);
                                            return 1;
                                        })
                                )
                        )
                )
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

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
            for (ServerLevel world : event.getServer().getAllLevels()) {
                for (Entity entity : world.getEntities().getAll()) {
                    if (entity instanceof IEntityDamageAccessor damageAccessor) {
//                        damageAccessor.resetDamageFlags();
                    }
                }
            }
    }

    private static int checkDamage(CommandSourceStack source, Entity target, String damagePredicate) throws CommandSyntaxException {
        source.sendFailure(Component.literal("Currently doesn't work in NeoForge. Please use Fabric to use the '/aputils check_damage' command."));
//        try {
//            if (!(target instanceof IEntityDamageAccessor damageAccessor)) {
//                source.sendFailure(Component.literal("Target entity does not support damage checking."));
//                return 0;
//            }
//
//            if (damageAccessor.hasTakenDamageThisTick()) {
//                if (damagePredicate == null) {
//                    source.sendSuccess(() -> Component.literal("The provided entity took damage!"), false);
//                    return 1;
//                } else {
//                    DamageSource lastSource = damageAccessor.getLastDamageSourceThisTick();
//
//                    if (lastSource != null) {
//                        DamageType damageTypeId = lastSource.type();
//                        String damageTypeIdStringified = damageTypeId.toString();
//                        source.sendSystemMessage(Component.literal("Type: " + damageTypeIdStringified));
//                        if (damagePredicate.equals(damageTypeIdStringified)) {
//                            AndrewsDatapackUtilities.LOGGER.info("Success, Type: " + damageTypeIdStringified);
//                            source.sendSuccess(() -> Component.literal("The provided entity took damage of type: " + damageTypeIdStringified), true);
//                            source.sendSystemMessage(Component.literal("Success"));
//                            return 1;
//                        } else {
//                            source.sendFailure(Component.literal("The entity took damage, but it did not match the specified damage type."));
//                            AndrewsDatapackUtilities.LOGGER.info("Failed, Type: " + damageTypeIdStringified);
//                            source.sendSuccess(() -> Component.literal("The type of damage that WAS taken is: " + damageTypeIdStringified), true);
//                            source.sendSystemMessage(Component.literal("Yes"));
//                            return 0;
//                        }
//                    } else {
//                        source.sendFailure(Component.literal("The entity took damage, but the damage source could not be determined."));
//                        source.sendSystemMessage(Component.literal("Failed"));
//                        return 0;
//                    }
//                }
//            } else {
//                source.sendFailure(Component.literal("The entity didn't take damage!"));
//                return 0;
//            }
//        } catch (Exception e) {
//            source.sendSystemMessage(Component.literal("Exception: " + e));
//        }
        return 0;
    }
}