package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer updateProfile(String email, Customer updatedCustomer) {

        Customer customer = getCustomerByEmail(email);

        customer.setFullName(updatedCustomer.getFullName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(customer);
    }

    public boolean changePassword(String email,
                                  String currentPassword,
                                  String newPassword,
                                  String confirmPassword) {

        Customer customer = getCustomerByEmail(email);

        if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            return false;
        }

        customer.setPassword(passwordEncoder.encode(newPassword));

        customerRepository.save(customer);

        return true;
    }
}