package net.rifttech.baldr;

import lombok.Getter;
import me.lucko.helper.Schedulers;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.plugin.ap.Plugin;
import me.lucko.helper.scheduler.HelperExecutors;
import net.rifttech.baldr.data.Registerable;
import net.rifttech.baldr.listener.Listener;
import net.rifttech.baldr.manager.AlertManager;
import net.rifttech.baldr.manager.CheckManager;
import net.rifttech.baldr.manager.CommandManager;
import net.rifttech.baldr.manager.PlayerDataManager;
import org.atteo.classindex.ClassIndex;

import java.util.Arrays;
import java.util.Optional;

@Getter
@Plugin(name = "Baldr")
public class Baldr extends ExtendedJavaPlugin {
    private static Optional<Baldr> instance;

    private final PlayerDataManager playerDataManager = new PlayerDataManager();
    private final CommandManager commandManager = new CommandManager();
    private final CheckManager checkManager = new CheckManager();
    private final AlertManager alertManager = new AlertManager();

    public Baldr() {
        instance = Optional.of(this);

        Arrays.stream(Baldr.class.getDeclaredFields())
                .filter(field -> Registerable.class.isAssignableFrom(field.getType()))
                .forEach(field -> {
                    try {
                        Registerable registerable = (Registerable) field.get(this);

                        registerable.register();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    protected void enable() {
        registerListeners();
        registerTasks();
    }

    private void registerListeners() {
        ClassIndex.getSubclasses(Listener.class, getClassLoader())
                .forEach(clazz -> {
                    try {
                        clazz.newInstance().register();
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void registerTasks() {
        Schedulers.sync().runRepeating(() -> {
            playerDataManager.getPlayers()
                    .forEach(playerData -> playerData.getPacketTracker().handleTick());
        }, 1L, 1L);
    }

    protected void disable() {
        HelperExecutors.shutdown();
    }

    public static Baldr getInstance() {
        return instance.orElseThrow(() -> new IllegalStateException("Baldr instance is null."));
    }

}
