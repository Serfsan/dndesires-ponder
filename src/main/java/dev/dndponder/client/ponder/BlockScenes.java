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

import static dev.dndponder.client.ponder.ShowcaseUtil.*;

public final class BlockScenes {
    private BlockScenes() {}

    public static void smartHopper(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("smart_hopper", "Using the Smart Hopper");
        begin(scene, util, 5);

        BlockPos chest = util.grid().at(2, 1, 2);
        BlockPos hopper = util.grid().at(2, 2, 2);
        place(scene, util, chest, mc("chest", "facing", "south"));
        place(scene, util, hopper, dnd("smart_hopper", "facing", "down"));
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(hopper))
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Smart Hopper is a Hopper with a filter");
        scene.idle(90);

        scene.overlay().showControls(util.vector().topOf(hopper), Pointing.DOWN, 40)
                .withItem(DnDIds.stack(ResourceLocation.fromNamespaceAndPath("create", "filter"), 1));
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(hopper))
                .placeNearTarget()
                .text("Only items that match its filter are moved");
        scene.idle(90);
    }

    public static void fluidGaugeAndHatch(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("fluid_gauge_and_hatch", "Fluid Gauges and Fluid Hatches");
        begin(scene, util, 5);

        BlockPos tank = util.grid().at(2, 1, 2);
        BlockPos gauge = util.grid().at(3, 1, 2);
        BlockPos hatch = util.grid().at(1, 1, 2);
        place(scene, util, tank, create("fluid_tank"));
        scene.idle(5);

        place(scene, util, gauge, dnd("fluid_gauge", "facing", "west"));
        scene.overlay().showText(80)
                .pointAt(util.vector().blockSurface(gauge, Direction.EAST))
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Fluid Gauge on the side of a tank shows how much fluid is inside");
        scene.idle(90);

        place(scene, util, hatch, dnd("fluid_hatch", "facing", "east"));
        scene.overlay().showControls(util.vector().blockSurface(hatch, Direction.WEST), Pointing.DOWN, 40)
                .withItem(new ItemStack(Items.WATER_BUCKET));
        scene.overlay().showText(90)
                .pointAt(util.vector().blockSurface(hatch, Direction.WEST))
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Fluid Hatch gives quick access to the tank: take fluid out or put fluid in by hand");
        scene.idle(100);
    }

    public static void rollTable(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("roll_table", "Using the Roll Table");
        scene.configureBasePlate(0, 0, 5);

        BlockPos table = util.grid().at(3, 1, 2);
        BlockPos depot = util.grid().at(4, 1, 2);
        BlockPos beltStart = util.grid().at(1, 1, 2);
        scene.world().setBlock(table, dnd("roll_table"), false);

        scene.showBasePlate();
        scene.idle(5);
        scene.world().showSection(util.select().position(1, 0, 5), Direction.UP);
        scene.world().showSection(util.select().fromTo(2, 1, 3, 2, 1, 5), Direction.NORTH);
        scene.idle(5);
        scene.world().showSection(util.select().fromTo(2, 1, 2, 1, 1, 2), Direction.SOUTH);
        scene.idle(5);
        scene.world().showSection(util.select().position(table), Direction.DOWN);
        scene.idle(15);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(table))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Items on a Roll Table roll across its top, like on an Item Drain. It needs no rotation");
        scene.idle(70);

        ItemStack ingot = new ItemStack(Items.IRON_INGOT);
        scene.world().createItemOnBelt(beltStart, Direction.NORTH, ingot);
        scene.idle(40);
        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(table))
                .placeNearTarget()
                .text("With nothing on the far side, items simply roll off the end");
        scene.idle(90);

        scene.world().setBlock(depot, create("depot"), false);
        scene.world().showSection(util.select().position(depot), Direction.DOWN);
        scene.idle(15);
        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(depot))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Otherwise they pass onto whatever is there: a belt, a depot, a funnel or another Roll Table");
        scene.idle(30);
        scene.world().createItemOnBelt(beltStart, Direction.NORTH, ingot);
        scene.idle(80);

        scene.overlay().showText(100)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(table))
                .placeNearTarget()
                .text("It can't turn items. They always leave on the side opposite where they came in");
        scene.idle(110);
    }

    public static void casings(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("dnd_casings", "Industrial and Overburden Casing");
        begin(scene, util, 5);

        BlockPos industrial = util.grid().at(1, 1, 2);
        BlockPos overburden = util.grid().at(3, 1, 2);
        place(scene, util, industrial, dnd("industrial_casing"));
        place(scene, util, overburden, dnd("overburden_casing"));
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(industrial))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Industrial Casing and Overburden Casing are decoration and crafting blocks");
        scene.idle(90);

        scene.overlay().showText(90)
                .colored(PonderPalette.RED)
                .pointAt(util.vector().topOf(overburden))
                .placeNearTarget()
                .text("They connect like other casings, but can't be used to encase shafts or cogs");
        scene.idle(100);

        scene.overlay().showText(70)
                .pointAt(util.vector().topOf(industrial))
                .placeNearTarget()
                .text("Industrial Casing is used to craft the Industrial Fan");
        scene.idle(80);
    }

    // Asphalt has properties below:
    // speedFactor 0.995, jumpFactor 1.25, friction 0.5 (vanilla default 0.6).
    public static void asphalt(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("asphalt", "Asphalt");
        begin(scene, util, 5);

        String[] colors = {"black", "gray", "white", "yellow", "red"};
        place(scene, util, util.grid().at(2, 1, 2), dnd("asphalt"));
        for (int i = 0; i < colors.length; i++) {
            place(scene, util, util.grid().at(i, 1, 0), dnd(colors[i] + "_asphalt"));
        }
        scene.idle(10);

        scene.overlay().showText(80)
                .pointAt(util.vector().topOf(util.grid().at(2, 1, 2)))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Asphalt comes plain and in 16 colors");
        scene.idle(90);

        scene.overlay().showText(90)
                .colored(PonderPalette.GREEN)
                .pointAt(util.vector().topOf(util.grid().at(2, 1, 2)))
                .placeNearTarget()
                .text("Walking on it is noticeably faster, and you jump higher. There's no potion effect involved");
        scene.idle(100);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(util.grid().at(2, 1, 0)))
                .placeNearTarget()
                .attachKeyFrame()
                .text("Plain Asphalt is made in a heated Mixer. Surround a dye with 8 Asphalt to color it");
        scene.idle(100);
    }

    public static void boreBlocks(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);
        scene.title("bore_blocks", "Bore Blocks");
        begin(scene, util, 5);

        BlockPos bore = util.grid().at(1, 1, 2);
        BlockPos stone = util.grid().at(3, 1, 2);
        place(scene, util, bore, dnd("bore_block"));
        place(scene, util, stone, mc("stone"));
        scene.idle(10);

        scene.overlay().showText(90)
                .pointAt(util.vector().topOf(bore))
                .placeNearTarget()
                .attachKeyFrame()
                .text("On a moving contraption, Bore Blocks break the blocks they're pushed into");
        scene.idle(100);

        scene.world().moveSection(scene.world().makeSectionIndependent(util.select().position(bore)),
                util.vector().of(1, 0, 0), 20);
        scene.idle(20);
        scene.world().destroyBlock(stone);
        scene.idle(20);

        scene.overlay().showText(70)
                .pointAt(util.vector().topOf(util.grid().at(2, 1, 2)))
                .placeNearTarget()
                .text("They come plain and in 16 colors");
        scene.idle(80);
    }
}