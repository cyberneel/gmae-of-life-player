package com.cyberneel.gameoflifeplayer;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.function.Supplier;

public class GameOfLifeCommands {
    public static void RegisterCommands() {
        // Grid Spawn Command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("spawn_gol_grid")
                    .executes(context -> {
                        ServerWorld world = context.getSource().getWorld();
                        BlockPos playerPos = context.getSource().getPlayer().getBlockPos();
                        BlockPos gridOrigin = playerPos.add(2, 1, 0); // Spawn grid 1 block above the player and side

                        // Clear the existing grid if it exists
                        if (GameOfLifePlayer.getCurrentGridOrigin() != null) {
                            clearGrid(world, GameOfLifePlayer.getCurrentGridOrigin());
                        } // else { // Clear all the other Grid blocks
                            // clearAllGridBlocks(world);
                        // }

                        // Spawn the new grid
                        spawnGrid(world, gridOrigin);

                        // Update the current grid's origin
                        GameOfLifePlayer.setCurrentGridOrigin(gridOrigin);

                        context.getSource().sendFeedback((Supplier<Text>) Text.literal("Spawned new grid at " + gridOrigin), false);
                        return 1;
                    })
            );
        });

        // Simulate Step Command
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("simulate_gol_step")
                    .then(CommandManager.argument("steps", IntegerArgumentType.integer())
                    .executes(context -> {
                        int steps = IntegerArgumentType.getInteger(context, "steps");
                        ServerWorld world = context.getSource().getWorld();

                        // Check if the grid exists
                        if (GameOfLifePlayer.getCurrentGridOrigin() == null) {
                            return 2;
                        }

                        BlockPos gridOrigin = GameOfLifePlayer.getCurrentGridOrigin();

                        for (int i = 0; i < steps; i++) {
                            simulateStep(world, gridOrigin);
                        }

                        return 1;
                    })
            ));
        });
    }

    // Clear All custom blocks
    public static void clearAllGridBlocks(ServerWorld world) {
        BlockPos startPos = new BlockPos(0, 0, 0);
        BlockPos endPos = new BlockPos(world.getHeight(), world.getHeight(), world.getHeight());

        BlockPos.stream(startPos, endPos).forEach(pos -> {
            if (world.getBlockState(pos).getBlock().getName() == GameOfLifePlayer.GRID_BLOCK.getName()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
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
        // Remove simulator block
        BlockPos blockPos =  origin.add(2, 0, -1);
        world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
    }

    // Spawns a new grid at the specified origin
    private static void spawnGrid(ServerWorld world, BlockPos origin) {
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                BlockPos blockPos = origin.add(x, 0, z);
                world.setBlockState(blockPos, GameOfLifePlayer.GRID_BLOCK.getDefaultState()); // Example block
            }
        }
        // Spawn a simulator block
        BlockPos blockPos =  origin.add(2, 0, -1);
        world.setBlockState(blockPos, GameOfLifePlayer.SIMULATOR_BLOCK.getDefaultState());
    }

    // Runs the simulation step
    private static void simulateStep(ServerWorld world, BlockPos origin) {
        // Get the current grid state so we can apply the rules
        boolean[][] blockStates = new boolean[10][10];
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                BlockPos blockPos = origin.add(x, 0, z);
                blockStates[x][z] = world.getBlockState(blockPos).get(GridBlock.COLOR) == DyeColor.RED;
            }
        }

        boolean[][] newBlockStates = new boolean[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Get the cell's neighbors
                int neighbors = 0;
                // Check all 8 possible neighbors
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        // Skip the cell itself
                        if (x == 0 && y == 0) continue;

                        // Check if the neighbor is within bounds
                        int ni = i + x;
                        int nj = j + y;
                        if (ni >= 0 && ni < 10 && nj >= 0 && nj < 10) {
                            if (blockStates[ni][nj]) {
                                neighbors++;
                            }
                        }
                    }
                }
                // Alive rules
                if (blockStates[i][j]) {
                    // underpopulation
                    if (neighbors < 2) {
                        newBlockStates[i][j] = false;
                    }
                    // sustained
                    if (neighbors == 2 || neighbors == 3) {
                        newBlockStates[i][j] = true;
                    }
                    // over population
                    if (neighbors > 3) {
                        newBlockStates[i][j] = false;
                    }
                } else { // Dead Rule
                    // Reproduction
                    if (neighbors == 3) {
                        newBlockStates[i][j] = true;
                    }
                }
            }
        }

        // Update the blocks in game
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                BlockPos blockPos = origin.add(x, 0, z);
                if (newBlockStates[x][z]) {
                    world.setBlockState(blockPos, world.getBlockState(blockPos).with(GridBlock.COLOR, DyeColor.RED));
                } else {
                    world.setBlockState(blockPos, world.getBlockState(blockPos).with(GridBlock.COLOR, DyeColor.BLACK));
                }
            }
        }
    }

}
