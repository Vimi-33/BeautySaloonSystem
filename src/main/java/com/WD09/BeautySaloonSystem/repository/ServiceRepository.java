package com.WD09.BeautySaloonSystem.repository;


import com.WD09.BeautySaloonSystem.entities.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {
}