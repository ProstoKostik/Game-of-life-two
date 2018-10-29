package ru.sbt.rgrtu.context;

import java.util.ArrayList;
import java.util.List;

public class Context {

    private final List<Object> beans = new ArrayList<>();

    public <T> T get(Class<T> clazz) {
        for (Object bean: beans) {
            if (clazz.isAssignableFrom(bean.getClass())) return (T)bean;
        }
        return null;
    }

    public void add(Object bean) {
        beans.add(bean);
    }
}
