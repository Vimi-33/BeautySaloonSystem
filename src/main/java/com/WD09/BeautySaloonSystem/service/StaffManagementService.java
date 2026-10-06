package com.WD09.BeautySaloonSystem.service;

import com.WD09.BeautySaloonSystem.entities.StaffEntity;
import com.WD09.BeautySaloonSystem.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffManagementService {

    private final StaffRepository staffRepository;

    @Autowired
    public StaffManagementService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public List<StaffEntity> getAllStaff() {
        return staffRepository.findAll();
    }

    public StaffEntity getStaffById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Staff member not found with id: " + id));
    }

    public void saveStaff(StaffEntity staff) {
        staffRepository.save(staff);
    }

    public void updateStaff(Long id, StaffEntity updatedStaff) {
        StaffEntity existing = getStaffById(id);
        existing.setFullName(updatedStaff.getFullName());
        existing.setEmail(updatedStaff.getEmail());
        existing.setPhone(updatedStaff.getPhone());
        existing.setRole(updatedStaff.getRole());
        existing.setStatus(updatedStaff.getStatus());
        staffRepository.save(existing);
    }

    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }
}