package dev.corgitaco.ohthetreesyoullgrow.mixin.chunk;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.RandomTickScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SerializableChunkData.class)
public class SerializableChunkDataMixin {


    @Shadow @Final private CompoundTag structureData;

    @Inject(method = "copyOf", at = @At(value = "RETURN"))
    private static void writeScheduledRandomTicks(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<SerializableChunkData> cir) {
        SerializableChunkData returnValue = cir.getReturnValue();
        if (returnValue != null) {
            CompoundTag compoundTag = returnValue.structureData();
            List<BlockPos> scheduledRandomTicks = ((RandomTickScheduler) chunk).getScheduledRandomTicks();

            if (!scheduledRandomTicks.isEmpty()) {
                CompoundTag corgiLibTag = new CompoundTag();

                ListTag listTag = new ListTag();
                for (BlockPos scheduledRandomTick : scheduledRandomTicks) {
                    listTag.add(NbtUtils.writeBlockPos(scheduledRandomTick));
                }
                corgiLibTag.put("scheduled_random_ticks", listTag);

                compoundTag.put(Constants.MOD_ID, corgiLibTag);
            }
        }
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void readScheduledRandomTicks(ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos, CallbackInfoReturnable<ProtoChunk> cir) {
        if (this.structureData.contains(Constants.MOD_ID)) {
            CompoundTag corgiLibTag = this.structureData.getCompound(Constants.MOD_ID);
            if (corgiLibTag.contains("scheduled_random_ticks", Tag.TAG_LIST)) {
                for (Tag scheduledTick : corgiLibTag.getList("scheduled_random_ticks", Tag.TAG_COMPOUND)) {
                    int[] intArrayTag = ((IntArrayTag) scheduledTick).getAsIntArray();
                    ((RandomTickScheduler) cir.getReturnValue()).getScheduledRandomTicks().add(new BlockPos(intArrayTag[0], intArrayTag[1], intArrayTag[2]));
                }
            }
            this.structureData.remove(Constants.MOD_ID);
        }
    }
}
