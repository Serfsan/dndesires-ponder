package dev.dndponder.client.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

// honestly I don't think I did a good job on these it's kind of lazy
public final class CatalystSailScenes {
    static final ResourceLocation SPLASHING = DnDIds.dnd("splashing_sail");
    static final ResourceLocation SMOKING = DnDIds.dnd("smoking_sail");
    static final ResourceLocation BLASTING = DnDIds.dnd("blasting_sail");
    static final ResourceLocation HAUNTING = DnDIds.dnd("haunting_sail");

    private CatalystSailScenes() {}

    private static BlockState sail(ResourceLocation id) {
        BlockState state = DnDIds.state(id);
        return state.hasProperty(BlockStateProperties.FACING)
                ? state.setValue(BlockStateProperties.FACING, Direction.EAST)
                : state;
    }

    public static void sails(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("dnd_catalyst_sails", "Processing with Catalyst Sails");
        FanSceneUtil.showBase(scene, util);

        BlockPos catalystPos = FanSceneUtil.catalystPos(util);
        Vec3 itemVec = FanSceneUtil.itemVec(util);
        ElementLink<WorldSectionElement> sail;

        sail = FanSceneUtil.placeCatalyst(scene, util, null, sail(SPLASHING));
        scene.overlay().showOutlineWithText(util.select().fromTo(catalystPos, catalystPos.west(2)), 90)
                .colored(PonderPalette.MEDIUM)
                .attachKeyFrame()
                .text("Catalyst Sails turn a fan's Air Flow into a processing setup without water, lava or fire");
        scene.idle(90);

        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.RED_WOOL), ParticleTypes.SPLASH);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .text("A Splashing Catalyst Sail washes items, just like water");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.WHITE_WOOL));

        sail = FanSceneUtil.placeCatalyst(scene, util, sail, sail(SMOKING));
        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.BEEF), ParticleTypes.SMOKE);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Smoking Catalyst Sail smokes food, just like fire");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.COOKED_BEEF));

        sail = FanSceneUtil.placeCatalyst(scene, util, sail, sail(BLASTING));
        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.RAW_IRON), ParticleTypes.LARGE_SMOKE);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Blasting Catalyst Sail smelts items, just like lava");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.IRON_INGOT));

        sail = FanSceneUtil.placeCatalyst(scene, util, sail, sail(HAUNTING));
        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.TORCH), ParticleTypes.SOUL_FIRE_FLAME);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Haunting Catalyst Sail haunts items, just like soul fire");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.SOUL_TORCH));

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Air passes through sails, and unlike lava or fire they never flow or spread");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .text("They also count as Windmill Sails");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .text("Sanding, Freezing, Seething and Dragon Breathing Catalyst Sails work the same way");
        scene.idle(100);
    }
}