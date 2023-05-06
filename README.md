# MCSR Fortress Practice Mod

This mod assists with practicing Nether Fortress splits and endgame splits. It lets you:

1. Teleport to the nearest fortress
2. Customize your starting inventory and status effects

## Installation

1. If using MultiMC or some similar tool, make a copy of your desired instance
2. Download the Assets.zip file from the latest release on GitHub
3. Extract the zip file and copy the mod `.jar` file to your mods folder (not the sources)
4. Delete Atum if installed, as this mod is incompatible with it

## Setup

1. Launch the game
2. Click the blaze rod next the play button
3. Enter creative mode and set up your inventory (see [inventory tips](#inventory-tips))
4. Run `/saveInventory`
5. Close the world. You'll notice that you are placed into a new world automatically.

## Usage

1. Launch the game
2. Click the blaze rod next the play button
3. Run `/fortress` to start the fortress. Your inventory will be filled automatically.
4. Do as much of the run as you want. This may be blind + measurement, blind + nav to stronghold, or completing the run
   entirely.
5. When you are done practicing, open the "More Options" menu and click "Stop Practice" in the bottom left.

## Inventory Tips

I recommend setting up your inventory as if you just left the bastion and are arriving at the fortress. You should
therefore probably have:

1. Tools
2. Blocks
3. Food
4. At least 16 pearls
5. 20 obsidian (there is no point in practicing 10 because most blinds will be bad)
6. String/Wool/Beds
7. Fire resistance potions

When running `/saveInventory`, your inventory state *and* your status effects are saved. Because you will usually have
used a fire resistance potion before leaving the bastion, **I recommend applying fire resistance before saving the
inventory**.

## How It Works & Potential Problems

To locate the fortress, this mod does the equivalent of running the `/locate` command. Then, the mod finds the first air
block at the reported coordinate and teleports you to that location.

As you may know, the `/locate` command reports coordinates that may not actually reveal the fortress. Additionally, the
first air block may be in a random cave or may not exist at all. In any case, **there is no point in playing out a seed
where you have to mine to the fortress**: locating a fortress in a real run is always by visual indication of the
structure. Just reset for a better seed.
