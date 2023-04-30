package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.IllegalConfigException;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigs) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        var configs = Arrays.stream(initialConfigs).filter(m->m.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(x -> x.getDeclaredAnnotation(AppComponentsContainerConfig.class).order())).toList();
        for (var config: configs) {
            processConfig(config);
        }
    }

    private void processConfig(Class<?> configClass) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);

        var constructor = configClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        var config = constructor.newInstance();
        var methods = configClass.getDeclaredMethods();
        for (var method: Arrays.stream(methods).filter(m->m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(x -> x.getDeclaredAnnotation(AppComponent.class).order())).toList())
        {
            var name = method.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(name))
                throw new IllegalConfigException("More than one component with same name");
            var params = Arrays.stream(method.getParameterTypes()).map(this::getAppComponent).toArray();
            var bean = method.invoke(config, params);
            appComponentsByName.put(name, bean);
            appComponents.add(bean);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalConfigException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        var result = appComponents.stream().filter(c->componentClass.isAssignableFrom(c.getClass())).toList();
        if (result.size() != 1)
            throw new IllegalConfigException("Too many components for class " + componentClass.getName());
        return (C) result.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        if (!appComponentsByName.containsKey(componentName))
            throw new IllegalConfigException("No component with such name " + componentName);
        return (C) appComponentsByName.get(componentName);
    }
}
