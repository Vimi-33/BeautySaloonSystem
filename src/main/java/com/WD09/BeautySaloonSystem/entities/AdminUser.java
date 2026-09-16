package com.WD09.BeautySaloonSystem.entities;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
public class AdminUser extends User {

    private String department = "General Management";


    @Override
    public String getRoleName() {
        return "ADMIN";
    }

    @Override
    public String getDashboardRedirectUrl() {
        return "redirect:/admin/dashboard";
    }
}