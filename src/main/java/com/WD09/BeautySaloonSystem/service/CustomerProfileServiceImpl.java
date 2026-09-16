package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.dto.ChangePasswordForm;
import com.WD09.BeautySaloonSystem.dto.ProfileUpdateForm;
import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Assumes:
 *  - CustomerRepository extends JpaRepository<Customer, Long> and has
 *      Optional<Customer> findByEmail(String email);
 *      boolean existsByEmail(String email);
 *  - Customer has fields fullName, email, phone, address, password (hashed)
 *    plus loyaltyPoints / membershipTier / totalSpent already used by the
 *    dashboard page.
 *  - Passwords are stored hashed and a PasswordEncoder bean (e.g. BCrypt) is
 *    already configured for Spring Security login.
 *
 * Adjust field/method names to match your actual entity and repository.
 */
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