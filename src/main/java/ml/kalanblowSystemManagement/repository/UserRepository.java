package ml.kalanblowSystemManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.model.UserRole;


@SuppressWarnings("unused")
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	 Optional<User> findUserById(Long id);

	    User deleteUserById(Long id);

	    Optional<User> findByEmail(String email);

	    User deleteUserByEmail(String email);

	    User findUserByfirstNameAndLastName(String firstName, String lastName);

	    User findUserByRoles(UserRole name);

	    /**
	     * Returns a {@link Page} of entities meeting the paging restriction provided in the
	     * {@code Pageable} object.
	     *
	     * @param pageable
	     * @return a page of entities
	     */
	    Page<User> findAll(Pageable pageable);

	    List<User> findAllByOrderByIdAsc();

	    List<User> findAllByLastNameContainingIgnoreCaseOrderByIdAsc(String lastName);

	    List<User> findUserByMobileNumber(String mobileNumber);

	    Page<User> findByEmailContainingOrderByIdAsc(String email, Pageable pageable);
	    
	    Page<User> findByFirstNameContainingOrderByIdAsc(String name, Pageable pageable);

	    User findByEmailAndIdNot(String email, Long id);

	    Page<User> firstNameContaining(String name, PageRequest pageRequest);
}
