package demo;

import IoC.Ioc;
import TestClass.TestLoggingInterface;

public class ProxyDemo {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createMyClass();
        myClass.calculation();
        myClass.calculation(1);
        myClass.calculation(1, 2, "");
    }
}



