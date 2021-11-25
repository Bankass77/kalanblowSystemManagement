
package ml.kalanblowsystemmanagement.service.searching;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Component
public class UserSearchParameters {

    private String usersProperty;
    private String propertyValue;
    private Integer pageSize;
    private Integer page;

    public Optional<String> getUsersProperty() {
        return Optional.ofNullable(usersProperty);
    }

    public Optional<String> getPropertyValue() {
        return Optional.ofNullable(propertyValue);
    }

    public Optional<Integer> getPageSize() {
        return Optional.ofNullable(pageSize);
    }

    public Optional<Integer> getPage() {
        return Optional.ofNullable(page);
    }
}
