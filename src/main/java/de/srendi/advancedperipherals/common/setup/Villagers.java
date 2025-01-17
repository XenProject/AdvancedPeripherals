package de.srendi.advancedperipherals.common.setup;

import com.google.common.collect.ImmutableSet;
import dan200.computercraft.shared.Registry;
import de.srendi.advancedperipherals.AdvancedPeripherals;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;

public class Villagers {

    public static final RegistryObject<PointOfInterestType> COMPUTER_SCIENTIST_POI = Registration.POI_TYPES.register("computer_scientist",
            () -> new PointOfInterestType("computer_scientist", ImmutableSet.copyOf(Registry.ModBlocks.COMPUTER_ADVANCED.get().getStateContainer().getValidStates()), 1, 1));

    public static final RegistryObject<VillagerProfession> COMPUTER_SCIENTIST = Registration.VILLAGER_PROFESSIONS.register("computer_scientist",
            () -> new VillagerProfession(AdvancedPeripherals.MOD_ID + ":" + "computer_scientist", COMPUTER_SCIENTIST_POI.get(),
                    ImmutableSet.of(Items.COMPUTER_TOOL.get()), ImmutableSet.of(Blocks.ENVIRONMENT_DETECTOR.get(), Blocks.PLAYER_DETECTOR.get(), Registry.ModBlocks.COMPUTER_ADVANCED.get()), SoundEvents.ENTITY_VILLAGER_WORK_ARMORER));

    public static void register() {
    }

}
