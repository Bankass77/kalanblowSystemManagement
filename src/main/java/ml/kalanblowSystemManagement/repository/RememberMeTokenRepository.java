package ml.kalanblowSystemManagement.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowSystemManagement.model.PersistentLogin;

@Repository
public interface RememberMeTokenRepository extends JpaRepository<PersistentLogin, String> {
	PersistentLogin findBySeries(String series);

	Set<PersistentLogin> findByFullName(String fullName);

	Iterable<PersistentLogin> findByLastUsedAfter(Date date);
}
