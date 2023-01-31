package Tests;


import Annotations.After;
import Annotations.Before;
import Annotations.Test;
import homework.Customer;
import homework.CustomerService;

import java.util.HashMap;
import java.util.Map;


class CustomerTest {

    @Before
    void setup(){

    }
    @After
    void tearDown(){

    }
    @Test
    void setterCustomerTest() {
        //given
        String expectedName = "updatedName";
        String name = "nameVas";
        Customer customer = new Customer(1, name, 2);

        //when
        customer.setName(expectedName);

        //then
        if (!customer.getName().equals(expectedName))
            throw new RuntimeException("Test are not passed");
    }

    @Test
    void customerAsKeyTest() {
        //given
        final long customerId = 1L;
        Customer customer = new Customer(customerId, "Ivan", 233);
        Map<Customer, String> map = new HashMap<>();

        String expectedData = "data";
        map.put(customer, expectedData);

        //when
        long newScore = customer.getScores() + 10;
        String factData = map.get(new Customer(customerId, "IvanChangedName", newScore));

        //then
        if (!factData.equals(expectedData))
            throw new RuntimeException("Test are not passed");

        //when
        long newScoreSecond = customer.getScores() + 20;
        customer.setScores(newScoreSecond);
        String factDataSecond = map.get(customer);

        //then
        if (!factDataSecond.equals(expectedData))
            throw new RuntimeException("Test are not passed");
    }

    @Test
    void scoreSortingTest() {
        //given
        Customer customer1 = new Customer(1, "Ivan", 233);
        Customer customer2 = new Customer(2, "Petr", 11);
        Customer customer3 = new Customer(3, "Pavel", 888);

        CustomerService customerService = new CustomerService();
        customerService.add(customer1, "Data1");
        customerService.add(customer2, "Data2");
        customerService.add(customer3, "Data3");

        //when
        Map.Entry<Customer, String> smallestScore = customerService.getSmallest();
        //then
        if (!smallestScore.getKey().equals(customer2))
            throw new RuntimeException("Test are not passed");

        //when
        // подсказка:
        // a key-value mapping associated with the least key strictly greater than the given key, or null if there is no such key.
        Map.Entry<Customer, String> middleScore = customerService.getNext(new Customer(10, "Key", 20));
        //then
        if (!middleScore.getKey().equals(customer1))
            throw new RuntimeException("Test are not passed");
        middleScore.getKey().setScores(10000);
        middleScore.getKey().setName("Vasy");

        //when
        Map.Entry<Customer, String> biggestScore = customerService.getNext(customer1);
        //then
        if (!biggestScore.getKey().equals(customer3))
            throw new RuntimeException("Test are not passed");

        //when
        Map.Entry<Customer, String> notExists = customerService.getNext(new Customer(100, "Not exists", 20000));
        //then
        if (notExists != null)
            throw new RuntimeException("Test are not passed");

    }

    @Test
    void mutationTest() {
        //given
        Customer customer1 = new Customer(1, "Ivan", 233);
        Customer customer2 = new Customer(2, "Petr", 11);
        Customer customer3 = new Customer(3, "Pavel", 888);

        CustomerService customerService = new CustomerService();
        customerService.add(customer1, "Data1");
        customerService.add(new Customer(customer2.getId(), customer2.getName(), customer2.getScores()), "Data2");
        customerService.add(customer3, "Data3");

        //when
        Map.Entry<Customer, String> smallestScore = customerService.getSmallest();
        smallestScore.getKey().setName("Vasyl");

        //then
        if (!customerService.getSmallest().getKey().getName().equals(customer2.getName()))
            throw new RuntimeException("Test are not passed");
    }
}