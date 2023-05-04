package org.dnsge.fortprac.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import org.dnsge.fortprac.SaveState;

import static net.minecraft.server.command.CommandManager.literal;

public class SaveInventoryCommand implements CommandRegister {
    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("saveInventory").executes(c -> {
            this.execute(c);
            return 1;
        }));
    }

    private void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        ServerPlayerEntity player = c.getSource().getPlayer();
        SaveState.savePlayerState(player);
        c.getSource().sendFeedback(new LiteralText("Saved your inventory & status effects"), false);
    }

}
