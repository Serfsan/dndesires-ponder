package dev.dndponder.client.ponder;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.math.Pointing;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidStack;

public final class HydraulicPressScenes {

    private HydraulicPressScenes() {}

    public static void basics(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("hydraulic_press", "Using the Hydraulic Press");
        scene.configureBasePlate(0, 0, 5);

        BlockPos pressPos = util.grid().at(1, 4, 2);
        BlockPos basin = util.grid().at(1, 2, 2);
        BlockPos burner = util.grid().at(1, 1, 2);
        BlockState hydraulicPress = DnDIds.state(DnDIds.HYDRAULIC_PRESS);

        scene.world().modifyBlock(pressPos,
                old -> DnDIds.copyProperty(old, hydraulicPress, BlockStateProperties.HORIZONTAL_FACING), false);
        scene.world().multiplyKineticSpeed(util.select().everywhere(), 4);
        scene.world().setKineticSpeed(util.select().position(pressPos), -128);
        scene.world().setBlock(burner, AllBlocks.ANDESITE_CASING.getDefaultState(), false);

        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(1, 4, 3, 1, 1, 5), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(burner), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(basin), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(pressPos), Direction.SOUTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(3, 1, 1, 1, 1, 1), Direction.SOUTH);
        scene.world().showSection(util.select().fromTo(3, 1, 5, 3, 1, 2), Direction.SOUTH);
        scene.idle(20);

        Vec3 pressSide = util.vector().blockSurface(pressPos, Direction.WEST);
        Vec3 basinSide = util.vector().blockSurface(basin, Direction.WEST);

        scene.overlay().showText(70)
                .pointAt(pressSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Hydraulic Press is a Mechanical Press that needs fluid to run");
        scene.idle(80);

        scene.overlay().showControls(pressSide, Pointing.RIGHT, 40).withItem(new ItemStack(Items.WATER_BUCKET));
        scene.idle(10);
        setTank(scene, pressPos, 3000);
        scene.overlay().showText(90)
                .pointAt(pressSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Pipe any liquid into either side without a shaft. Its tank holds 3000mB");
        scene.idle(100);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(pressSide)
                .placeNearTarget()
                .text("Every press uses 1000mB, and it won't start with less than 1000mB in the tank. Gases don't work");
        scene.idle(100);

        scene.world().setBlock(burner, AllBlocks.BLAZE_BURNER.getDefaultState()
                .setValue(BlazeBurnerBlock.HEAT_LEVEL, HeatLevel.KINDLED), true);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(basinSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Pressing a Basin does Hydraulic Compacting, which has its own recipes");
        scene.idle(60);

        ItemStack boneBlock = new ItemStack(Items.BONE_BLOCK);
        ItemStack diorite = new ItemStack(Items.DIORITE);
        ItemStack calcite = new ItemStack(Items.CALCITE, 2);

        scene.overlay().showControls(util.vector().topOf(basin), Pointing.DOWN, 20).withItem(boneBlock);
        scene.idle(25);
        scene.overlay().showControls(util.vector().topOf(basin), Pointing.DOWN, 20).withItem(diorite);
        scene.idle(25);

        startBasinPress(scene, pressPos);
        scene.idle(30);
        compactingParticles(scene, util, pressPos, basin, diorite);
        showInBasin(scene, util, basin, calcite);
        setTank(scene, pressPos, 2000);
        scene.idle(4);
        scene.world().createItemOnBelt(util.grid().at(1, 1, 1), Direction.UP, calcite);
        scene.idle(30);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(basinSide)
                .placeNearTarget()
                .text("It does not do normal Compacting recipes. Keep a Mechanical Press for those");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(pressSide)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Like a Mechanical Press, it also presses items on belts, depots and the ground");
        scene.idle(100);

        scene.overlay().showText(100)
                .pointAt(pressSide)
                .placeNearTarget()
                .text("It presses at about a quarter of a Mechanical Press's speed at the same RPM");
        scene.idle(110);

        scene.overlay().showText(80)
                .pointAt(basinSide)
                .placeNearTarget()
                .text("Look up Hydraulic Compacting in JEI to see every recipe");
        scene.idle(90);
    }

    private static void setTank(CreateSceneBuilder scene, BlockPos pressPos, int amount) {
        scene.world().modifyBlockEntity(pressPos, SmartBlockEntity.class, be -> {
            SmartFluidTankBehaviour tank = be.getBehaviour(SmartFluidTankBehaviour.TYPE);
            if (tank != null) tank.getPrimaryHandler().setFluid(new FluidStack(Fluids.WATER, amount));
        });
    }

    private static void startBasinPress(CreateSceneBuilder scene, BlockPos pressPos) {
        scene.world().modifyBlockEntity(pressPos, SmartBlockEntity.class, be -> {
            if (be.getBehaviour(BeltProcessingBehaviour.TYPE) instanceof PressingBehaviour pressing)
                pressing.start(PressingBehaviour.Mode.BASIN);
        });
    }

    private static void compactingParticles(CreateSceneBuilder scene, SceneBuildingUtil util,
                                            BlockPos pressPos, BlockPos basin, ItemStack stack) {
        scene.world().modifyBlockEntity(pressPos, SmartBlockEntity.class, be -> {
            if (be.getBehaviour(BeltProcessingBehaviour.TYPE) instanceof PressingBehaviour pressing)
                pressing.makeCompactingParticleEffect(util.vector().centerOf(basin), stack);
        });
    }

    private static void showInBasin(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos basin, ItemStack stack) {
        scene.world().modifyBlockEntityNBT(util.select().position(basin), BasinBlockEntity.class, nbt ->
                nbt.put("VisualizedItems", NBTHelper.writeCompoundList(ImmutableList.of(IntAttached.with(1, stack)),
                        ia -> (CompoundTag) ia.getValue().saveOptional(scene.world().getHolderLookupProvider()))));
    }
}
