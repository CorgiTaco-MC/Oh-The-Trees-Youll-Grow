package dev.corgitaco.ohthetreesyoullgrow.platform;

import net.minecraft.Util;
import net.minecraft.core.Registry;

import java.nio.file.Path;
import java.util.ServiceLoader;
import java.util.function.Supplier;

public interface ModPlatform {

    ModPlatform INSTANCE = Util.make(() -> {
        final var loader = ServiceLoader.load(ModPlatform.class);
        final var it = loader.iterator();
        if (!it.hasNext()) {
            throw new RuntimeException("No Mod Platform was found on the classpath!");
        } else {
            final ModPlatform factory = it.next();
            if (it.hasNext()) {
                throw new RuntimeException("More than one Mod Platform was found on the classpath!");
            }
            return factory;
        }
    });

    Path configPath();

    boolean isModLoaded(String isLoaded);

    Platform modPlatform();

    <T> Supplier<T> register(Registry<? super T> registry, String name, Supplier<T> value);

    enum Platform {
        FORGE,
        FABRIC,
        NEOFORGE
    }
}
