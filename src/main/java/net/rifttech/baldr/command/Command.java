package net.rifttech.baldr.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.helper.command.context.CommandContext;
import net.rifttech.baldr.Baldr;
import org.atteo.classindex.IndexSubclasses;
import org.bukkit.entity.Player;

@Getter
@IndexSubclasses
@RequiredArgsConstructor
public abstract class Command {
    protected final Baldr plugin = Baldr.getInstance();

    private final String name;
    private final String description;

    public abstract void handle(Player sender, CommandContext<Player> ctx);
}
