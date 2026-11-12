package dev.dndponder.client.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** Need to add more details later about use cases besides the basic info I have
 *  Sanding: exposed copper > copper block
 *  Freezing: packed ice > blue ice
 *  Seething: ender pearl > eye of ender
 *  Dragon egg/POW head: glass bottle > dragon's breath
 */
public final class FanCatalystScenes {

    private FanCatalystScenes() {}

    public static void catalysts(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("dnd_fan_catalysts", "Bulk Processing with Dreams n' Desires Catalysts");
        FanSceneUtil.showBase(scene, util);

        BlockPos catalystPos = FanSceneUtil.catalystPos(util);
        Vec3 itemVec = FanSceneUtil.itemVec(util);
        ElementLink<WorldSectionElement> catalyst;

        catalyst = FanSceneUtil.placeCatalyst(scene, util, null, Blocks.SAND.defaultBlockState());
        scene.overlay().showOutlineWithText(util.select().fromTo(catalystPos, catalystPos.west(2)), 80)
                .colored(PonderPalette.WHITE)
                .attachKeyFrame()
                .text("Air Flows passing through any Sand create a Sanding setup");
        scene.idle(80);

        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.EXPOSED_COPPER), ParticleTypes.WHITE_ASH);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .text("Sanding wears items down, like scrubbing oxidation off copper");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.COPPER_BLOCK));

        catalyst = FanSceneUtil.placeCatalyst(scene, util, catalyst, Blocks.POWDER_SNOW.defaultBlockState());
        scene.overlay().showOutlineWithText(util.select().fromTo(catalystPos, catalystPos.west(2)), 80)
                .colored(PonderPalette.BLUE)
                .attachKeyFrame()
                .text("Powder Snow creates a Freezing setup");
        scene.idle(80);

        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.PACKED_ICE), ParticleTypes.SNOWFLAKE);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .text("Freezing chills items, like turning Packed Ice into Blue Ice");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.BLUE_ICE));

        BlockState superheated = AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.SEETHING);
        catalyst = FanSceneUtil.placeCatalyst(scene, util, catalyst, superheated);
        scene.overlay().showOutlineWithText(util.select().fromTo(catalystPos, catalystPos.west(2)), 80)
                .colored(PonderPalette.RED)
                .attachKeyFrame()
                .text("A superheated Blaze Burner creates a Seething setup. A normal heat level won't");
        scene.idle(80);

        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.ENDER_PEARL), ParticleTypes.SOUL_FIRE_FLAME);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .text("Seething runs hotter than Blasting, like turning Ender Pearls into Eyes of Ender");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.ENDER_EYE));

        catalyst = FanSceneUtil.placeCatalyst(scene, util, catalyst, Blocks.DRAGON_EGG.defaultBlockState());
        scene.overlay().showOutlineWithText(util.select().fromTo(catalystPos, catalystPos.west(2)), 80)
                .colored(PonderPalette.MEDIUM)
                .attachKeyFrame()
                .text("A Dragon Egg creates a Dragon Breathing setup");
        scene.idle(80);

        FanSceneUtil.throwItem(scene, util, new ItemStack(Items.GLASS_BOTTLE), ParticleTypes.DRAGON_BREATH);
        scene.overlay().showText(70)
                .pointAt(itemVec)
                .placeNearTarget()
                .text("Dragon Breathing infuses items with the End, like filling bottles with Dragon's Breath");
        FanSceneUtil.finishItem(scene, util, new ItemStack(Items.DRAGON_BREATH));

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .text("On an Industrial Fan, a Dragon Head on the fan's front face also works while powered by redstone");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Each type also has a Catalyst Sail that works the same way as its block");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(catalystPos))
                .placeNearTarget()
                .text("Look up Bulk Sanding, Freezing, Seething and Dragon Breathing in JEI to see every recipe");
        scene.idle(100);
    }
}