package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfig;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TreeFromStructureNBTFeature extends Feature<TreeFromStructureNBTConfig> {

    private static final boolean DEBUG = false;

    public TreeFromStructureNBTFeature(Codec<TreeFromStructureNBTConfig> $$0) {
        super($$0);
    }

    @Override
    public boolean place(FeaturePlaceContext<TreeFromStructureNBTConfig> featurePlaceContext) {
        TreeFromStructureNBTConfig config = featurePlaceContext.config();

        BlockStateProvider logProvider = config.logProvider();
        BlockStateProvider leavesProvider = config.leavesProvider();

        WorldGenLevel level = featurePlaceContext.level();
        StructureTemplateManager templateManager = level.getLevel().getStructureManager();
        ResourceLocation baseLocation = config.baseLocation();
        Optional<StructureTemplate> baseTemplateOptional = templateManager.get(baseLocation);
        ResourceLocation canopyLocation = config.canopyLocation();
        Optional<StructureTemplate> canopyTemplateOptional = templateManager.get(canopyLocation);

        if (baseTemplateOptional.isEmpty()) {
            throw noTreePartPresent(baseLocation);
        }
        if (canopyTemplateOptional.isEmpty()) {
            throw noTreePartPresent(canopyLocation);
        }
        StructureTemplate baseTemplate = baseTemplateOptional.get();
        StructureTemplate canopyTemplate = canopyTemplateOptional.get();
        List<StructureTemplate.Palette> basePalettes = baseTemplate.palettes;
        List<StructureTemplate.Palette> canopyPalettes = canopyTemplate.palettes;
        BlockPos origin = featurePlaceContext.origin();
        if (DEBUG) {
            level.setBlock(origin, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        }
        RandomSource random = featurePlaceContext.random();
        StructurePlaceSettings placeSettings = new StructurePlaceSettings().setRotation(Rotation.getRandom(random));
        StructureTemplate.Palette trunkBasePalette = placeSettings.getRandomPalette(basePalettes, origin);
        StructureTemplate.Palette randomCanopyPalette = placeSettings.getRandomPalette(canopyPalettes, origin);

        List<StructureTemplate.StructureBlockInfo> center = trunkBasePalette.blocks(Blocks.WHITE_WOOL);

        if (center.isEmpty()) {
            throw new IllegalArgumentException("No trunk central position was specified for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }
        if (center.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one trunk central position for structure NBT palette %s. Trunk central position is specified with white wool.".formatted(config.baseLocation()));
        }

        BlockPos centerOffset = center.getFirst().pos();
        centerOffset = new BlockPos(-centerOffset.getX(), 0, -centerOffset.getZ());


        List<StructureTemplate.StructureBlockInfo> logs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), trunkBasePalette);
        List<StructureTemplate.StructureBlockInfo> logBuilders = trunkBasePalette.blocks(Blocks.RED_WOOL);
        if (logBuilders.isEmpty()) {
            throw new UnsupportedOperationException(String.format("\"%s\" is missing log builders.", baseLocation));
        }

        int trunkLength = config.height().sample(random);
        final int maxTrunkBuildingDepth = config.maxLogDepth();

        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            if (!isOnGround(config.maxLogDepth(), level, pos, config.growableOn())) {
                return false; // Exit because all positions are not on ground.
            }
        }

        Map<BlockPos, BlockState> leavePositions = new HashMap<>();
        Map<BlockPos, BlockState> logPositions = new HashMap<>();

        fillTrunkPositions(logProvider, leavesProvider, config, level, random, origin, placeSettings, trunkBasePalette, centerOffset, logs, logBuilders, leavePositions, logPositions, maxTrunkBuildingDepth);

        // Verify the canopy has connected with all trunk positions
        if(!fillCanopyPositions(trunkBasePalette.blocks(Blocks.YELLOW_WOOL), config, level, random, placeSettings, centerOffset, origin, randomCanopyPalette, leavePositions, logPositions, trunkLength)) {
            return false;
        }

        if (config.isSapling()) {
            if (validateLogPositions(logPositions, config, level)) {
                return false; // Exit because some positions are not valid.
            }
        }

        if (insideStructure(logPositions, level, config)) {
            return false; // Exit because the trunk position intersects with a structure.
        }

        placeKnownLogPositions(logPositions, level);
        placeKnownLeavePositions(leavePositions, level);

        placeAdditional(config, level, origin, placeSettings, trunkBasePalette, centerOffset);
        placeAdditional(config, level, origin, placeSettings, randomCanopyPalette, centerOffset);


        Set<BlockPos> decorationPositions = new HashSet<>();
        placeTreeDecorations(config.treeDecorators(), level, random, leavePositions.keySet(), logPositions.keySet(), decorationPositions);

        return true;
    }

    private static boolean fillCanopyPositions(List<StructureTemplate.StructureBlockInfo> canopyAnchor, TreeFromStructureNBTConfig config, WorldGenLevel level, RandomSource randomSource, StructurePlaceSettings placeSettings, BlockPos centerOffset, BlockPos origin, StructureTemplate.Palette randomCanopyPalette, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> logPositions, int trunkLength) {
        if (!canopyAnchor.isEmpty()) {
            if (canopyAnchor.size() > 1) {
                throw new IllegalArgumentException("There cannot be more than one central canopy position. Canopy central position is specified with yellow wool on the trunk palette.");
            }
            return fillCanopyPositions(config.logProvider(), config.leavesProvider(), config, level, randomSource, getModifiedPos(placeSettings, canopyAnchor.getFirst(), centerOffset, origin), placeSettings, randomCanopyPalette, leavePositions, logPositions, trunkLength);
        } else {
           return fillCanopyPositions(config.logProvider(), config.leavesProvider(), config, level, randomSource, origin, placeSettings, randomCanopyPalette, leavePositions, logPositions, trunkLength);
        }
    }


    private static boolean insideStructure(Map<BlockPos, BlockState> logPositions, WorldGenLevel level, TreeFromStructureNBTConfig config) {
        if (level instanceof WorldGenRegion region) {
            for (BlockPos trunkPosition : logPositions.keySet()) {
                ChunkAccess chunk = level.getChunk(trunkPosition);
                for (StructureStart value : chunk.getAllStarts().values()) {
                    for (StructurePiece piece : value.getPieces()) {
                        if (piece.getBoundingBox().isInside(trunkPosition) && !testValidPos(config, level, trunkPosition)) {
                            return true;
                        }
                    }
                }

                for (Map.Entry<Structure, LongSet> entry : chunk.getAllReferences().entrySet()) {
                    Structure structure = entry.getKey();
                    LongSet references = entry.getValue();
                    for (long reference : references) {
                        int chunkX = ChunkPos.getX(reference);
                        int chunkZ = ChunkPos.getZ(reference);
                        if (!region.hasChunk(chunkX, chunkZ)) {
                            continue;
                        }
                        ChunkAccess referenceChunk = region.getChunk(chunkX, chunkZ, ChunkStatus.STRUCTURE_STARTS, true);

                        StructureStart startForStructure = referenceChunk.getStartForStructure(structure);
                        if (startForStructure != null) {
                            for (StructurePiece piece : startForStructure.getPieces()) {
                                if (piece.getBoundingBox().isInside(trunkPosition) && !testValidPos(config, level, trunkPosition)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean validateLogPositions(Map<BlockPos, BlockState> logPositions, TreeFromStructureNBTConfig config, WorldGenLevel level) {
        for (BlockPos trunkPosition : logPositions.keySet()) {
            if (!testValidPos(config, level, trunkPosition)) {
                return true;
            }
        }
        return false;
    }

    private static boolean testValidPos(TreeFromStructureNBTConfig config, WorldGenLevel level, BlockPos trunkPosition) {
        return config.leavesPlacementFilter().test(level, trunkPosition);
    }

    private static void placeKnownLogPositions(Map<BlockPos, BlockState> trunkPositions, WorldGenLevel level) {
        for (Map.Entry<BlockPos, BlockState> entry : trunkPositions.entrySet()) {
            BlockPos trunkPosition = entry.getKey();
            BlockState state = entry.getValue();
            level.setBlock(trunkPosition, state, 2);
        }
    }

    private static void placeKnownLeavePositions(Map<BlockPos, BlockState> leavePositions, WorldGenLevel level) {
        List<Runnable> leavesPostApply = new ArrayList<>(leavePositions.size());

        for (Map.Entry<BlockPos, BlockState> entry : leavePositions.entrySet()) {
            BlockPos leavePosition = entry.getKey();
            BlockState state = entry.getValue();
            level.setBlock(leavePosition, state, 2);
            if (state.hasProperty(LeavesBlock.DISTANCE)) {
                Runnable postProcess = () -> {
                    BlockState blockState = LeavesBlock.updateDistance(state, level, leavePosition);
                    if (blockState.getValue(LeavesBlock.DISTANCE) < LeavesBlock.DECAY_DISTANCE) {
                        if (blockState.hasProperty(LeavesBlock.PERSISTENT)) {
                            blockState = blockState.setValue(LeavesBlock.PERSISTENT, false);
                        }
                        level.setBlock(leavePosition, blockState, 2);
                        level.scheduleTick(leavePosition, blockState.getBlock(), 0);
                    } else {
                        level.removeBlock(leavePosition, false);
                        leavePositions.remove(leavePosition.immutable());
                    }
                };
                leavesPostApply.add(postProcess);
            }
        }

        leavesPostApply.forEach(Runnable::run);
    }

    public static void placeAdditional(TreeFromStructureNBTConfig config, WorldGenLevel level, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette palette, BlockPos centerOffset) {
        List<StructureTemplate.StructureBlockInfo> additionalBlocks = getStructureInfosInStructurePalletteFromBlockList(config.placeFromNBT(), palette);
        for (StructureTemplate.StructureBlockInfo additionalBlock : additionalBlocks) {
            BlockPos pos = getModifiedPos(placeSettings, additionalBlock, centerOffset, origin);
            level.setBlock(pos, additionalBlock.state(), 2);
            ((RandomTickScheduler) level.getChunk(pos)).scheduleRandomTick(pos.immutable());
        }
    }

    public static void fillTrunkPositions(BlockStateProvider logProvider, BlockStateProvider leavesProvider, TreeFromStructureNBTConfig config, WorldGenLevel level, RandomSource randomSource, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette trunkBasePalette, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, List<StructureTemplate.StructureBlockInfo> logBuilders, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> trunkPositions, int maxTrunkBuildingDepth) {
        fillLogsUnder(logProvider, level, randomSource, origin, placeSettings, centerOffset, logBuilders, maxTrunkBuildingDepth, config.growableOn(), trunkPositions);
        placeLogsWithRotation(logProvider, level, randomSource, origin, placeSettings, centerOffset, logs, trunkPositions);
        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, randomSource, placeSettings, getStructureInfosInStructurePalletteFromBlockList(config.leavesTarget(), trunkBasePalette), leavePositions, centerOffset, config.leavesPlacementFilter());
    }

    public static boolean fillCanopyPositions(BlockStateProvider logProvider, BlockStateProvider leavesProvider, TreeFromStructureNBTConfig config, WorldGenLevel level, RandomSource randomSource, BlockPos origin, StructurePlaceSettings placeSettings, StructureTemplate.Palette randomCanopyPalette, Map<BlockPos, BlockState> leavePositions, Map<BlockPos, BlockState> trunkPositions, int trunkLength) {
        List<StructureTemplate.StructureBlockInfo> leaves = getStructureInfosInStructurePalletteFromBlockList(config.leavesTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyLogs = getStructureInfosInStructurePalletteFromBlockList(config.logTarget(), randomCanopyPalette);
        List<StructureTemplate.StructureBlockInfo> canopyAnchor = randomCanopyPalette.blocks(Blocks.WHITE_WOOL);

        if (canopyAnchor.isEmpty()) {
            throw new IllegalArgumentException("No canopy anchor was specified for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }
        if (canopyAnchor.size() > 1) {
            throw new IllegalArgumentException("There cannot be more than one canopy anchor for structure NBT palette %s. Canopy anchor is specified with white wool.".formatted(config.canopyLocation()));
        }

        StructureTemplate.StructureBlockInfo structureBlockInfo = canopyAnchor.getFirst();
        BlockPos canopyCenterOffset = structureBlockInfo.pos();
        canopyCenterOffset = new BlockPos(-canopyCenterOffset.getX(), trunkLength, -canopyCenterOffset.getZ());

        List<StructureTemplate.StructureBlockInfo> trunkFillers = new ArrayList<>(randomCanopyPalette.blocks(Blocks.RED_WOOL));

        if(!intersectTrunk(logProvider, level, randomSource, origin, placeSettings, canopyCenterOffset, trunkFillers, trunkLength + 1, trunkPositions)) {
            return false;
        }

        placeLogsWithRotation(logProvider, level, randomSource, origin, placeSettings, canopyCenterOffset, canopyLogs, trunkPositions);
        placeLeavesWithCalculatedDistanceAndRotation(leavesProvider, level, origin, randomSource, placeSettings, leaves, leavePositions, canopyCenterOffset, config.leavesPlacementFilter());
        return true;
    }

    public static void placeLogsWithRotation(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logs, Map<BlockPos, BlockState> trunkPositions) {
        for (StructureTemplate.StructureBlockInfo trunk : logs) {
            BlockPos pos = getModifiedPos(placeSettings, trunk, centerOffset, origin);
            trunkPositions.put(pos.immutable(), getTransformedState(pos, logProvider.getState(random, pos), trunk.state(), placeSettings.getRotation(), level));
        }
    }

    public static void placeTreeDecorations(Iterable<TreeDecorator> treeDecorators, WorldGenLevel level, RandomSource random, Set<BlockPos> leavePositions, Set<BlockPos> trunkPositions, Set<BlockPos> decorationPositions) {
        for (TreeDecorator treeDecorator : treeDecorators) {
            treeDecorator.place(new TreeDecorator.Context(level, (pos, state) -> {
                level.setBlock(pos, state, 2);
                decorationPositions.add(pos.immutable());
            }, random, trunkPositions, leavePositions, trunkPositions));
        }
    }

    public static void placeLeavesWithCalculatedDistanceAndRotation(BlockStateProvider leavesProvider, WorldGenLevel level, BlockPos origin, RandomSource random, StructurePlaceSettings placeSettings, List<StructureTemplate.StructureBlockInfo> leaves, Map<BlockPos, BlockState> leavePositions, BlockPos canopyCenterOffset, BlockPredicate leavesPlacementFilter) {
        for (StructureTemplate.StructureBlockInfo leaf : leaves) {
            BlockPos modifiedPos = getModifiedPos(placeSettings, leaf, canopyCenterOffset, origin);

            if (leavesPlacementFilter.test(level, modifiedPos)) {
                leavePositions.put(modifiedPos.immutable(), getTransformedState(modifiedPos, leavesProvider.getState(random, modifiedPos), leaf.state(), placeSettings.getRotation(), level));
            }
        }
    }

    public static void fillLogsUnder(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logBuilders, int maxTrunkBuildingDepth, BlockPredicate groundFilter, Map<BlockPos, BlockState> trunkPositions) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);

            for (int i = 0; i < maxTrunkBuildingDepth; i++) {
                if (!groundFilter.test(level, mutableBlockPos) && !level.getBlockState(mutableBlockPos).is(Blocks.BEDROCK)) {
                    trunkPositions.put(mutableBlockPos.immutable(), getTransformedState(mutableBlockPos, logProvider.getState(random, mutableBlockPos), logBuilder.state(), placeSettings.getRotation(), level));
                    mutableBlockPos.move(Direction.DOWN);
                } else {
                    ((RandomTickScheduler) level.getChunk(mutableBlockPos)).scheduleRandomTick(mutableBlockPos.immutable());
                    break;
                }
            }
        }
    }

    public static boolean intersectTrunk(BlockStateProvider logProvider, WorldGenLevel level, RandomSource random, BlockPos origin, StructurePlaceSettings placeSettings, BlockPos centerOffset, List<StructureTemplate.StructureBlockInfo> logBuilders, int maxTrunkBuildingDepth, Map<BlockPos, BlockState> trunkPositions) {
        for (StructureTemplate.StructureBlockInfo logBuilder : logBuilders) {
            BlockPos pos = getModifiedPos(placeSettings, logBuilder, centerOffset, origin);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);

            for (int i = 0; i <= maxTrunkBuildingDepth; i++) {
                if (!trunkPositions.containsKey(mutableBlockPos)) {
                    trunkPositions.put(mutableBlockPos.immutable(), getTransformedState(mutableBlockPos, logProvider.getState(random, mutableBlockPos), logBuilder.state(), placeSettings.getRotation(), level));
                    mutableBlockPos.move(Direction.DOWN);
                } else {
                    break;
                }

                if (i == maxTrunkBuildingDepth) {
                    return false;
                }
            }
        }

        return true;
    }


    @NotNull
    public static BlockState getTransformedState(BlockPos modifiedPos, BlockState state, BlockState nbtState, Rotation rotation, WorldGenLevel level) {
        for (Property property : state.getProperties()) {
            if (nbtState.hasProperty(property)) {
                Comparable value = nbtState.getValue(property);
                state = state.setValue(property, value);
            }
        }

        if (state.hasProperty(LeavesBlock.WATERLOGGED)) {
            FluidState fluidState = level.getFluidState(modifiedPos);
            if (fluidState.is(Fluids.WATER) && fluidState.getAmount() >= 7) {
                state = state.setValue(LeavesBlock.WATERLOGGED, true);
            } else {
                state = state.setValue(LeavesBlock.WATERLOGGED, false);
            }
        }

        state = state.rotate(rotation);

        return state;
    }

    public static boolean isOnGround(int maxLogDepth, WorldGenLevel level, BlockPos pos, BlockPredicate growableOn) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos().set(pos);
        for (int logDepth = 0; logDepth < maxLogDepth; logDepth++) {
            mutableBlockPos.move(Direction.DOWN);
            if (growableOn.test(level, mutableBlockPos)) {
                return true;
            }
        }

        return false;
    }

    public static BlockPos getModifiedPos(StructurePlaceSettings settings, StructureTemplate.StructureBlockInfo placing, BlockPos partCenter, BlockPos featureOrigin) {
        return StructureTemplate.calculateRelativePosition(settings, placing.pos()).offset(featureOrigin).offset(StructureTemplate.calculateRelativePosition(settings, partCenter));
    }

    public static IllegalArgumentException noTreePartPresent(ResourceLocation location) {
        return new IllegalArgumentException(String.format("\"%s\" is not a valid tree part.", location));
    }

    public static List<StructureTemplate.StructureBlockInfo> getStructureInfosInStructurePalletteFromBlockList(Iterable<Block> blocks, StructureTemplate.Palette palette) {
        List<StructureTemplate.StructureBlockInfo> result = new ArrayList<>();
        for (Block block : blocks) {
            result.addAll(palette.blocks(block));
        }
        return result;
    }
}
