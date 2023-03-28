package demo;

import IoC.Ioc;
import TestClass.TestLogging;
import TestClass.TestLoggingInterface;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.register(new TestLogging());
        myClass.calculation();
        myClass.calculation(1);
        myClass.calculation(1, 2, "");
    }
}



