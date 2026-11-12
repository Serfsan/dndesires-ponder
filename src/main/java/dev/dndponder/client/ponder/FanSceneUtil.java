package dev.dndponder.client.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


final class FanSceneUtil {
    static final int[] CATALYST = {4, 1, 2};
    static final int[] STAGING = {3, 1, 0};

    private FanSceneUtil() {}

    static void showBase(CreateSceneBuilder scene, SceneBuildingUtil util) {
        scene.configureBasePlate(1, 0, 5);
        scene.world().showSection(util.select().layer(0)
                .substract(util.select().position(0, 0, 4)), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(6, 1, 2, 5, 1, 2)
                .add(util.select().position(1, 1, 2)), Direction.DOWN);
        scene.idle(25);
    }

    static BlockPos catalystPos(SceneBuildingUtil util) {
        return util.grid().at(CATALYST[0], CATALYST[1], CATALYST[2]);
    }

    static Vec3 itemVec(SceneBuildingUtil util) {
        return util.vector().blockSurface(util.grid().at(1, 1, 2), Direction.EAST).add(0.1, 0, 0);
    }

    static ElementLink<WorldSectionElement> placeCatalyst(CreateSceneBuilder scene, SceneBuildingUtil util,
                                                          ElementLink<WorldSectionElement> previous, BlockState state) {
        BlockPos stagingPos = util.grid().at(STAGING[0], STAGING[1], STAGING[2]);
        BlockPos catalystPos = catalystPos(util);
        if (previous != null) {
            scene.world().hideIndependentSection(previous, Direction.NORTH);
            scene.idle(15);
        }
        scene.world().setBlock(stagingPos, state, false);
        scene.world().setBlock(catalystPos, state, previous != null);
        ElementLink<WorldSectionElement> link = scene.world().showIndependentSection(
                util.select().position(stagingPos), previous == null ? Direction.SOUTH : Direction.NORTH);
        scene.world().moveSection(link, util.vector().of(1, 0, 2), 0);
        scene.idle(20);
        return link;
    }

    static void throwItem(CreateSceneBuilder scene, SceneBuildingUtil util, ItemStack input, ParticleOptions particle) {
        Vec3 itemVec = itemVec(util);
        ElementLink<EntityElement> entity = scene.world().createItemEntity(
                util.vector().centerOf(catalystPos(util).west(2).above(2)), util.vector().of(0, 0.1, 0), input);
        scene.idle(15);
        scene.world().modifyEntity(entity, e -> e.setDeltaMovement(-0.2f, 0, 0));
        scene.overlay().showControls(itemVec, Pointing.DOWN, 20).withItem(input);
        scene.idle(20);
        scene.effects().emitParticles(itemVec.add(0, 0.2f, 0),
                scene.effects().simpleParticleEmitter(particle, Vec3.ZERO), 1, 60);
    }

    static void finishItem(CreateSceneBuilder scene, SceneBuildingUtil util, ItemStack output) {
        Vec3 itemVec = itemVec(util);
        scene.idle(60);
        scene.world().modifyEntities(ItemEntity.class, ie -> ie.setItem(output));
        scene.overlay().showControls(itemVec, Pointing.DOWN, 20).withItem(output);
        scene.idle(30);
        scene.world().modifyEntities(ItemEntity.class, Entity::discard);
        scene.idle(20);
    }
}