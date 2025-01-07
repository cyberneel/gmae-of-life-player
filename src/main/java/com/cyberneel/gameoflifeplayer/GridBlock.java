package com.cyberneel.gameoflifeplayer;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.RunArgs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.StateManager;
import net.minecraft.world.WorldAccess;

public class GridBlock extends Block {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.of("color", DyeColor.class);

    public GridBlock(FabricBlockSettings settings) {
        super(settings);
        // Set the default state
        this.setDefaultState(this.getStateManager().getDefaultState().with(COLOR, DyeColor.BLACK));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        // Add the color property to the block's state
        builder.add(COLOR);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient()) {
            // Toggle color between black and red
            DyeColor currentColor = state.get(COLOR);
            DyeColor nextColor = (currentColor == DyeColor.BLACK) ? DyeColor.RED : DyeColor.BLACK;

            // Update the block's state with the new color
            world.setBlockState(pos, state.with(COLOR, nextColor));
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);

        // Break surrounding grid blocks and trigger cascading effect
        BlockPos.stream(pos.add(-1, 0, -1), pos.add(1, 0, 1)).forEach(blockPos -> {
            BlockState blockState = world.getBlockState(blockPos);
            if (blockState.isOf(this)) { // Check if the block is the same type
                world.breakBlock(blockPos, false);
                // Trigger onBroken manually for the surrounding block
                blockState.getBlock().onBroken(world, blockPos, blockState);
            }
        });
    }
}
