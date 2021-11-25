package ml.kalanblowsystemmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowsystemmanagement.model.User;
import ml.kalanblowsystemmanagement.model.UserLocation;

@Repository
public interface UserLocationRepository extends JpaRepository<UserLocation, Long> {

	UserLocation findByCountryAndUser(String country, User user);

    
}
