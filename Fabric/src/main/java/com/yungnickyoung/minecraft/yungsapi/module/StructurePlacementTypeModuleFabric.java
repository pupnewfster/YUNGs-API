package com.yungnickyoung.minecraft.yungsapi.module;

import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegistrationManager;
import com.yungnickyoung.minecraft.yungsapi.autoregister.AutoRegisterField;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

/**
 * Registration of StructurePlacementTypes.
 */
public class StructurePlacementTypeModuleFabric {
    public static void processEntries() {
        AutoRegistrationManager.STRUCTURE_PLACEMENT_TYPES.stream()
                .filter(data -> !data.processed())
                .forEach(StructurePlacementTypeModuleFabric::register);
    }

    private static void register(AutoRegisterField data) {
        Registry.register(BuiltInRegistries.STRUCTURE_PLACEMENT, data.name(), (StructurePlacementType<?>) data.object());
        data.markProcessed();
    }
}