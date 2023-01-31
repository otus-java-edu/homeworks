import Annotations.After;
import Annotations.Before;
import Annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class Tester {
    public static void main(String[] args){
        if (args.length < 1)
            return;
        Class c = null;
        try {
            c = Class.forName(args[0]);
        } catch (ClassNotFoundException e) {
            System.out.println(String.format("Class %s not found", args[0]));
        }
        if (c!= null) {
            var allMethods = c.getDeclaredMethods();
            var beforeMethod = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(Before.class)).findFirst();
            var testMethods = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(Test.class)).collect(Collectors.toUnmodifiableList());
            var afterMethod = Arrays.stream(allMethods).filter(m->m.isAnnotationPresent(After.class)).findFirst();
            Constructor constructor = null;
            try {
                constructor = c.getDeclaredConstructor();
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                System.out.println("Error - There is no default constructor");
                return;
            }
            var passedTests = 0;
            var failedTests = 0;
            for (var test : testMethods) {
                Object instance = null;
                try {
                    instance = constructor.newInstance();
                } catch (Exception e) {
                    System.out.println(String.format("Error on Instantiating by constructor: %s", e.getMessage()));
                    failedTests++;
                    break;
                }
                if (!executeMethod(instance, beforeMethod)) {
                    executeMethod(instance, afterMethod);
                    failedTests++;
                    break;
                }
                if (executeMethod(instance, Optional.ofNullable(test))) {
                    System.out.println(String.format("Test %s is passed", test.getName()));
                    passedTests++;
                }
                else {
                    System.out.println(String.format("Test %s is failed", test.getName()));
                    failedTests++;
                }
                executeMethod(instance, afterMethod);
            }

            System.out.println("==========================================================================");
            System.out.println(String.format("Total processed tests: %d", passedTests + failedTests));
            System.out.println(String.format("Passed tests: %d", passedTests));
            System.out.println(String.format("Failed tests: %d", failedTests));
        }
    }

    private static boolean executeMethod(Object obj, Optional<Method> method)
    {
        if (method.isPresent()) {
            try {
                var m = method.get();
                m.setAccessible(true);
                m.invoke(obj);
            } catch (Exception e) {
                System.out.println(String.format("Error on method %s: %s",
                        method.get().getName(), e.getMessage()));
                return false;
            }
        }
        return true;
    }
}
