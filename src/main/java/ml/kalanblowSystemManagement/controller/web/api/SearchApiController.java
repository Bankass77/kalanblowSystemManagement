/*
 * package ml.kalanblowSystemManagement.controller.web.api;
 * 
 * import java.util.List;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.web.bind.annotation.MatrixVariable; import
 * org.springframework.web.bind.annotation.PathVariable; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import ml.kalanblowSystemManagement.dto.model.UserDto; import
 * ml.kalanblowSystemManagement.service.SearchService;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/search") public class SearchApiController {
 * 
 * private SearchService searchService;
 * 
 * @Autowired public SearchApiController(SearchService searchService) {
 * this.searchService = searchService; }
 * 
 * public List<UserDto> search(@PathVariable String searchType, @MatrixVariable
 * List<String> keywords) { return searchService.search(searchType, keywords);
 * }}
 */