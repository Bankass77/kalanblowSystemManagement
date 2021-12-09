
package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Controller
@NoArgsConstructor
@RequestMapping("/user")
public class SearchController {

    private UserService searchService;

    @Autowired
    public SearchController(UserService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping("/search")
    public ModelAndView search(@PathVariable Map<String, Object> params) {
        int draw = params.containsKey("draw") ? Integer.parseInt(params.get("draw").toString()) : 1;
        int length =
                params.containsKey("length") ? Integer.parseInt(params.get("length").toString())
                        : 30;
        int start =
                params.containsKey("start") ? Integer.parseInt(params.get("start").toString()) : 30;
        int currentPage = start / length;

        String sortName = "id";
        String dataTableOrderColumnIdx = params.get("order[0][column]").toString();
        String dataTableOrderColumnName = "columns[" + dataTableOrderColumnIdx + "][data]";
        if (params.containsKey(dataTableOrderColumnName))
            sortName = params.get(dataTableOrderColumnName).toString();
        String sortDir =
                params.containsKey("order[0][dir]") ? params.get("order[0][dir]").toString()
                        : "asc";

        Sort.Order sortOrder = new Sort.Order(
                (sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC), sortName);
        Sort sort = Sort.by(sortOrder);

        Pageable pageRequest = PageRequest.of(currentPage, length, sort);

        String queryString = (String) (params.get("search[value]"));

        Page<UserDto> uPage =
                searchService.findAllPageableOrderByLastName(queryString, pageRequest);

        long totalRecords = uPage.getTotalElements();

        List<Map<String, Object>> cells = new ArrayList<>();
        uPage.forEach(userDto -> {
            Map<String, Object> cellData = new HashMap<>();
            cellData.put("firstName", userDto.getFirstName());
            cellData.put("lastName", userDto.getLastName());
            cellData.put("email", userDto.getEmail());
            cellData.put("address", userDto.getAdresse());
            cellData.put("phoneNumber", userDto.getMobileNumber());
            cells.add(cellData);
        });

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("draw", draw);
        jsonMap.put("recordsTotal", totalRecords);
        jsonMap.put("recordsFiltered", totalRecords);
        jsonMap.put("data", cells);

        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(jsonMap);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView("resultPage");
        modelAndView.addObject("search", json);
        return modelAndView;
    }
}
