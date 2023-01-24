package homework;


import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customerStringTreeMap = new TreeMap<>((o1, o2) ->
            (int)(o1.getScores() - o2.getScores()));

    public Map.Entry<Customer, String> getSmallest() {
        return cloneEntry(customerStringTreeMap.firstEntry());
    }
    private Map.Entry<Customer, String> cloneEntry(Map.Entry<Customer, String> entry)
    {
        if (entry == null)
            return null;
        var foundCustomer = entry.getKey();
        var result = new AbstractMap.SimpleImmutableEntry<>(
                new Customer(foundCustomer.getId(),
                        foundCustomer.getName(),
                        foundCustomer.getScores()),
                entry.getValue());
        return result;
    }
    public Map.Entry<Customer, String> getNext(Customer customer) {
        return cloneEntry(customerStringTreeMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }
}
