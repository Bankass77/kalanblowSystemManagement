
package ml.kalanblowSystemManagement.model;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    ADMIN, STAFF, TEACHER, PARENT, STUDENT, VIEWER;

    // Forms role hiearchy like ADMIN>STAFF>TEACHER>STUDENT>PARENT>USER
    // the sign> is not greater than but it means include, so that ADMIn include all
    // other roles and hence
    // ADMIN can access APIs for all other roles. Similary TEACHER cans access for
    // PARENT and so on.
    // User is least open, it can not any other API other than that of its own.

    public static String getRoleHiearchy() {

        return Arrays.stream(UserRole.values()).map(UserRole::getUserRole)
                .collect(Collectors.joining(">"));
    }

    public String getUserRole() {
        return name();
    }

}
