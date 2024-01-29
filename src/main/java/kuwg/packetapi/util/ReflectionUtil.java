package kuwg.packetapi.util;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtil {

    public static final String v = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().substring(23);

    public static Field[] getFields(Class<?> clazz){
        return clazz.getFields();
    }

    @Nullable
    public static Field getField(Class<?> clazz, String fieldName){
        try {
            return clazz.getField(fieldName);
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

    @Nullable
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params){
        try{
            if(params.length>0)
                return clazz.getMethod(methodName, params);
            else return clazz.getMethod(methodName);
        }catch (NoSuchMethodException ignored){
            return null;
        }
    }

    public static Method[] getMethods(Class<?> clazz){
        return clazz.getMethods();
    }

    @Nullable
    public static Object getInvokeResult(Method method, Object object, Object... params){
        try {
            if (params.length > 0)
                return method.invoke(object, params);
            else return method.invoke(object);
        }catch (IllegalAccessException | InvocationTargetException ignored){
            return null;
        }
    }

    @Nullable
    public static Class<?> classForName(String path){
        try{
            return Class.forName(path);
        }catch (ClassNotFoundException ignored){
            return null;
        }
    }

    @Nullable
    public static Field getDeclaredField(Class<?> clazz, String fieldName){
        try{
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Nullable
    public static Object getFieldInvocationResult(Field field, Object obj){
        try{
            return field.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    @Nullable
    public static Object startClassConst(Class<?> classToInit, Object... params) {
        try {
            final Class<?>[] paramTypes = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);
            Constructor<?> constructor = classToInit.getConstructor(paramTypes);
            return constructor.newInstance(params);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {
            return null;
        }
    }

    public static Object invokeMethod(Object target, String methodName, Object... params) {
        try {
            Class<?>[] paramTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                paramTypes[i] = params[i].getClass();
            }
            Method method = target.getClass().getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(target, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    public void setValue(Object instance, String fieldName, Object value) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    @Nullable
    public Object getValue(Object instance, String fieldName) throws Exception {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    @SuppressWarnings("unchecked")
    public static  <T> T getTField(Object location, String fieldName){
        final Field field = getField(location.getClass(), fieldName);
        if(field==null)return null;
        final Object result = getFieldInvocationResult(field, location);
        try {
            return (T) result;
        }catch (ClassCastException ignored){
            return null;
        }
    }

    public static void setField(Object location, String fieldName, Object content){
        Field field = getField(location.getClass(), fieldName);
        if(field==null)return;
        field.setAccessible(true);
        try {
            field.set(location, content);
        } catch (IllegalAccessException ignored) {

        }
    }


























    private ReflectionUtil(){
        throw new UnsupportedOperationException("You cannot instantiate this class.");
    }
}
