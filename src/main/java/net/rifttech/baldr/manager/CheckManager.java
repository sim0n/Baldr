package net.rifttech.baldr.manager;

import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.check.Check;
import net.rifttech.baldr.data.Registerable;
import net.rifttech.baldr.player.PlayerData;
import org.atteo.classindex.ClassIndex;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class CheckManager implements Registerable {
    public static final List<Constructor<?>> CONSTRUCTORS = new ArrayList<>();

    @Override
    public void register() {
        ClassIndex.getSubclasses(Check.class, Baldr.getInstance().getClassloader())
                .forEach(clazz -> {
                    try {
                        if (Modifier.isAbstract(clazz.getModifiers()))
                            return;

                        CONSTRUCTORS.add(clazz.getConstructor(PlayerData.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }
}
