// Example recipes for Create: Dreams n' Desires using the schemas from "Dreams n' Desires: Ponder & KubeJS".
// Requires KubeJS + KubeJS Create. Copy into kubejs/server_scripts/ and edit to taste.
// Argument order matches Create's processing recipes: OUTPUTS first, then INPUTS.

ServerEvents.recipes(event => {
  const dnd = event.recipes.dndesires

  // --- Bulk Sanding (fan through sand) ---------------------------------------------------
  dnd.sanding('minecraft:granite', 'minecraft:polished_granite')
  dnd.sanding('minecraft:sand', 'minecraft:sandstone')

  // --- Bulk Freezing (fan through powder snow) --------------------------------------------
  dnd.freezing('minecraft:packed_ice', 'minecraft:ice')
  dnd.freezing('minecraft:snow_block', 'minecraft:snowball')

  // --- Bulk Seething (fan through a superheated blaze burner) ----------------------------
  // Chanced outputs use KubeJS Create's CreateItem.of(item, chance)
  dnd.seething(['create:zinc_ingot', CreateItem.of('create:zinc_ingot', 0.5)], 'create:crushed_raw_zinc')

  // --- Bulk Dragon Breathing (fan through a dragon egg) ----------------------------------
  dnd.dragon_breathing(CreateItem.of('minecraft:shulker_shell', 0.1), 'minecraft:popped_chorus_fruit')

  // --- Hydraulic Compacting (Hydraulic Press over a basin) -------------------------------
  // Items and fluids together; .heated() / .superheated() for blaze burner heat.
  dnd.hydraulic_compacting('4x minecraft:tuff', [
    'minecraft:gravel',
    'minecraft:andesite',
    Fluid.of('minecraft:lava', 250)
  ]).superheated()

  // --- Removing / replacing D&D's own recipes ---------------------------------------------
  event.remove({ id: 'dndesires:hydraulic_compacting/cobblestone_gen' })
  event.remove({ type: 'dndesires:freezing', output: 'minecraft:blue_ice' })

  // --- Fluid TAGS or anything the schema doesn't cover: raw JSON always works -----------
  event.custom({
    type: 'dndesires:hydraulic_compacting',
    heat_requirement: 'heated',
    ingredients: [
      { type: 'neoforge:tag', tag: 'c:lava', amount: 100 },
      { type: 'neoforge:tag', tag: 'c:water', amount: 500 }
    ],
    results: [{ id: 'minecraft:stone', count: 8 }]
  })
})
