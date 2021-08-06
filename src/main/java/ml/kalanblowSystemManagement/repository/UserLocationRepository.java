package ml.kalanblowSystemManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

	UserLocation findByCountryAndUser(String country, User user);
}
