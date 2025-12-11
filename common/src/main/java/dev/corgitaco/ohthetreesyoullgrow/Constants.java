package dev.corgitaco.ohthetreesyoullgrow;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "ohthetreesyoullgrow";
	public static final String MOD_NAME = "Oh The Trees You'll Grow";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

	public static Identifier createLocation(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

    public static <T>ResourceKey<T> key(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, createLocation(path));
    }
}