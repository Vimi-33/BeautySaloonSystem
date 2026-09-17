package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.entities.User;
import com.WD09.BeautySaloonSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer registerCustomer(Customer customer) {
        if (userRepository.existsByEmail(customer.getEmail())) {
            throw new IllegalArgumentException("Email address is already registered!");
        }

        // Encrypt password using BCrypt
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        // Set default values for new Customer accounts
        customer.setMembershipTier("SILVER");
        customer.setLoyaltyPoints(0);
        customer.setTotalSpent(0.0);

        return userRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return userRepository.findAllCustomers();
    }

    @Override
    public User updateUserProfile(Long userId, String fullName, String phone) {
        User user = findById(userId);
        user.setFullName(fullName);
        user.setPhone(phone);
        return userRepository.save(user);
    }

    @Override
    public void updateCustomerTier(Long customerId, String newTier) {
        User user = findById(customerId);

        if (user instanceof Customer customer) {
            customer.setMembershipTier(newTier.toUpperCase());
            userRepository.save(customer);
        } else {
            throw new IllegalArgumentException("Target user is not a Customer account.");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Cannot delete. User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void updateUserProfile(Long id, String fullName, String email, String phone) {
        User user = findById(id);

        if (!email.equalsIgnoreCase(user.getEmail()) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("That email address is already in use.");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = findById(id);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Your current password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void applyAppointmentRewards(Long customerId, Double price) {
        if (price == null || price <= 0) return;

        User user = findById(customerId);

        if (user instanceof Customer customer) {
            // 1. Increment Total Spent
            // 1. Increment Total Spent
// 1. Increment Total Spent
            double currentSpent = customer.getTotalSpent(); // Primitive double cannot be null
            double newTotalSpent = currentSpent + price;
            customer.setTotalSpent(newTotalSpent);

// 2. Add Loyalty Points (1 point earned per $10 spent)
            int pointsEarned = (int) (price / 10);
            int currentPoints = customer.getLoyaltyPoints(); // Primitive int cannot be null
            customer.setLoyaltyPoints(currentPoints + pointsEarned);

// 3. Automatically upgrade Tier based on cumulative spent amount
            if (newTotalSpent >= 500.0) {
                customer.setMembershipTier("PLATINUM");
            } else if (newTotalSpent >= 200.0) {
                customer.setMembershipTier("GOLD");
            } else {
                customer.setMembershipTier("SILVER");
            }

            userRepository.save(customer);
        } else {
            throw new IllegalArgumentException("Target user is not a Customer account.");
        }
    }
}