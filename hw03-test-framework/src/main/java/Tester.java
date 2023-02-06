import Annotations.After;
import Annotations.Before;
import Annotations.Test;
import lombok.Getter;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tester {

    private Class clazz;
    private Optional<Method> beforeMethod;
    private Collection<Method> testMethods;
    private Optional<Method> afterMethod;

    private Constructor constructor;

    private boolean isInitialized = false;

    private PrintStream outStream;

    @Getter
    private int passedTests;
    @Getter
    private int failedTests;

    public Tester(PrintStream outStream){
        this.outStream = outStream;
    }

    public boolean Initialize(String className){
        try {
            passedTests = 0;
            failedTests = 0;
            clazz = Class.forName(className);

            var allMethods = clazz.getDeclaredMethods();
            beforeMethod = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(Before.class)).findFirst();
            testMethods = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(Test.class)).collect(Collectors.toUnmodifiableList());
            afterMethod = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(After.class)).findFirst();
            try {
                constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                outStream.println("Error - There is no default constructor");
                return false;
            }
        } catch (ClassNotFoundException e) {
            outStream.println(String.format("Class %s not found", className));
            return false;
        }
        isInitialized = true;
        return true;
    }

    public void ExecuteTests()
    {
        for (var test : testMethods) {
            Object instance = null;
            try {
                instance = constructor.newInstance();
            } catch (Exception e) {
                outStream.println(String.format("Error on Instantiating by constructor: %s", e.getMessage()));
                failedTests++;
                break;
            }
            if (!executeMethod(instance, beforeMethod)) {
                executeMethod(instance, afterMethod);
                failedTests++;
                break;
            }
            if (executeMethod(instance, Optional.ofNullable(test))) {
                outStream.println(String.format("Test %s is passed", test.getName()));
                passedTests++;
            }
            else {
                outStream.println(String.format("Test %s is failed", test.getName()));
                failedTests++;
            }
            executeMethod(instance, afterMethod);
        }

        outStream.println("==========================================================================");
        outStream.println(String.format("Total processed tests: %d", passedTests + failedTests));
        outStream.println(String.format("Passed tests: %d", passedTests));
        outStream.println(String.format("Failed tests: %d", failedTests));
    }
    private boolean executeMethod(Object obj, Optional<Method> method)
    {
        if (method.isPresent()) {
            try {
                var m = method.get();
                m.setAccessible(true);
                m.invoke(obj);
            } catch (Exception e) {
                outStream.println(String.format("Error on method %s: %s",
                        method.get().getName(), e.getMessage()));
                return false;
            }
        }
        return true;
    }
}
