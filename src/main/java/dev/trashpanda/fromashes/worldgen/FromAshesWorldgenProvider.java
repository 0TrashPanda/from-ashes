package dev.trashpanda.fromashes.worldgen;

import java.util.concurrent.CompletableFuture;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;

public class FromAshesWorldgenProvider extends FabricDynamicRegistryProvider {
	public FromAshesWorldgenProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
	}

	@Override
	public String getName() {
		return "From Ashes Worldgen";
	}
}
