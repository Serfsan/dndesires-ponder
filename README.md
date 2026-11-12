# Dreams n' Desires: Ponder & KubeJS

Unofficial addon for **Create: Dreams n' Desires** (NeoForge 1.21.1). It adds the in-game Ponder
scenes and KubeJS recipe support that D&D doesn't ship with, and documents its custom recipe types
here so you don't have to go hunting for them.

Not affiliated with or endorsed by LopyLuna. Please don't send issues with this addon to the D&D team.

## Built for

| | Version |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.200+ |
| Create | 6.0.9 – 6.0.x |
| **Create: Dreams n' Desires** | **2.3a-BETA** (other versions load, with a warning) |
| KubeJS + KubeJS Create | optional, needed only for the KubeJS part |

### Version warning

The addon is built against **one** D&D release (`dnd_tested_version` in `gradle.properties`). Any
D&D version still loads, because a hard version lock would break packs every time D&D updates.
When the installed version is different, the player gets **one chat message per game launch**,
about two seconds after joining a world or server, with:

- a link to the D&D version this addon was built for
- a link to this addon's page (if `addon_page_url` is set)
- **[Don't warn me for &lt;version&gt;]**, which stays quiet until D&D changes version again

Commands (client-side, work on any server):

- `/dndponder status`: show tested vs. installed version
- `/dndponder dismiss`: same as the dismiss button

Settings are in `config/dndesires_ponder-client.toml`, or in game from Mods → this addon → Config.
The mismatch is also logged to `latest.log` on both client and server.

If `update_json_url` is set, NeoForge's update checker also runs, and the same chat message says
when a newer build of this addon is out.

## Gameplay tweaks

All on by default. Each one can be turned off in `config/dndesires_ponder-startup.toml`. The file is
read **once at startup**, so restart after changing it. Each tweak is a built-in datapack that's only
loaded when its switch is on, and it sits above mod data so it reliably overrides D&D's own recipes.

| Switch | What it does |
|---|---|
| `disableHandheldTools` | Removes the **Handheld Saw**, **Handheld Drill** and **Gatling Breaker**: recipes removed, hidden from creative and JEI, and copies already in a world can't mine, attack or be used |
| `disableBurnerStock` | Removes **Burner Stock** and its smelting/blasting/smoking recipes, which turn it back into itself so a furnace never stops "smelting" for the Stirling Engine. It's hidden from creative and JEI. The Seething Catalyst Sail now uses a Blaze Burner in its place |
| `milkshakeRecipes` | **Chocolate:** milk instead of liquid chocolate, and accepts Create's or Pam's chocolate bar. **Vanilla:** a dandelion or any white flower instead of a blue orchid, or Pam's vanilla bean when Pam's Trees is installed. **Strawberry:** Pam's strawberries instead of sweet berries when Pam's Crops is installed |
| `brecciaQuartz` | Breccia (Hydraulic Compacting) uses quartz (`#c:gems/quartz`) instead of pumpkin milkshake |
| `removeMilkshakeOreStone` | Milkshake + lava only makes its normal stone: no ore-stone chance on Bedrock, Netherite Blocks, Beacons, Dragon Eggs, Reinforced Deepslate or End Portals. Also removes the Hydraulic Compacting recipes that turn milkshake into Asurine, Crimsite, Ochrum and Veridium |
| `dyeableIndustrialFan` | Right-click an Industrial Fan with any dye to color its blades. D&D registered models for this but never built it. Only turns on when the installed D&D is the tested version, unless `dyeableFanOnAnyDnDVersion=true`, because it replaces D&D's fan renderer. Players without the addon see plain blades |
| `mixerRecipes` | Mechanical Mixer recipes for all 16 asphalt colors and all 16 bore block colors (8 blocks + 1 dye), plain bore blocks, and 1 coal → 8 coal pieces with 1 gravel as a filter (the gravel comes back out) |

Pam's HarvestCraft is **never required**. The Pam's recipes use `neoforge:item_exists` conditions
(`pamhc2foodcore:chocolatebaritem`, `pamhc2trees:vanillabeanitem`, `pamhc2crops:strawberryitem`),
so without Pam's they don't load and the default recipe is used instead.

Dyeing uses `c:dyes/<color>` tags, so any mod's dyes work, the same as D&D's own crafting recipes.

## Ponder scenes

Hold **W** over any of these in JEI or your inventory:

| Block / item | Scene covers |
|---|---|
| Hydraulic Press | Fluid requirement, Hydraulic Compacting, how it differs from a Mechanical Press |
| Encased Fan | D&D's Sanding, Freezing, Seething and Dragon Breathing processing |
| All 8 Catalyst Sails | Sails as fan catalysts, air passing through, windmill use |
| Stirling Engine | Furnace + Flywheel setup, only runs while smelting, furnace-type efficiency |
| Industrial Fan | 2x processing speed, 30-block range, generator mode (64 RPM / 1024 SU) |
| Cog Crank, Large Cog Crank | Meshing with cogs, 256 SU vs 128 SU |
| Inverse Gearshift | Always-reversed output, no redstone |
| Omni Gearbox | Output on all six faces |
| Omni Speed Controller | Any output speed in one block |
| Multimeter | Speed and stress in one gauge |
| Golden Mixer | 2.5x speed, 8 SU/RPM, built-in cog |
| Spud Sentry | Ammo, target priority, stress cost |
| Creative Gear Motor | Testing power source |
| Smart Hopper | Filtered hopper |
| Fluid Gauge, Fluid Hatch | Reading and hand-accessing tanks |
| Roll Table | Moving items without rotation |
| Industrial / Overburden Casing | Decoration only, can't encase |
| Asphalt (all colors) | Faster walking, higher jumps, how to make and dye it |
| Bore Blocks (all colors) | Breaking blocks on contraptions |

Things the Hydraulic Press scene covers that aren't written down anywhere else (from D&D 2.3a-BETA source):

- 3000 mB internal tank, filled by pipe from the **two sides without a shaft** (not top or bottom)
- Won't start below 1000 mB, and **every press stroke uses 1000 mB**. Gases don't count.
- In a basin it **only** does `dndesires:hydraulic_compacting`, never normal Create compacting
- On belts, depots and the ground it does normal `create:pressing` recipes
- Presses at about **¼ the speed** of a Mechanical Press at the same RPM

## KubeJS

Requires **KubeJS** and **KubeJS Create**. The addon ships recipe schemas, so these become typed
methods under `event.recipes.dndesires`:

| Recipe type | Machine / catalyst | Notes |
|---|---|---|
| `dndesires:sanding` | Fan through any `#minecraft:sand`, or Sanding Catalyst Sail | |
| `dndesires:freezing` | Fan through powder snow, or Freezing Catalyst Sail | |
| `dndesires:seething` | Fan through a **superheated** Blaze Burner, or Seething Catalyst Sail | |
| `dndesires:dragon_breathing` | Fan through a dragon egg, or Dragon Breathing Catalyst Sail. Also a redstone-powered dragon head on an **Industrial Fan**'s front face | |
| `dndesires:hydraulic_compacting` | Hydraulic Press over a basin | items + fluids; `.heated()` / `.superheated()` |

All five use Create's standard processing format, so the arguments work like Create's own
`splashing` / `compacting`: **outputs first, then inputs**.

```js
ServerEvents.recipes(event => {
  const dnd = event.recipes.dndesires

  dnd.sanding('minecraft:granite', 'minecraft:polished_granite')
  dnd.freezing('minecraft:packed_ice', 'minecraft:ice')
  dnd.seething(['minecraft:iron_ingot', CreateItem.of('minecraft:iron_ingot', 0.75)], 'create:crushed_raw_iron')
  dnd.dragon_breathing('minecraft:end_stone', 'minecraft:deepslate')

  dnd.hydraulic_compacting('2x minecraft:calcite', ['minecraft:bone_block', 'minecraft:diorite']).heated()
  dnd.hydraulic_compacting('8x minecraft:cobblestone', [Fluid.of('minecraft:lava', 100), Fluid.of('minecraft:water', 500)])

  // Removing works by type or by ID
  event.remove({ type: 'dndesires:sanding', output: 'minecraft:andesite' })
  event.remove({ id: 'dndesires:hydraulic_compacting/stone_gen' })
})
```

More in `examples/kubejs/server_scripts/dndesires_examples.js`.

**No KubeJS Create, or need a fluid tag?** Raw JSON works with or without this addon:

```js
event.custom({
  type: 'dndesires:hydraulic_compacting',
  heat_requirement: 'superheated',          // none | heated | superheated
  ingredients: [
    { item: 'minecraft:sandstone' },
    { type: 'neoforge:tag', tag: 'c:lava', amount: 500 }
  ],
  results: [{ id: 'create:asurine', count: 2 }, { id: 'minecraft:sand', chance: 0.5 }]
})
```

D&D's built-in recipes, useful as templates, are in its source at
`src/generated/resources/data/dndesires/recipe/<type>/`.

## Building

JDK 21. From the project folder:

```powershell
.\gradlew.bat build          # jar ends up in build\libs\
```

### Testing in a dev client

D&D isn't a compile dependency (see Design notes), so drop the D&D jar you're testing against into
`run\mods\`, then:

```powershell
.\gradlew.bat runClient
```

After editing any scene text, regenerate the lang file (needs the D&D jar in `run\mods\` too):

```powershell
.\gradlew.bat runData
```

## Updating for a new D&D release

1. Put the new D&D jar in `run\mods\`.
2. Set `dnd_tested_version` in `gradle.properties` to the version string shown on the Mods screen.
3. Check the IDs in `DnDIds.java` still exist. The log will say `... not found ..., skipping its Ponder scene` if one doesn't.
4. Re-check the behaviour notes at the top of `HydraulicPressScenes.java` and `FanCatalystScenes.java`
   against the new D&D source (`HydraulicPressBE`, `content/fan_types/*Type.java`, block tags).
5. `runClient`, ponder each block, then `runData` if any text changed.
6. Bump `mod_version`, build, release.

## Design notes

- **No compile dependency on D&D.** Every D&D block and item is looked up by registry ID at runtime.
  If one disappears, that scene is skipped with a log line, and the game keeps running.
- **Driven only through Create's public API.** The press animation and tank use Create's
  `PressingBehaviour` and `SmartFluidTankBehaviour`, which D&D's press is built on, rather than D&D classes.
- **No bundled schematics.** Scenes reuse Create's own `mechanical_press/compacting` and
  `fan/processing` layouts with D&D blocks swapped in.
- **No D&D assets** are included or redistributed.
- **No network payloads or registry content**, so the addon is safe client-only, server-only, or both.

## License

MIT
