package org.dnsge.fortprac.commands;

import java.util.ArrayList;
import java.util.List;

public class AllCommands {

    public static List<CommandRegister> allCommands() {
        var cmds = new ArrayList<CommandRegister>();
        cmds.add(new FortressCommand());
        cmds.add(new SaveInventoryCommand());
        return cmds;
    }
}
