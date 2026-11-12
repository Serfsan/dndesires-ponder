package dev.dndponder.client.ponder;

import dev.dndponder.DnDTarget;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;

public final class DnDIds {
    public static final ResourceLocation HYDRAULIC_PRESS = dnd("hydraulic_press");

    public static final List<ResourceLocation> ALL_CATALYST_SAILS = List.of(
            dnd("splashing_sail"),
            dnd("smoking_sail"),
            dnd("blasting_sail"),
            dnd("haunting_sail"),
            dnd("sanding_sail"),
            dnd("freezing_sail"),
            dnd("seething_sail"),
            dnd("dragon_breathing_sail"));

    public static final ResourceLocation ENCASED_FAN = ResourceLocation.fromNamespaceAndPath("create", "encased_fan");

    public static final ResourceLocation CREATE_COMPACTING_SCHEMATIC = ResourceLocation.fromNamespaceAndPath("create", "mechanical_press/compacting");
    public static final ResourceLocation CREATE_FAN_PROCESSING_SCHEMATIC = ResourceLocation.fromNamespaceAndPath("create", "fan/processing");

    private DnDIds() {}

    public static ResourceLocation dnd(String path) {
        return ResourceLocation.fromNamespaceAndPath(DnDTarget.DND_MOD_ID, path);
    }

    public static boolean hasBlock(ResourceLocation id) {
        return BuiltInRegistries.BLOCK.containsKey(id);
    }

    public static boolean hasItem(ResourceLocation id) {
        return BuiltInRegistries.ITEM.containsKey(id);
    }

    public static BlockState state(ResourceLocation id) {
        return BuiltInRegistries.BLOCK.getOptional(id).map(Block::defaultBlockState).orElse(Blocks.AIR.defaultBlockState());
    }

    public static ItemStack stack(ResourceLocation id, int count) {
        return BuiltInRegistries.ITEM.getOptional(id).map(item -> new ItemStack((Item) item, count)).orElse(ItemStack.EMPTY);
    }

    public static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property) {
        if (from.hasProperty(property) && to.hasProperty(property)) return to.setValue(property, from.getValue(property));
        return to;
    }
}