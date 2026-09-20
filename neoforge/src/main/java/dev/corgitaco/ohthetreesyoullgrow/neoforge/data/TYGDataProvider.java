package dev.corgitaco.ohthetreesyoullgrow.neoforge.data;

import dev.corgitaco.ohthetreesyoullgrow.Constants;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGConfiguredFeatures;
import dev.corgitaco.ohthetreesyoullgrow.data.worldgen.features.TYGPlacedFeatures;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

@EventBusSubscriber(modid = Constants.MOD_ID)
class TYGDataProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.FEATURE, context -> TYGConfiguredFeatures.CONFIGURED_FEATURES_FACTORIES.forEach((configuredFeatureResourceKey, configuredFeatureFactory) -> context.register(configuredFeatureResourceKey, configuredFeatureFactory.generate(context))))
            .add(Registries.PLACED_FEATURE, context -> TYGPlacedFeatures.PLACED_FEATURE_FACTORIES.forEach((placedFeatureResourceKey, placedFeatureFactory) -> context.register(placedFeatureResourceKey, placedFeatureFactory.generate(context.lookup(Registries.FEATURE)))));

    @SubscribeEvent
    private static void onGatherData(GatherDataEvent.Server event) {
        final DataGenerator gen = event.getGenerator();
        gen.addProvider(event.includeDev(), DatapackBuiltinEntriesProvider.forWorldLayer(gen.getPackOutput(), "Oh The Trees You'll Grow worldgen", event.getWorldLookupProvider(), BUILDER, Set.of(Constants.MOD_ID)));
    }
}
