package TestClass;

import Annotations.Log;

public class TestLogging implements TestLoggingInterface{
    @Override
    @Log
    public void calculation() {

    }

    @Override
    public void calculation(int x) {

    }

    @Override
    public void calculation(int x, int y) {

    }

    @Override
    @Log
    public void calculation(int x, int y, String s) {

    }
}
