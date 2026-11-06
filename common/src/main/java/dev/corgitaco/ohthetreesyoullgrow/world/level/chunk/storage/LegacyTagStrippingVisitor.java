package dev.corgitaco.ohthetreesyoullgrow.world.level.chunk.storage;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.visitors.FieldSelector;
import net.minecraft.nbt.visitors.SkipFields;

public class LegacyTagStrippingVisitor extends SkipFields {

    public static final String LEGACY_SCHEDULED_RANDOM_TICKS = "scheduled_random_ticks";
    public static final String STRIPPED_MARKER = "legacy_scheduled_random_ticks_stripped";

    private boolean stripped;

    public LegacyTagStrippingVisitor() {
        super(new FieldSelector(Constants.MOD_ID, ListTag.TYPE, LEGACY_SCHEDULED_RANDOM_TICKS),
                new FieldSelector("structures", Constants.MOD_ID, ListTag.TYPE, LEGACY_SCHEDULED_RANDOM_TICKS));
    }

    @Override
    public EntryResult visitEntry(TagType<?> type, String id) {
        EntryResult result = super.visitEntry(type, id);
        if (result == EntryResult.SKIP) {
            stripped = true;
        }
        return result;
    }

    public CompoundTag getResultWithMarker() {
        if (!(getResult() instanceof CompoundTag root)) {
            return null;
        }
        if (stripped && root.get("structures") instanceof CompoundTag structures) {
            CompoundTag modTag = structures.get(Constants.MOD_ID) instanceof CompoundTag existing ? existing : new CompoundTag();
            modTag.put(STRIPPED_MARKER, ByteTag.ONE);
            structures.put(Constants.MOD_ID, modTag);
        }
        return root;
    }
}
