package net.rifttech.baldr.listener.impl;

import me.lucko.helper.Commands;
import me.lucko.helper.command.argument.Argument;
import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.listener.Listener;
import net.rifttech.baldr.manager.CommandManager;
import net.rifttech.baldr.util.PermissionNode;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandListener implements Listener {
    private final Baldr plugin = Baldr.getInstance();

    private final CommandManager commandManager = plugin.getCommandManager();

    @Override
    public void register() {
        Commands.create()
                .assertPermission(PermissionNode.MOD)
                .assertPlayer()
                .handler(ctx -> {
                    Player player = ctx.sender();

                    Argument argument = ctx.arg(0);
                    Optional<String> value = argument.value();

                    if (!value.isPresent()) {
                        sendHelpCommands(player);

                        return;
                    }

                    commandManager.getCommands().stream()
                            .filter(command -> command.getName().equalsIgnoreCase(value.get()))
                            .findFirst()
                            .ifPresent(command -> command.handle(player, ctx));
                })
                .register("ac", "anticheat", "baldr");
    }

    private void sendHelpCommands(Player player) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n");

        sb.append("§4[AntiCheat] §cCommands").append("\n");

        commandManager.getCommands().forEach(command -> {
            String message = String.format(" §7- §c/%s §7- %s", command.getName(), command.getDescription());

            sb.append(message).append("\n");
        });

        sb.append("\n");

        player.sendMessage(sb.toString());
    }
}
