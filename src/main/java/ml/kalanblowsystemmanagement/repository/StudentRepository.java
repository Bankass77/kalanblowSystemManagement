package ml.kalanblowsystemmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.kalanblowsystemmanagement.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

}
