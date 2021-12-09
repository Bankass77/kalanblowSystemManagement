
package ml.kalanblowSystemManagement.model;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    String userQuery;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder) {

        ArrayList<Predicate> predicates = new ArrayList<>();

        if (userQuery != null && userQuery != "") {
            predicates.add(criteriaBuilder.like(root.get("firstName"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("lastName"), '%' + userQuery + '%'));
            //predicates.add(criteriaBuilder.like(root.get("adresse"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("email"), '%' + userQuery + '%'));
            predicates.add(criteriaBuilder.like(root.get("mobileNumber"), '%' + userQuery + '%'));
          
        }

        return (!predicates.isEmpty()
                ? criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
                : null);
    }

}
