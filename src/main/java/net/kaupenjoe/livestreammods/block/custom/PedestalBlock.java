package net.kaupenjoe.livestreammods.block.custom;

import com.mojang.serialization.MapCodec;
import net.kaupenjoe.livestreammods.block.ModBlocks;
import net.kaupenjoe.livestreammods.block.entity.ModBlockEntities;
import net.kaupenjoe.livestreammods.block.entity.custom.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

public class PedestalBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);
    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);

    public PedestalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return super.getOcclusionShape(state, level, pos);
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PedestalBlockEntity(pPos, pState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos,
                            BlockState pNewState, boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if(pLevel.getBlockEntity(pPos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                pedestalBlockEntity.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level pLevel, BlockPos pPos,
                                              Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult) {
        if(pLevel.getBlockEntity(pPos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            if(pStack.is(ModBlocks.SIDE_PEDESTAL.asItem())) {
                for(Vector2i offset : PedestalBlockEntity.offsets) {
                    if(isPositionEmpty(pLevel, pPos.offset(offset.x, 0, offset.y))) {
                        placePedestal(pLevel, pPos.offset(offset.x, 0, offset.y));
                        if(!pPlayer.isCreative()) {
                            pStack.shrink(1);
                        }
                        pLevel.playSound(pPlayer, pPos.offset(offset.x, 0, offset.y), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1f, 1f);
                    } else if(hasSidePedestal(pLevel, pPos.offset(offset.x, 0, offset.y))) {
                        continue;
                    } else {
                        // Any other block
                        pPlayer.displayClientMessage(Component.literal("Remove all obstacles in the way!"), false);
                        break;
                    }

                    break;
                }

                return ItemInteractionResult.SUCCESS;
            }

            if(pPlayer.isCrouching() && !pLevel.isClientSide()) {
                ((ServerPlayer) pPlayer).openMenu(new SimpleMenuProvider(pedestalBlockEntity, Component.literal("Pedestal")), pPos);
                return ItemInteractionResult.SUCCESS;
            }

            if(pedestalBlockEntity.inventory.getStackInSlot(0).isEmpty() && !pStack.isEmpty()) {
                pedestalBlockEntity.inventory.insertItem(0, pStack.copy(), false);
                if(!pPlayer.isCreative()) {
                    pStack.shrink(1);
                }
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 2f);
            } else if(pStack.isEmpty()) {
                ItemStack stackOnPedestal = pedestalBlockEntity.inventory.extractItem(0, 1, false);
                pPlayer.setItemInHand(InteractionHand.MAIN_HAND, stackOnPedestal);
                pedestalBlockEntity.clearContents();
                pLevel.playSound(pPlayer, pPos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
        }
        return ItemInteractionResult.SUCCESS;
    }



    private boolean isPositionEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    private boolean hasSidePedestal(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.SIDE_PEDESTAL);
    }

    private void placePedestal(Level level, BlockPos pos) {
        level.setBlockAndUpdate(pos, ModBlocks.SIDE_PEDESTAL.get().defaultBlockState());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }

        return createTickerHelper(pBlockEntityType, ModBlockEntities.MAIN_PEDESTAL_BE.get(),
                (level, blockPos, blockState, pedestalBlockEntity) -> pedestalBlockEntity.tick(level, blockPos, blockState));
    }
}
