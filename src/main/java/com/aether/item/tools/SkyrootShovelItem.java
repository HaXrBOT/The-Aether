package com.aether.item.tools;

import com.aether.registry.AetherItemGroups;
import com.aether.registry.AetherItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ShovelItem;

public class SkyrootShovelItem extends ShovelItem
{
    public SkyrootShovelItem() {
        super(AetherItemTier.SKYROOT, 1.5F, -3.0F, new Item.Properties().group(AetherItemGroups.AETHER_TOOLS));
    }
}
