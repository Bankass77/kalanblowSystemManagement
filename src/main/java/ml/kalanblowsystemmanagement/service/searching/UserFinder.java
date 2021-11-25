
package ml.kalanblowsystemmanagement.service.searching;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowsystemmanagement.dto.model.UserDto;
import ml.kalanblowsystemmanagement.service.UserService;

import static java.lang.Long.parseLong;

@Data
@Service
@Slf4j
public class UserFinder {

    private UserService userService;

    @Autowired
    public UserFinder(UserService userService) {
        this.userService = userService;
    }

    public UserSearchResult searchUsersByProperty(PageRequest pageRequest,
            UserSearchParameters params) {
        Page<UserDto> userDtoPage = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        String s = params.getUsersProperty().get();

        if ("id".equals(s)) {
            try {
                userDtoPage = userService
                        .findByIdPageable(parseLong(params.getPropertyValue().get()), pageRequest);
            }
            catch (NumberFormatException e) {
                log.error(e.getMessage());
                return new UserSearchResult(userService.findAllPageable(pageRequest), true);
            }
        }
        else if ("firstName".equals(s))
            userDtoPage =
                    userService.findByFirstNameContaining(params.getPropertyValue().get(), pageRequest);

        else if ("email".equals(s))
            userDtoPage =
                    userService.findByEmailContaining(params.getPropertyValue().get(), pageRequest);
        return new UserSearchResult(userDtoPage, false);
    }

}
