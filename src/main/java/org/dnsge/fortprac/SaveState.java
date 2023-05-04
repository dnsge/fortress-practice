package org.dnsge.fortprac;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.io.IOException;

public class SaveState {

    /**
     * Serialize and save player state to the FortressPractice player state
     * file. The file stores inventory state and status effects.
     *
     * @param player Player to save state for
     */
    public static void savePlayerState(ServerPlayerEntity player) {
        try {
            File file = FortressPracticeMod.getPlayerStateFile();
            if (!file.exists()) {
                file.createNewFile();
            }

            CompoundTag tag = SaveState.serializePlayerState(player);
            NbtIo.write(tag, file);
        } catch (IOException e) {
            FortressPracticeMod.LOGGER.error("Failed to save player state to file", e);
            return;
        }

        FortressPracticeMod.LOGGER.info("Saved inventory state to file");
    }

    /**
     * Deserialize and load player state from the FortressPractice player state.
     * Loads inventory state and status effects.
     *
     * @param player Player to load state to
     * @return Whether the save state file was found and was valid
     */
    public static boolean loadPlayerState(ServerPlayerEntity player) {
        try {
            File file = FortressPracticeMod.getPlayerStateFile();
            if (!file.exists()) {
                return false; // file not found
            }

            var tag = NbtIo.read(file);
            if (tag == null) {
                return false;
            }

            SaveState.deserializePlayerState(player, tag);
        } catch (IOException e) {
            FortressPracticeMod.LOGGER.error("Failed to load player state from file", e);
            return false;
        }

        FortressPracticeMod.LOGGER.info("Loaded inventory state from file");
        return true;
    }

    private static CompoundTag serializePlayerState(ServerPlayerEntity player) {
        CompoundTag baseTag = new CompoundTag();

        // Serialize inventory
        ListTag inventoryTag = new ListTag();
        player.inventory.serialize(inventoryTag);
        baseTag.put("Inventory", inventoryTag);

        // Serialize status effects, if any exist
        var statusEffects = player.getActiveStatusEffects();
        if (!statusEffects.isEmpty()) {
            ListTag listTag = new ListTag();
            for (StatusEffectInstance effect : statusEffects.values()) {
                listTag.add(effect.toTag(new CompoundTag()));
            }
            baseTag.put("ActiveEffects", listTag);
        }

        return baseTag;
    }

    public static void deserializePlayerState(ServerPlayerEntity player, CompoundTag tag) {
        player.inventory.deserialize(tag.getList("Inventory", 10));

        // Clear status effects in anticipation of loading any saved effects.
        // If there are no saved effects, this gives the desired result of having
        // no status effects applied.
        player.clearStatusEffects();

        if (tag.contains("ActiveEffects", 9)) {
            ListTag listTag = tag.getList("ActiveEffects", 10);

            for (int i = 0; i < listTag.size(); ++i) {
                CompoundTag compoundTag = listTag.getCompound(i);
                StatusEffectInstance statusEffectInstance = StatusEffectInstance.fromTag(compoundTag);
                if (statusEffectInstance != null) {
                    player.getActiveStatusEffects().put(statusEffectInstance.getEffectType(), statusEffectInstance);
                }
            }
        }
    }
}
