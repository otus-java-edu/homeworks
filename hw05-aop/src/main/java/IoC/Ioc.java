package IoC;

import Annotations.Log;
import TestClass.TestLogging;
import TestClass.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    public static TestLoggingInterface Register(TestLoggingInterface obj) {
        InvocationHandler handler = new DemoInvocationHandler(obj);
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final HashMap<Method, Boolean> annotationPresentMap = new HashMap<>();

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            boolean isAnnotationPresent;
            if (annotationPresentMap.containsKey(method))
                isAnnotationPresent = annotationPresentMap.get(method);
            else{
                var classMethod = myClass.getClass().getMethod(method.getName(), method.getParameterTypes());
                isAnnotationPresent = classMethod.isAnnotationPresent(Log.class);
                annotationPresentMap.put(method, isAnnotationPresent);
            }
            if (isAnnotationPresent)
            {
                if (args != null)
                {
                    var vars = Arrays.stream(args).map(a -> " param: " + a.toString()).collect(Collectors.toUnmodifiableList());
                    var params = String.join(",", vars);
                    System.out.println(String.format("executing method: %s,%s", method.getName(), params));
                }
                else
                    System.out.println(String.format("executing method: %s", method.getName()));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
