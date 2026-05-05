package dev.trashpanda.fromashes.client;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import dev.trashpanda.fromashes.worldgen.FromAshesWorldPlacedFeatures;
import dev.trashpanda.fromashes.worldgen.FromAshesWorldConfiguredFeatures;
import dev.trashpanda.fromashes.worldgen.FromAshesWorldgenProvider;

public class FromAshesDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(FromAshesWorldgenProvider::new);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.CONFIGURED_FEATURE, FromAshesWorldConfiguredFeatures::configure);
		registryBuilder.add(Registries.PLACED_FEATURE, FromAshesWorldPlacedFeatures::configure);
	}
}
