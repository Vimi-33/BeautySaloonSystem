package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.dto.ChangePasswordForm;
import com.WD09.BeautySaloonSystem.dto.ProfileUpdateForm;
import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerProfileServiceImpl implements CustomerProfileService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerProfileServiceImpl(CustomerRepository customerRepository,
                                      PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer getByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("No account found for " + email));
    }

    public Customer getCustomerByEmail(String email) {
        return getByEmail(email); // Reuses the same robust check
    }

    @Override
    public Customer getProfileByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Customer not found with email: " + email));
    }

    @Override
    @Transactional
    public Customer updateProfile(String currentEmail, ProfileUpdateForm form) {
        Customer customer = getByEmail(currentEmail);

        String newEmail = form.getEmail().trim().toLowerCase();
        if (!newEmail.equalsIgnoreCase(customer.getEmail())
                && customerRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("That email address is already in use.");
        }

        customer.setFullName(form.getFullName().trim());
        customer.setEmail(newEmail);
        customer.setPhone(form.getPhone() != null ? form.getPhone().trim() : null);

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer updateProfile(String email, Customer updatedCustomer) {
        Customer customer = getByEmail(email);

        customer.setFullName(updatedCustomer.getFullName());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public void changePassword(String currentEmail, @Valid ChangePasswordForm form) {
        Customer customer = getByEmail(currentEmail);

        if (!passwordEncoder.matches(form.getCurrentPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("Your current password is incorrect.");
        }

        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation don't match.");
        }

        if (passwordEncoder.matches(form.getNewPassword(), customer.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current one.");
        }

        customer.setPassword(passwordEncoder.encode(form.getNewPassword()));
        customerRepository.save(customer);
    }
}