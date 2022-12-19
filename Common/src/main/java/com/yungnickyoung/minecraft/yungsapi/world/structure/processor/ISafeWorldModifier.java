package com.yungnickyoung.minecraft.yungsapi.world.structure.processor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import java.util.Optional;

/**
 * Utility methods that bypass the PaletteContainer's lock, as it was causing an
 * `Accessing PalettedContainer from multiple threads` crash, even though everything
 * seemed to be safe.
 *
 * This crash started occurring in 1.17. I currently do not know the cause, so this
 * is a workaround in the meantime.
 *
 * @author TelepathicGrunt
 * @author YUNGNICKYOUNG
 */
public interface ISafeWorldModifier {
    /**
     * Safe method for grabbing a FluidState.
     */
    default FluidState getFluidStateSafe(LevelChunkSection chunkSection, BlockPos pos) {
        if (chunkSection == null) return Fluids.EMPTY.defaultFluidState();
        return chunkSection.getFluidState(
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ()));
    }

    /**
     * Safe method for grabbing a FluidState.
     */
    default FluidState getFluidStateSafe(LevelReader world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = world.getSectionIndex(pos.getY());
        LevelChunkSection chunkSection = chunk.getSection(sectionYIndex);
        return getFluidStateSafe(chunkSection, pos);
    }

    /**
     * Safe method for grabbing a BlockState.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     */
    default Optional<BlockState> getBlockStateSafe(LevelChunkSection chunkSection, BlockPos pos) {
        if (chunkSection == null) return Optional.empty();
        return Optional.of(chunkSection.getBlockState(
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ())));
    }

    /**
     * Safe method for grabbing a BlockState.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     */
    default Optional<BlockState> getBlockStateSafe(LevelReader world, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = world.getSectionIndex(pos.getY());
        LevelChunkSection chunkSection = chunk.getSection(sectionYIndex);
        return getBlockStateSafe(chunkSection, pos);
    }

    /**
     * Safely checks if the BlockState at a given position is air.
     */
    default boolean isBlockStateAirSafe(LevelReader world, BlockPos pos) {
        Optional<BlockState> blockState = getBlockStateSafe(world, pos);
        return blockState.isPresent() && blockState.get().isAir();
    }

    /**
     * Safely checks if the Material at a given position is liquid.
     */
    default boolean isMaterialLiquidSafe(LevelReader world, BlockPos pos) {
        Optional<BlockState> blockState = getBlockStateSafe(world, pos);
        return blockState.isPresent() && blockState.get().getMaterial().isLiquid();
    }

    /**
     * Safe method for setting a BlockState.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     */
    default Optional<BlockState> setBlockStateSafe(LevelChunkSection chunkSection, BlockPos pos, BlockState state) {
        if (chunkSection == null) return Optional.empty();
        return Optional.of(chunkSection.setBlockState(
                SectionPos.sectionRelative(pos.getX()),
                SectionPos.sectionRelative(pos.getY()),
                SectionPos.sectionRelative(pos.getZ()),
                state,
                false));
    }

    /**
     * Safe method for setting a BlockState.
     * This bypasses the PaletteContainer's lock as it was causing a
     * `Accessing PalettedContainer from multiple threads` crash, even though everything
     * seemed to be safe.
     */
    default Optional<BlockState> setBlockStateSafe(LevelReader world, BlockPos pos, BlockState state) {
        ChunkPos chunkPos = new ChunkPos(pos);
        ChunkAccess chunk = world.getChunk(chunkPos.x, chunkPos.z);
        int sectionYIndex = chunk.getSectionIndex(pos.getY());
        LevelChunkSection chunkSection = chunk.getSection(sectionYIndex);
        return setBlockStateSafe(chunkSection, pos, state);
    }
}
