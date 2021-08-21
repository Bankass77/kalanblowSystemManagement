package ml.kalanblowSystemManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;

@Repository(value = "userRepository")
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{

	Optional<User> findUserById(Long id);

	Optional<User> findByEmail(String email);

	User findUserByfirstNameAndLastName(String firstName, String lastName);

	User findUserByRoles(UserRole name);

	User deleteUserById(Long id);

	User deleteUserByEmail(String email);

	Page<User> findAll(Pageable pageable);

	List<User> findAllByOrderByIdAsc();

	List<User> findAllByLastNameContainingIgnoreCaseOrderByIdAsc(String lastName);

	List<User> findUserByMobileNumber(String mobileNumber);

}
