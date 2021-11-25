package ml.kalanblowsystemmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowsystemmanagement.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
	List<Device> findByUserId(Long userId);
}
