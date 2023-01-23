package homework;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customerStringTreeMap = new TreeMap<>((o1, o2) ->
            (int)(o1.getScores() - o2.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        var found = customerStringTreeMap.firstEntry();
        if (found == null)
            return null;
        var foundCustomer = found.getKey();
        var result = new AbstractMap.SimpleImmutableEntry<>(
                new Customer(foundCustomer.getId(),
                        foundCustomer.getName(),
                        foundCustomer.getScores()),
                found.getValue());
        return result;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        var found = customerStringTreeMap.higherEntry(customer);
        if (found == null)
            return null;
        var foundCustomer = found.getKey();
        var result = new AbstractMap.SimpleImmutableEntry<>(
                new Customer(foundCustomer.getId(),
                        foundCustomer.getName(),
                        foundCustomer.getScores()),
                found.getValue());
        return result;
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }
}
