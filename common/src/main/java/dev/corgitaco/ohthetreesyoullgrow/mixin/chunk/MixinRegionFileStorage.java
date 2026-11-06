package dev.corgitaco.ohthetreesyoullgrow.mixin.chunk;

import dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.storage.LegacyTagStrippingVisitor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.chunk.storage.RegionFileStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.DataInput;
import java.io.IOException;

@Mixin(RegionFileStorage.class)
public class MixinRegionFileStorage {

    @Redirect(method = "read", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtIo;read(Ljava/io/DataInput;)Lnet/minecraft/nbt/CompoundTag;"))
    private CompoundTag ohthetreesyoullgrow$readSkippingLegacyTicks(DataInput input) throws IOException {
        LegacyTagStrippingVisitor visitor = new LegacyTagStrippingVisitor();
        NbtIo.parse(input, visitor);
        CompoundTag result = visitor.getResultWithMarker();
        if (result == null) {
            throw new IOException("Root tag must be a named compound tag");
        }
        return result;
    }
}
