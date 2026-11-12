package dev.dndponder.client.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import static dev.dndponder.client.ponder.ShowcaseUtil.*;


public final class MachineScenes {
    private MachineScenes() {}

    public static void stirlingEngine(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("stirling_engine", "Generating Power with the Stirling Engine");
        begin(scene, util, 5);

        BlockPos furnace = util.grid().at(0, 2, 2);
        BlockPos engine = util.grid().at(1, 2, 2);
        BlockPos flywheel = util.grid().at(3, 2, 2);
        BlockPos outputSide = util.grid().at(3, 2, 1);
        BlockPos outputFar = util.grid().at(3, 2, 0);

        place(scene, util, util.grid().at(0, 1, 2), mc("cobblestone"));
        place(scene, util, furnace, mc("furnace", "facing", "south", "lit", "true"));
        scene.idle(10);
        place(scene, util, engine, dnd("stirling_engine", "face", "wall", "facing", "east"));
        scene.idle(10);
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(engine))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Stirling Engine attaches to the side of a furnace and turns its heat into rotation");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(engine), Pointing.DOWN, 40)
                .rightClick()
                .withItem(DnDIds.stack(ResourceLocation.fromNamespaceAndPath("create", "flywheel"), 1));
        scene.idle(20);
        place(scene, util, flywheel, dnd("powered_flywheel", "facing", "south", "axis", "z"));
        spin(scene, util, flywheel, 32);
        scene.overlay().showText(90)
                .pointAt(util.vector().centerOf(flywheel))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Right-click the engine with a Flywheel and it snaps into place in front of it");
        scene.idle(100);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().centerOf(flywheel))
                .placeNearTarget()
                .text("Only one side of the Flywheel outputs rotation. Sneak while placing it to flip which side");
        scene.idle(100);

        scene.rotateCameraY(180);
        scene.idle(30);

        place(scene, util, outputSide, create("cogwheel", "axis", "z"));
        spin(scene, util, outputSide, 32);
        scene.overlay().showText(80)
                .pointAt(util.vector().centerOf(outputSide))
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Cogwheel connects straight to the output side");
        scene.idle(90);

        scene.world().hideSection(util.select().position(outputSide), Direction.UP);
        scene.idle(15);
        place(scene, util, outputSide, create("shaft", "axis", "z"));
        place(scene, util, outputFar, create("shaft", "axis", "z"));
        scene.world().setKineticSpeed(util.select().fromTo(outputSide, outputFar), 32);
        scene.overlay().showText(80)
                .pointAt(util.vector().centerOf(outputFar))
                .placeNearTarget()
                .text("So does a Shaft, to carry the power away");
        scene.idle(90);

        scene.rotateCameraY(180);
        scene.idle(30);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().blockSurface(furnace, Direction.NORTH))
                .placeNearTarget()
                .attachKeyFrame()
                .text("It only runs while the furnace is actually smelting something. Fuel alone isn't enough");
        scene.idle(100);

        scene.overlay().showText(100)
                .pointAt(util.vector().blockSurface(furnace, Direction.NORTH))
                .placeNearTarget()
                .text("Output depends on the furnace: a Furnace runs at 25%%, a Smoker at 50%% and a Blast Furnace at 75%%");
        scene.idle(110);
    }

    public static void industrialFan(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("industrial_fan", "Using the Industrial Fan");
        begin(scene, util, 5);

        BlockPos fan = util.grid().at(3, 1, 2);
        BlockPos cog = util.grid().at(3, 1, 3);
        place(scene, util, fan, dnd("industrial_fan", "facing", "west"));
        place(scene, util, cog, create("cogwheel", "axis", "x"));
        spin(scene, util, fan, 64);
        spin(scene, util, cog, -64);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(fan))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Industrial Fan is a stronger Encased Fan, driven through the cog on its side");
        scene.idle(90);

        scene.overlay().showText(90)
                .pointAt(util.vector().blockSurface(fan, Direction.WEST))
                .placeNearTarget()
                .text("It processes items twice as fast as an Encased Fan, and its air reaches up to 30 blocks");
        scene.idle(100);

        scene.overlay().showText(80)
                .pointAt(util.vector().blockSurface(fan, Direction.WEST))
                .placeNearTarget()
                .text("Every fan processing type works with it, including Dreams n' Desires' own");
        scene.idle(90);

        scene.world().hideSection(util.select().position(fan).add(util.select().position(cog)), Direction.UP);
        scene.idle(15);

        BlockPos lava = util.grid().at(2, 1, 2);
        BlockPos power = util.grid().at(1, 1, 2);
        BlockPos genFan = util.grid().at(2, 2, 2);
        place(scene, util, lava, mc("lava"));
        place(scene, util, power, mc("redstone_block"));
        place(scene, util, genFan, dnd("industrial_fan", "facing", "down"));
        spin(scene, util, genFan, 64);
        scene.idle(10);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(genFan))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Facing down at a heat source while powered by redstone, it becomes a generator");
        scene.idle(100);

        scene.overlay().showText(90)
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(genFan))
                .placeNearTarget()
                .text("It then outputs 64 RPM with 1024 SU. The direction can't be changed");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().centerOf(lava))
                .placeNearTarget()
                .text("Lava, a lit Blaze Burner or a Seething Catalyst Sail all count as heat");
        scene.idle(100);
    }

        //dyeable fans, this will probably be removed next update
    public static void industrialFanDyeing(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("industrial_fan_dyeing", "Dyeing the Industrial Fan");
        begin(scene, util, 5);

        BlockPos fan = util.grid().at(2, 1, 2);
        place(scene, util, fan, dnd("industrial_fan", "facing", "north"));
        spin(scene, util, fan, 32);
        scene.idle(10);

        scene.overlay().showControls(util.vector().topOf(fan), Pointing.DOWN, 40)
                .rightClick()
                .withItem(new ItemStack(Items.RED_DYE));
        scene.idle(20);
        scene.world().modifyBlockEntity(fan, net.minecraft.world.level.block.entity.BlockEntity.class,
                be -> be.setData(dev.dndponder.tweaks.DyeableFan.COLOR, net.minecraft.world.item.DyeColor.RED.getId()));
        scene.overlay().showText(80)
                .pointAt(util.vector().blockSurface(fan, Direction.NORTH))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Right-click an Industrial Fan with any dye to color its blades");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(fan), Pointing.DOWN, 40)
                .rightClick()
                .withItem(new ItemStack(Items.LIGHT_BLUE_DYE));
        scene.idle(20);
        scene.world().modifyBlockEntity(fan, net.minecraft.world.level.block.entity.BlockEntity.class,
                be -> be.setData(dev.dndponder.tweaks.DyeableFan.COLOR, net.minecraft.world.item.DyeColor.LIGHT_BLUE.getId()));
        scene.overlay().showText(70)
                .pointAt(util.vector().blockSurface(fan, Direction.NORTH))
                .placeNearTarget()
                .text("Use another dye to change the color at any time");
        scene.idle(80);
    }

        // cranking it
    public static void cogCranks(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("cog_cranks", "Using Cog Cranks");
        begin(scene, util, 5);

        BlockPos crank = util.grid().at(1, 1, 2);
        BlockPos cog = util.grid().at(2, 1, 2);
        place(scene, util, crank, dnd("cog_crank", "axis", "y"));
        place(scene, util, cog, create("cogwheel", "axis", "y"));

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(crank))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Cog Cranks are Hand Cranks shaped like cogwheels, for early power in tight spots");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(crank), Pointing.DOWN, 40).rightClick();
        spin(scene, util, crank, 32);
        spin(scene, util, cog, -32);
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(cog))
                .placeNearTarget()
                .text("Turn one by hand and it drives the cogwheels it meshes with");
        scene.idle(90);

        scene.overlay().showText(80)
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(crank))
                .placeNearTarget()
                .text("A Cog Crank gives 32 RPM with 256 SU");
        scene.idle(90);

        spin(scene, util, crank, 0);
        spin(scene, util, cog, 0);
        BlockPos large = util.grid().at(3, 1, 3);
        place(scene, util, large, dnd("large_cog_crank", "axis", "y"));
        spin(scene, util, large, 16);
        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(large))
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Large Cog Crank gives 16 RPM with 128 SU, half as much");
        scene.idle(100);
    }

    // tbh I don't think I'd use this block
    public static void inverseGearshift(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("inverse_gearshift", "Using the Inverse Gearshift");
        begin(scene, util, 5);

        BlockPos motor = util.grid().at(0, 1, 2);
        BlockPos block = util.grid().at(2, 1, 2);
        place(scene, util, motor, create("creative_motor", "facing", "east"));
        place(scene, util, util.grid().at(1, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, block, dnd("inverse_gearshift", "axis", "x"));
        place(scene, util, util.grid().at(3, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, util.grid().at(4, 1, 2), create("shaft", "axis", "x"));
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 2, 2, 1, 2), 32);
        scene.world().setKineticSpeed(util.select().fromTo(3, 1, 2, 4, 1, 2), -32);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(block))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Inverse Gearshift always reverses rotation, with no redstone needed");
        scene.idle(90);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(block))
                .placeNearTarget()
                .text("Use it instead of a Gearshift that would only ever sit powered. It has no stress cost");
        scene.idle(90);
    }

        // bs cube
    public static void omniGearbox(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("omni_gearbox", "Using the Omni Gearbox");
        begin(scene, util, 5);

        BlockPos box = util.grid().at(2, 1, 2);
        place(scene, util, util.grid().at(0, 1, 2), create("creative_motor", "facing", "east"));
        place(scene, util, util.grid().at(1, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, box, dnd("omni_gearbox",
                "west_shaft", "true", "east_shaft", "true", "north_shaft", "true",
                "south_shaft", "true", "top_shaft", "true"));
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 2, 2, 1, 2), 32);
        scene.idle(10);

        place(scene, util, util.grid().at(3, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, util.grid().at(2, 1, 1), create("shaft", "axis", "z"));
        place(scene, util, util.grid().at(2, 1, 3), create("shaft", "axis", "z"));
        place(scene, util, util.grid().at(2, 2, 2), create("shaft", "axis", "y"));
        spin(scene, util, util.grid().at(3, 1, 2), 32);
        spin(scene, util, util.grid().at(2, 1, 1), -32);
        spin(scene, util, util.grid().at(2, 1, 3), 32);
        spin(scene, util, util.grid().at(2, 2, 2), -32);
        scene.idle(10);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(box))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Omni Gearbox passes rotation to all six of its faces, including up and down");
        scene.idle(100);

        scene.overlay().showText(70)
                .pointAt(util.vector().topOf(box))
                .placeNearTarget()
                .text("It has no stress cost");
        scene.idle(80);
    }

        // bs^2 cube
    public static void omniSpeedController(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("omni_speed_controller", "Using the Omni Speed Controller");
        begin(scene, util, 5);

        BlockPos block = util.grid().at(2, 1, 2);
        place(scene, util, util.grid().at(0, 1, 2), create("creative_motor", "facing", "east"));
        place(scene, util, util.grid().at(1, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, block, dnd("omni_speed_controller", "axis", "x"));
        place(scene, util, util.grid().at(3, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, util.grid().at(4, 1, 2), create("shaft", "axis", "x"));
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 2, 4, 1, 2), 16);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(block))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Omni Speed Controller sets its output to any speed you choose, in a single block");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(block), Pointing.DOWN, 40).scroll();
        scene.idle(20);
        scene.world().setKineticSpeed(util.select().fromTo(3, 1, 2, 4, 1, 2), 96);
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(block))
                .placeNearTarget()
                .text("Scroll on it to pick the speed, forward or reverse. It has no stress cost");
        scene.idle(90);
    }

        // good addition
    public static void multimeter(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("multimeter", "Using the Multimeter");
        begin(scene, util, 5);

        BlockPos meter = util.grid().at(2, 1, 2);
        place(scene, util, util.grid().at(0, 1, 2), create("creative_motor", "facing", "east"));
        place(scene, util, util.grid().at(1, 1, 2), create("shaft", "axis", "x"));
        place(scene, util, meter, dnd("multimeter", "facing", "up", "axis_along_first", "true"));
        place(scene, util, util.grid().at(3, 1, 2), create("shaft", "axis", "x"));
        scene.world().setKineticSpeed(util.select().fromTo(0, 1, 2, 3, 1, 2), 32);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(meter))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Multimeter shows speed and stress together, like a Speedometer and Stressometer in one");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(meter), Pointing.DOWN, 40)
                .withItem(DnDIds.stack(ResourceLocation.fromNamespaceAndPath("create", "goggles"), 1));
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(meter))
                .placeNearTarget()
                .text("Look at it while wearing Engineer's Goggles to read both. It has no stress cost");
        scene.idle(90);
    }

        // 이 아이템이 존재한다는 건, 모더의 주방이 호화롭거나 인내심이 부족하다는 뜻이다.
    public static void goldenMixer(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("golden_mixer", "Using the Golden Mixer");
        begin(scene, util, 5);

        BlockPos basin = util.grid().at(2, 1, 2);
        BlockPos mixer = util.grid().at(2, 3, 2);
        BlockPos cog = util.grid().at(3, 3, 2);
        place(scene, util, basin, create("basin"));
        place(scene, util, mixer, dnd("gold_mixer"));
        place(scene, util, cog, create("cogwheel", "axis", "y"));
        spin(scene, util, mixer, 32);
        spin(scene, util, cog, -32);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().blockSurface(mixer, Direction.WEST))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Golden Mixer is a faster Mechanical Mixer. Place it two blocks above a Basin");
        scene.idle(90);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(cog))
                .placeNearTarget()
                .text("It has a cogwheel built in, so it's driven by cogs beside it rather than a shaft");
        scene.idle(100);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().blockSurface(mixer, Direction.WEST))
                .placeNearTarget()
                .text("It mixes 2.5 times faster at the same RPM, but costs 8 SU per RPM instead of 4");
        scene.idle(100);
    }

    public static void spudSentry(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("spud_sentry", "Using the Spud Sentry");
        begin(scene, util, 5);

        BlockPos sentry = util.grid().at(2, 1, 2);
        BlockPos cog = util.grid().at(3, 1, 2);
        place(scene, util, sentry, dnd("spud_sentry"));
        place(scene, util, cog, create("cogwheel", "axis", "y"));
        spin(scene, util, sentry, 32);
        spin(scene, util, cog, -32);
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(sentry))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Spud Sentry is an automatic Potato Cannon turret, driven by cogs beside it");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(sentry), Pointing.DOWN, 40).withItem(new ItemStack(Items.POTATO));
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(sentry))
                .placeNearTarget()
                .text("Feed it Potato Cannon ammo and it fires at targets in range");
        scene.idle(90);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(sentry))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Choose its target priority: closest, farthest, least health, most health or random");
        scene.idle(100);

        scene.overlay().showText(70)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(sentry))
                .placeNearTarget()
                .text("It costs 16 SU per RPM");
        scene.idle(80);
    }

    public static void creativeGearMotor(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("creative_gear_motor", "The Creative Gear Motor");
        begin(scene, util, 5);

        BlockPos motor = util.grid().at(2, 1, 2);
        place(scene, util, motor, dnd("creative_gear_motor", "facing", "up"));
        spin(scene, util, motor, 256);
        scene.idle(10);

        Vec3 top = util.vector().topOf(motor);
        scene.overlay().showText(90)
                .pointAt(top)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Creative Gear Motor is a creative-only power source for testing builds");
        scene.idle(100);

        scene.overlay().showText(80)
                .colored(PonderPalette.GREEN)
                .pointAt(top)
                .placeNearTarget()
                .text("It runs at 256 RPM with practically unlimited stress capacity");
        scene.idle(90);
    }
}
