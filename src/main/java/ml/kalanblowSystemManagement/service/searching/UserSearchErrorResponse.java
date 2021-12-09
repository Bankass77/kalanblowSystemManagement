
package ml.kalanblowSystemManagement.service.searching;

import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.service.UserService;
import ml.kalanblowSystemManagement.utils.paging.InitialPagingSizes;
import ml.kalanblowSystemManagement.utils.paging.Pager;

@Slf4j
@AllArgsConstructor
@Data
public class UserSearchErrorResponse {

    private final UserService userService;

    public ModelAndView respondToNumberFormatException(UserSearchResult userSearchResult,
            ModelAndView modelAndView) {
        Pager pager = new Pager(userSearchResult.getUserPage().getTotalPages(),
                userSearchResult.getUserPage().getNumber(), InitialPagingSizes.BUTTONS_TO_SHOW,
                userSearchResult.getUserPage().getTotalElements());

        log.debug("UserSearchErrorResponse Pager:{}", pager);
        modelAndView.addObject("numberFormatException", true);
        modelAndView.addObject("pager", pager);
        modelAndView.addObject("users", userSearchResult.getUserPage());
        modelAndView.setViewName("adminPage/user/users");
        return modelAndView;
    }

    public ModelAndView respondToEmptySearchResult(ModelAndView modelAndView,
            PageRequest pageRequest) {
        modelAndView.addObject("noMatches", true);
        modelAndView.addObject("users", userService.findAllPageable(pageRequest));
        return modelAndView;
    }

}
