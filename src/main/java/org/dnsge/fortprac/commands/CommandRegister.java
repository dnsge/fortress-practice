package org.dnsge.fortprac.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegister {
    void register(CommandDispatcher<ServerCommandSource> dispatcher);
}
