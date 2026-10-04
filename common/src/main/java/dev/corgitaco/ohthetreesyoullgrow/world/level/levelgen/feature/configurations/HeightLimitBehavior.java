package dev.corgitaco.ohthetreesyoullgrow.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;

public enum HeightLimitBehavior {
    DEFAULT, // If tree generates above height limit. Block it from generating
    WORLD_HEIGHT_PASS_REMOVE_BLOCKS, // Allow trees the generate past the height limit and have blocks above the height limit removed.
    CHUNK_GENERATOR_HEIGHT_PASS_REMOVE_BLOCKS, // If tree generates above the defined chunk generator Y limit, remove blocks above the chunk generator height limit.
    CHUNK_GENERATOR_HEIGHT_BLOCK; // If tree generates above the defined chunk generator Y limit, block it from generating.


    public static final Codec<HeightLimitBehavior> CODEC = Codec.STRING.xmap(s -> HeightLimitBehavior.valueOf(s.toUpperCase()), s -> s.name().toUpperCase());
}
