package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.entities.Customer;
import com.WD09.BeautySaloonSystem.entities.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    Customer registerCustomer(Customer customer);
    User findByEmail(String email);
    User updateUserProfile(Long userId, String fullName, String phone);
    void updateUserProfile(Long id, String fullName, String email, String phone); // overload, keep the old one too
    void changePassword(Long id, String currentPassword, String newPassword);

    @Transactional(readOnly = true)
    User findById(Long userId);

    List<Customer> getAllCustomers();
    void deleteUser(Long userId);

    void updateCustomerTier(Long customerId, String newTier);
}