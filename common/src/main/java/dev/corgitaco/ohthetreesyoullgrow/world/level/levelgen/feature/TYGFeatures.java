package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature;


import com.mojang.serialization.MapCodec;
import dev.corgitaco.ohthetreesyoullgrow.platform.ModPlatform;
import dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations.TreeFromStructureNBTConfigV2;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.function.Supplier;

public class TYGFeatures {
    public static final Supplier<MapCodec<TreeFromStructureNBTConfigV2>> TREE_FROM_NBT_V2 = ModPlatform.INSTANCE.register(BuiltInRegistries.FEATURE_TYPE, "tree_from_nbt_v2", () -> TreeFromStructureNBTConfigV2.CODEC);

    public static void register() {}
}
