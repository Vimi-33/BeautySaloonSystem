package com.WD09.BeautySaloonSystem.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Backing object for the "Personal information" form on /customer/profile.
 * If you're on Spring Boot 2 / javax, swap the jakarta.validation imports
 * above for javax.validation.constraints instead.
 */
public class ProfileUpdateForm {

    @NotBlank(message = "Full name is required")
    @Size(max = 120, message = "Full name is too long")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email address")
    private String email;

    @Size(max = 30, message = "Phone number is too long")
    private String phone;


    public ProfileUpdateForm() {
    }

    public ProfileUpdateForm(String fullName, String email, String phone) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
