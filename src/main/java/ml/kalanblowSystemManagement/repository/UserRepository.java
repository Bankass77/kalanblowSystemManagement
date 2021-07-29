package ml.kalanblowSystemManagement.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findUserById(Long id);

	User findUserByEmail(String email);

	User findUserByfirstNameAndLastName(String firstName, String lastName);

	User findUserByRoles(UserRole name);

	User deleteUserById(Long id);

	User deleteUserByEmail(String email);

	Page<User> findAll(Pageable pageable);

}
