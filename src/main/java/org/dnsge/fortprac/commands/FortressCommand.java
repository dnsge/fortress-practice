package org.dnsge.fortprac.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import org.dnsge.fortprac.SaveState;

import static net.minecraft.server.command.CommandManager.literal;

public class FortressCommand implements CommandRegister {

    @Override
    public void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("fortress").executes(c -> {
            this.execute(c);
            return 1;
        }));
    }

    private void execute(CommandContext<ServerCommandSource> c) throws CommandSyntaxException {
        ServerPlayerEntity player = c.getSource().getPlayer();
        ServerWorld nether = c.getSource().getMinecraftServer().getWorld(World.NETHER);
        assert nether != null;

        BlockPos fortressLocation = nether.locateStructure(StructureFeature.FORTRESS, BlockPos.ORIGIN, 64, false);
        if (fortressLocation == null) {
            c.getSource().sendError(new LiteralText("Could not locate a fortress within 64 chunks, try another world"));
            return;
        }

        var spawnPoint = fortressLocation.mutableCopy();
        while (!nether.isAir(spawnPoint)) {
            spawnPoint.move(Direction.UP, 1);
            if (spawnPoint.getY() > 120) {
                c.getSource().sendError(new LiteralText("Could not locate an okay spawn point for the fortress"));
                spawnPoint.setY(30);
                break;
            }
        }

        BlockState glassState = Blocks.GLASS.getDefaultState();
        BlockState airState = Blocks.AIR.getDefaultState();

        for (int xOffset = 0; xOffset < 3; ++xOffset) {
            for (int zOffset = 0; zOffset < 3; ++zOffset) {
                for (int yOffset = 0; yOffset < 4; ++yOffset) {
                    BlockPos setPoint = spawnPoint.add(xOffset - 1, yOffset, zOffset - 1);
                    if (yOffset == 0) {
                        nether.setBlockState(setPoint, glassState);
                    } else {
                        nether.setBlockState(setPoint, airState);
                    }
                }
            }
        }

        player.teleport(nether, spawnPoint.getX() + 0.5, spawnPoint.getY() + 1, spawnPoint.getZ() + 0.5, 0, 0);
        SaveState.loadPlayerState(player);
        c.getSource().sendFeedback(new LiteralText("Teleported you to the nearest fortress"), false);
    }
}
