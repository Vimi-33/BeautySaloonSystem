package com.WD09.BeautySaloonSystem.repository;

import com.WD09.BeautySaloonSystem.entities.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long> {
}
