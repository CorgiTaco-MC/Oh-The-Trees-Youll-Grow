package dev.corgitaco.ohthetreesyoullgrow.mixin.chunk;


import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChunkAccess.class)
public class MixinChunkAccess implements RandomTickScheduler {

    @Unique
    private final List<BlockPos> ohthetreesyoullgrow$scheduledRandomTicks = new ArrayList<>();


    @Override
    public void ohthetreesyoullgrow$scheduleRandomTick(BlockPos pos) {
        ohthetreesyoullgrow$scheduledRandomTicks.add(pos.immutable());
    }

    @Override
    public List<BlockPos> ohthetreesyoullgrow$getScheduledRandomTicks() {
        return ohthetreesyoullgrow$scheduledRandomTicks;
    }
}
