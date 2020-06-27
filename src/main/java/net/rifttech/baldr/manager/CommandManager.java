package net.rifttech.baldr.manager;

import lombok.Getter;
import net.rifttech.baldr.command.Command;
import net.rifttech.baldr.data.Registerable;
import org.atteo.classindex.ClassIndex;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CommandManager implements Registerable {
    private final List<Command> commands = new ArrayList<>();

    @Override
    public void register() {
        ClassIndex.getSubclasses(Command.class, getClass().getClassLoader())
                .forEach(clazz -> {
                    try {
                        commands.add(clazz.newInstance());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

}
