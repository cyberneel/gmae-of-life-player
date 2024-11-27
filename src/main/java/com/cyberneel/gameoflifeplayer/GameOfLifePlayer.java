package com.cyberneel.gameoflifeplayer;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOfLifePlayer implements ModInitializer {
	// We do this because it is going to be referenced throughout the project
	public static final String MOD_ID = "game-of-life-player";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Define the grid block
	public static final Block GRID_BLOCK = new Block(FabricBlockSettings.create().strength(1.0f));

	// Define the simulator block
	public static final Block SIMULATOR_BLOCK = new SimulatorBlock(FabricBlockSettings.create().strength(200.0f));

	private static BlockPos currentGridOrigin = null; // Tracks the origin of the existing grid

	public static void setCurrentGridOrigin(BlockPos origin) {
		currentGridOrigin = origin;
	}

	public static BlockPos getCurrentGridOrigin() {
		return currentGridOrigin;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		// Register the Grid Block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "grid_block"), GRID_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "grid_block"),
				new BlockItem(GRID_BLOCK, new Item.Settings()));
		LOGGER.info("Grid Block registered");

		// Register the simulator block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "simulator_block"), SIMULATOR_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "simulator_block"),
				new BlockItem(SIMULATOR_BLOCK, new Item.Settings()));
		System.out.println("Simulator block registered!");

		// Register Commands
		GameOfLifeCommands.RegisterCommands();
	}
}