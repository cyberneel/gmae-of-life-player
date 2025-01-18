package com.cyberneel.gameoflifeplayer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.CommandBlockMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SimulatorBlock extends Block {
    public SimulatorBlock(FabricBlockSettings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            // Simulate the next step
            GameOfLifePlayer.LOGGER.info("Simulator Block Clicked");
            SimulateNextStep((ServerWorld) world, pos);
            player.getServer().getCommandManager().executeWithPrefix(player.getServer().getCommandSource(), "simulate_gol_step 1");
        }
        return ActionResult.SUCCESS;
    }

    private void SimulateNextStep(ServerWorld world, BlockPos simulatorPos) {
        // Placeholder: Print all nearby grid blocks (future grid logic here)
        GameOfLifePlayer.LOGGER.info("Simulating next step...");
    }
}
