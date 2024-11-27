package com.cyberneel.gameoflifeplayer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.function.Supplier;

public class GameOfLifeCommands {
    public static void RegisterCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("spawn_gol_grid")
                    .executes(context -> {
                        ServerWorld world = context.getSource().getWorld();
                        BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
                        BlockPos gridOrigin = playerPos.add(0, 1, 0); // Spawn grid 1 block above the player

                        // Clear the existing grid if it exists
                        if (GameOfLifePlayer.getCurrentGridOrigin() != null) {
                            clearGrid(world, GameOfLifePlayer.getCurrentGridOrigin());
                        }

                        // Spawn the new grid
                        spawnGrid(world, gridOrigin);

                        // Update the current grid's origin
                        GameOfLifePlayer.setCurrentGridOrigin(gridOrigin);

                        context.getSource().sendFeedback((Supplier<Text>) Text.literal("Spawned new grid at " + gridOrigin), false);
                        return 1;
                    })
            );
        });
    }

    // Clears the blocks of the existing grid
    private static void clearGrid(ServerWorld world, BlockPos origin) {
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                BlockPos blockPos = origin.add(x, 0, z);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            }
        }
    }

    // Spawns a new grid at the specified origin
    private static void spawnGrid(ServerWorld world, BlockPos origin) {
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                BlockPos blockPos = origin.add(x, 0, z);
                world.setBlockState(blockPos, GameOfLifePlayer.GRID_BLOCK.getDefaultState()); // Example block
            }
        }
    }

}
