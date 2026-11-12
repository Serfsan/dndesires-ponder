# Changelog

## 1.0.0 — Initial release

Built for **Create: Dreams n' Desires 2.3a-BETA** on NeoForge 1.21.1 (Create 6.0.9+). Unofficial; not affiliated with LopyLuna.

Soft requirement of KubeJS recommended

### Ponder scenes
- **Hydraulic Press**: fluid tank requirement (1000 mB per press, 3000 mB tank, side inputs only), Hydraulic Compacting, bulk pressing, ¼ press speed
- **Encased Fan**: D&D's Sanding, Freezing, Seething and Dragon Breathing processing and their catalysts
- **All 8 Catalyst Sails**: sails as fan catalysts, air pass-through, windmill use
- **Stirling Engine**: Flywheel placement, output side and shaft/cog connection, furnace-type efficiency
- **Industrial Fan**: 2× processing speed, 30-block range, generator mode (64 RPM / 1024 SU)
- **Cog Crank & Large Cog Crank**, **Inverse Gearshift**, **Omni Gearbox**, **Omni Speed Controller**, **Multimeter**, **Golden Mixer**, **Spud Sentry**, **Creative Gear Motor**
- **Smart Hopper**, **Fluid Gauge & Fluid Hatch**, **Roll Table** (belt feed and pass-through)
- **Industrial & Overburden Casing**, **Asphalt** (all colors), **Bore Blocks** (all colors)

### KubeJS
- Added recipe methods for all five D&D recipe types under `event.recipes.dndesires`: `sanding`, `freezing`, `seething`, `dragon_breathing`, `hydraulic_compacting` (requires KubeJS Create; loaded only when it's installed)

### Tweaks (disable per preference in config)
- **Removed:** Handheld Saw, Handheld Drill, Gatling Breaker (recipes, creative, JEI; existing copies are inert)
- **Removed:** Burner Stock and its infinite-fuel smelting loop; Seething Catalyst Sail now uses a Blaze Burner instead
- **Removed:** milkshake + lava ore-stone chance on Bedrock and catalyst blocks, and the milkshake > Asurine / Crimsite / Ochrum / Veridium Hydraulic Compacting recipes
- **Changed:** Chocolate Milkshake uses milk instead of liquid chocolate
- **Changed:** Vanilla Milkshake uses white flowers instead of blue orchids
- **Changed:** Breccia Hydraulic Compacting uses quartz instead of pumpkin milkshake
- **Added:** Pam's HarvestCraft 2 support (optional): Pam's chocolate bar, vanilla bean and strawberries in milkshakes
- **Added:** Mechanical Mixer recipes for all 16 asphalt and 16 bore block colors (any mod's dyes), plain bore blocks, and coal > 8 coal pieces (gravel filter, returned)
- **Added:** Dyeable Industrial Fan blades: right-click with any dye.

### Fixes
- Supplies the 16 colored propeller models D&D registers but doesn't ship, removing its startup model warnings

### Other
- One-time chat warning when the installed D&D version differs from the tested one, with a dismiss button and config option (`/dndponder dismiss`)
- CLIENTSIDE: ponder scenes.
- SERVERSIDE: recipe and gameplay changes. Not required by players who join the server.
