package dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfigV2;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TYGConfiguredFeatures {
    public static final Map<ResourceKey<Feature>, ConfiguredFeatureFactory> CONFIGURED_FEATURES_FACTORIES = new Reference2ObjectOpenHashMap<>();

    private static final BlockPredicate GROWABLE_ON_SOIL = BlockPredicate.anyOf(BlockPredicate.matchesTag(BlockTags.DIRT), BlockPredicate.matchesTag(BlockTags.SUBSTRATE_OVERWORLD));

    public static final ResourceKey<Feature> V2_TEST_TREE1 = createConfiguredFeature("v2_test_tree_1", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.ACACIA_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.ACACIA_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(BlockStateProvider.holderOf(Blocks.MOSS_BLOCK))))
                    .build()
    );

    public static final ResourceKey<Feature> V2_TEST_TREE2 = createConfiguredFeature("v2_test_tree_2", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.JUNGLE_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.JUNGLE_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .build()
    );

    public static final ResourceKey<Feature> V2_TEST_TREE3 = createConfiguredFeature("v2_test_tree_3", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.of(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.EMERALD_BLOCK)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<Feature> V1_UPSIDE_DOWN_TEST_TREE1 = createConfiguredFeature("v2_upside_down_test_tree1", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.ACACIA_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.ACACIA_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(BlockStateProvider.holderOf(Blocks.MOSS_BLOCK))))
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<Feature> V1_UPSIDE_DOWN_TEST_TREE2 = createConfiguredFeature("v2_upside_down_test_tree2", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.JUNGLE_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.JUNGLE_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<Feature> V1_UPSIDE_DOWN_TEST_TREE3 = createConfiguredFeature("v2_upside_down_test_tree3", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.of(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.EMERALD_BLOCK)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.UPSIDE_DOWN)
                    .build()
    );

    public static final ResourceKey<Feature> V1_SIDEWAYS_TEST_TREE1 = createConfiguredFeature("v2_sideways_test_tree1", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.ACACIA_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.ACACIA_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .treeDecorators(List.of(new AlterGroundDecorator(BlockStateProvider.holderOf(Blocks.MOSS_BLOCK))))
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<Feature> V1_SIDEWAYS_TEST_TREE2 = createConfiguredFeature("v2_sideways_test_tree2", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.JUNGLE_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.JUNGLE_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(5)
                    .treeDecorators(List.of(new LeaveVineDecorator(0.5F), new BeehiveDecorator(0.2F)))
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<Feature> V1_SIDEWAYS_TEST_TREE3 = createConfiguredFeature("v2_sideways_test_tree3", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv1/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv1/test_tree_canopy1"))
                    .height(UniformInt.of(20, 25))
                    .logProvider(BlockStateProvider.of(Blocks.DIAMOND_BLOCK))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.EMERALD_BLOCK)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .orientation(TreeFromStructureNBTConfigV2.Orientation.SIDEWAYS)
                    .build()
    );

    public static final ResourceKey<Feature> V1_TEST_MUSHROOM1 = createConfiguredFeature("v2_test_mushroom_1", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_trunk1"))
                    .canopyLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.RED_MUSHROOM_BLOCK)))
                    .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                    .leavesTarget(List.of(Blocks.RED_MUSHROOM_BLOCK))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON))
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<Feature> V1_TEST_MUSHROOM2 = createConfiguredFeature("v2_test_mushroom_2", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_trunk1"))
                    .canopyLocation(Constants.createLocation("features/mushrooms/testv1/test_mushroom_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.MUSHROOM_STEM))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.BROWN_MUSHROOM_BLOCK)))
                    .logTarget(Set.of(Blocks.MUSHROOM_STEM))
                    .leavesTarget(List.of(Blocks.RED_MUSHROOM_BLOCK))
                    .growableOn(BlockPredicate.matchesTag(BlockTags.HUGE_BROWN_MUSHROOM_CAN_PLACE_ON))
                    .maxLogDepth(3)
                    .build()
    );

    public static final ResourceKey<Feature> V2_TEST_TREE4 = createConfiguredFeature("v2_test_tree_4", ctx ->
            new TreeFromStructureNBTConfigV2.Builder()
                    .baseLocation(Constants.createLocation("features/trees/testv2/test_tree_trunk1"))
                    .canopyLocation(Constants.createLocation("features/trees/testv2/test_tree_canopy1"))
                    .height(UniformInt.of(5, 10))
                    .logProvider(BlockStateProvider.of(Blocks.ACACIA_LOG))
                    .leavesProvider(List.of(BlockStateProvider.of(Blocks.OAK_LEAVES), BlockStateProvider.of(Blocks.OAK_LEAVES)))
                    .logTarget(Set.of(Blocks.OAK_LOG))
                    .leavesTarget(List.of(Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES))
                    .growableOn(GROWABLE_ON_SOIL)
                    .maxLogDepth(3)
                    .replaceFromNBT(Map.of(Blocks.SHROOMLIGHT, new WeightedStateProvider(WeightedList.<BlockState>builder().add(Blocks.GLOWSTONE.defaultBlockState(), 3).add(Blocks.GLASS.defaultBlockState(), 1).build())))
                    .treeDecorators(List.of(new AlterGroundDecorator(BlockStateProvider.holderOf(Blocks.MOSS_BLOCK))))
                    .build()
    );


    private static ResourceKey<Feature> createConfiguredFeature(String id, Function<BootstrapContext<Feature>, ? extends Feature> config) {
        Identifier tygID = Constants.createLocation(id);

        ResourceKey<Feature> featureResourceKey = ResourceKey.create(Registries.FEATURE, tygID);

        CONFIGURED_FEATURES_FACTORIES.put(featureResourceKey, config::apply);

        return featureResourceKey;
    }

    public static void register() {
    }

    @FunctionalInterface
    public interface ConfiguredFeatureFactory {
        Feature generate(BootstrapContext<Feature> configuredFeatureHolderGetter);
    }
}
