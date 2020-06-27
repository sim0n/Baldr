package net.rifttech.baldr.wrapper;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a class wrapper used for classes that has private fields that you want to access
 * here we use {@link MethodHandles} as they're faster than reflection.
 *
 * It's advised that you switch to {@link LambdaMetafactory} in the future
 */
public abstract class ClassWrapper<T> {
    protected final List<Field> fields = new ArrayList<>();

    protected final T instance;

    public ClassWrapper(T instance, Class<?> clazz) {
        this.instance = instance;

        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        fields.forEach(field -> field.setAccessible(true));
    }

    protected void setField(int index, Object object) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            lookup.unreflectSetter(fields.get(index)).invokeExact(instance, object);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the value of a field using method handles
     * @param index The index of the field
     * @return The value of the field specified
     */
    protected <K> K getField(int index) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            return (K) lookup.unreflectGetter(fields.get(index)).invoke(instance);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }
}
