package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.dto.ChangePasswordForm;
import com.WD09.BeautySaloonSystem.dto.ProfileUpdateForm;
import com.WD09.BeautySaloonSystem.entities.Customer;
import jakarta.validation.Valid;

public interface CustomerProfileService {


    Customer getByEmail(String email);

    Customer getProfileByEmail(String email);
    Customer updateProfile(String currentEmail, ProfileUpdateForm form);

    void changePassword(String currentEmail, @Valid ChangePasswordForm form);
}
