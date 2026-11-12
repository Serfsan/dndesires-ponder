package dev.dndponder.client.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.dndponder.DnDPonder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

final class ShowcaseUtil {
    static final ResourceLocation BASE_5 = ResourceLocation.fromNamespaceAndPath(DnDPonder.MOD_ID, "base_5");
    static final ResourceLocation BASE_7 = ResourceLocation.fromNamespaceAndPath(DnDPonder.MOD_ID, "base_7");

    private ShowcaseUtil() {}

    static BlockState dnd(String path, String... props) {
        return with(DnDIds.state(DnDIds.dnd(path)), props);
    }

    static BlockState create(String path, String... props) {
        return with(DnDIds.state(ResourceLocation.fromNamespaceAndPath("create", path)), props);
    }

    static BlockState mc(String path, String... props) {
        return with(DnDIds.state(ResourceLocation.withDefaultNamespace(path)), props);
    }

    static BlockState with(BlockState state, String... props) {
        for (int i = 0; i + 1 < props.length; i += 2) {
            Property<?> property = state.getBlock().getStateDefinition().getProperty(props[i]);
            if (property != null) state = setParsed(state, property, props[i + 1]);
        }
        return state;
    }

    private static <T extends Comparable<T>> BlockState setParsed(BlockState state, Property<T> property, String value) {
        return property.getValue(value).map(v -> state.setValue(property, v)).orElse(state);
    }

    static void begin(CreateSceneBuilder scene, SceneBuildingUtil util, int size) {
        scene.configureBasePlate(0, 0, size);
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(10);
    }

    static void place(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, BlockState state) {
        scene.world().setBlock(pos, state, false);
        scene.world().showSection(util.select().position(pos), Direction.DOWN);
        scene.idle(5);
    }

    static void spin(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, float rpm) {
        scene.world().setKineticSpeed(util.select().position(pos), rpm);
    }
}
