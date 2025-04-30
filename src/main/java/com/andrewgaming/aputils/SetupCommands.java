package com.andrewgaming.aputils;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import net.minecraft.commands.arguments.*;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.commands.CommandBuildContext;
import com.mojang.brigadier.CommandDispatcher; // Corrected import
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge; // Import NeoForge
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent; // Import SubscribeEvent annotation

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
public class SetupCommands {

    // No need to pass the IEventBus here anymore
    public static void registerCommands() {
        NeoForge.EVENT_BUS.register(SetupCommands.class); // Register the class itself to the NeoForge event bus
    }

    @SubscribeEvent // Mark this method to be called when RegisterCommandsEvent is fired
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext buildContext = event.getBuildContext();

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
                                                            entityIndex.addDeltaMovement(velocity);
                                                            entityIndex.hasImpulse = true;
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
                                                            entityIndex.setDeltaMovement(velocity);
                                                            entityIndex.hasImpulse = true;
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
        );
    }
}