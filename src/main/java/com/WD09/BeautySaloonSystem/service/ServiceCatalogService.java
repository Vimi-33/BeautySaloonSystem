package com.WD09.BeautySaloonSystem.service;


import com.WD09.BeautySaloonSystem.entities.ServiceEntity;
import com.WD09.BeautySaloonSystem.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceCatalogService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceCatalogService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    public ServiceEntity getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
    }

    public void saveService(ServiceEntity service) {
        serviceRepository.save(service);
    }

    public void updateService(Long id, ServiceEntity updatedService) {
        ServiceEntity existing = getServiceById(id);
        existing.setName(updatedService.getName());
        existing.setCategory(updatedService.getCategory());
        existing.setDescription(updatedService.getDescription());
        existing.setDurationMinutes(updatedService.getDurationMinutes());
        existing.setPrice(updatedService.getPrice());
        serviceRepository.save(existing);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}