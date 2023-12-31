package com.tbert31;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;


@SpringBootApplication
@RestController
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ){

    }

    @PostMapping
    public ResponseEntity<String> addCustomer(@RequestBody NewCustomerRequest request) {
        try {
            Customer customer = new Customer();
            customer.setName(request.name());
            customer.setEmail(request.email());
            customer.setAge(request.age());
            customerRepository.save(customer);

            return ResponseEntity.ok("Customer added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"msg\": \"Error adding customer\"}");
        }
    }


    @DeleteMapping("{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") Integer id) {
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            customerRepository.deleteById(id);
            return ResponseEntity.ok("Customer deleted successfully");
        } else {
            String errorMessage = "Customer with id " + id + " does not exist";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"msg\": \"" + errorMessage + "\"}");
        }
    }


    @PutMapping("{customerId}")
    public ResponseEntity<String> updateCustomer(@PathVariable("customerId") Integer id, @RequestBody NewCustomerRequest request){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);

        if (optionalCustomer.isPresent()) {
            Customer existingCustomer = optionalCustomer.get();
            existingCustomer.setName(request.name());
            existingCustomer.setEmail(request.email());
            existingCustomer.setAge(request.age());
            customerRepository.save(existingCustomer);

            return ResponseEntity.ok("Customer updated successfully");
        } else {
            String errorMessage = "Customer with id " + id + " does not exist";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"msg\": \"" + errorMessage + "\"}");
        }
    }
}
