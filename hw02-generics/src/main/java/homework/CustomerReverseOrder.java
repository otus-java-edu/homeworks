package homework;


import java.util.LinkedList;

public class CustomerReverseOrder {

    private final LinkedList<Customer> customerDeque = new LinkedList<>();

    public void add(Customer customer) {
        customerDeque.add(customer);
    }

    public Customer take() {
        return customerDeque.pollLast();
    }
}
