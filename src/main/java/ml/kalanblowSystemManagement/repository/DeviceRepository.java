package ml.kalanblowSystemManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
	List<Device> findByUserId(Long userId);
}
