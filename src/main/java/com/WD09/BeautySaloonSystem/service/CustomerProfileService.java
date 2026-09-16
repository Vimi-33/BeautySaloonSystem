package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.dto.ChangePasswordForm;
import com.WD09.BeautySaloonSystem.dto.ProfileUpdateForm;
import com.WD09.BeautySaloonSystem.entities.Customer;
import jakarta.validation.Valid;

public interface CustomerProfileService {

    /**
     * Loads the customer that is currently logged in.
     * @param email the email/username taken from the Spring Security Authentication
     */
    Customer getByEmail(String email);

    /**
     * Applies the "Personal information" form to the given customer and saves it.
     * Throws IllegalArgumentException (with a user-facing message) if, for example,
     * the new email is already used by another account.
     */
    Customer updateProfile(String currentEmail, ProfileUpdateForm form);

    /**
     * Verifies the current password and, if it matches, saves the new one.
     * Throws IllegalArgumentException (with a user-facing message) if the current
     * password is wrong or the new/confirm passwords don't match.
     */
    void changePassword(String currentEmail, @Valid ChangePasswordForm form);
}
