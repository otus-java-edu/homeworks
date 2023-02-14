package IoC;

import Annotations.Log;
import TestClass.TestLogging;
import TestClass.TestLoggingInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Ioc {

    private Ioc() {
    }

    public static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            var classMethod = myClass.getClass().getMethod(method.getName(), method.getParameterTypes());
            if (classMethod.isAnnotationPresent(Log.class))
            {
                if (args != null)
                {
                    var vars = Arrays.stream(args).map(a -> " param: " + a.toString()).collect(Collectors.toUnmodifiableList());
                    var params = String.join(",", vars);
                    System.out.println(String.format("executing method: %s,%s", classMethod.getName(), params));
                }
                else
                    System.out.println(String.format("executing method: %s", classMethod.getName()));
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
