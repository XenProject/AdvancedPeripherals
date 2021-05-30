package de.srendi.advancedperipherals.common.addons.computercraft.proxyintegrations.create;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.schematics.block.SchematicannonTileEntity;
import dan200.computercraft.api.lua.LuaFunction;
import de.srendi.advancedperipherals.common.addons.computercraft.base.ProxyIntegration;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SchematicannonIntegration extends ProxyIntegration<SchematicannonTileEntity> {
    @Override
    protected Class<SchematicannonTileEntity> getTargetClass() {
        return SchematicannonTileEntity.class;
    }

    @Override
    public SchematicannonIntegration getNewInstance() {
        return new SchematicannonIntegration();
    }

    @Override
    protected String getName() {
        return "schematicannon";
    }

    @LuaFunction
    public final Object getNeededItem() {
        ItemStack missingItem = getTileEntity().missingItem;

        if (missingItem == null) {
            return null;
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("name", missingItem.getItem().getRegistryName().toString());
        resultMap.put("amount", missingItem.getCount());
        resultMap.put("displayName", missingItem.getDisplayName().getString());

        return resultMap;
    }

    @LuaFunction
    public final String getState() {
        return getTileEntity().state.toString();
    }

    @LuaFunction
    public final void setState(SchematicannonTileEntity.State value) {
        getTileEntity().state = value;
    }

    @LuaFunction
    public final float getFuelLevel() {
        return getTileEntity().fuelLevel;
    }

    @LuaFunction
    public final boolean hasSchematic() {
        return getTileEntity().inventory.getStackInSlot(0).isItemEqual(AllItems.SCHEMATIC.asStack());
    }

    @LuaFunction
    public final int getPlacedCount() {
        return getTileEntity().blocksPlaced;
    }

    @LuaFunction
    public final int getCountToPlace() {
        return getTileEntity().blocksToPlace;
    }

    @LuaFunction
    public final float getProgress() {
        return getTileEntity().schematicProgress;
    }

    @LuaFunction
    public final int refuel(int gunpowderCount) {
        SchematicannonTileEntity te = getTileEntity();
        LinkedHashSet<LazyOptional<IItemHandler>> inventories = te.attachedInventories;
        AtomicInteger count = new AtomicInteger(0);

        inventories.stream().forEach(inventory -> {
            if (inventory.isPresent()) {
                IItemHandler existingInv = inventory.resolve().get();
                int slotCount = existingInv.getSlots();

                for (int i = 0; i < slotCount; i++) {
                    if (existingInv.getStackInSlot(i).isItemEqual(new ItemStack(Items.GUNPOWDER))) {
                        ItemStack extracted = existingInv.extractItem(i, gunpowderCount, false);
                        int extractedCount = extracted.getCount();
                        int curCount = count.addAndGet(extractedCount);

                        if (extractedCount > 0) {
                            ItemStack notInserted = te.inventory.insertItem(4, extracted, false);

                            if (!notInserted.isEmpty()) {
                                existingInv.insertItem(i, notInserted, false);
                                count.addAndGet(-notInserted.getCount());
                            }
                        }

                        if (curCount == gunpowderCount) {
                            break;
                        }
                    }
                }
            }
        });

        return count.get();
    }
}
