import model.Customer;

public class Driver {
    public static void main(String[] args) {

        // Test 1: Valid customer - should print successfully
        Customer customer = new Customer("first", "second", "j@domain.com");
        System.out.println(customer);

        // Test 2: Invalid email - should throw IllegalArgumentException
        Customer customer2 = new Customer("first", "second", "email");
        System.out.println(customer2);
    }
}