
package ml.kalanblowSystemManagement.controller.web.ui;

import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ml.kalanblowsystemmanagement.dto.model.RoleDto;
import ml.kalanblowsystemmanagement.service.RoleService;

@Controller
@RequestMapping("/role")
public class RoleController {

	public static final String REDIRECT_ADMIN_PAGE_ROLES = "redirect:/role/roles";
	public static final String ADMIN_PAGE_ROLE_EDIT_ROLE = "role/editRole";
	public static final String ADMIN_PAGE_ROLE_CREATE_ROLE = "role/newRole";
	private RoleService roleService;

	@Autowired
	public RoleController(RoleService roleService) {
		super();
		this.roleService = roleService;
	}

	@GetMapping("/roles")
	@Secured("ADMIN")
	public ModelAndView showUserRoles() {

		ModelAndView modelAndView = new ModelAndView("role/roles");
		modelAndView.addObject("roles", roleService.getAllRoles());

		return modelAndView;
	}

	@GetMapping("/roles/{id}")
	@Secured("ADMIN")
	public ModelAndView editRoleForm(@PathVariable Long id) {

		Optional<RoleDto> rolOptional = roleService.findById(id);
		ModelAndView modelAndView = new ModelAndView(ADMIN_PAGE_ROLE_EDIT_ROLE);
		modelAndView.addObject("role", rolOptional.get());

		return modelAndView;
	}

	@PostMapping("/roles/{id}")
	@Secured("ADMIN")
	public String updateRole(Model model, @PathVariable Long id,
			@ModelAttribute("oldRole") @Valid final RoleDto roleDto, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		Optional<RoleDto> persistedRole = roleService.findById(id);
		Set<RoleDto> allRoleDtos = roleService.getAllRoles();

		boolean roleNameAlreadysExists = roleService.checkIfRoleNameIsTaken(allRoleDtos, roleDto, persistedRole.get());
		boolean hasErrors = roleNameAlreadysExists || bindingResult.hasErrors();
		if (roleNameAlreadysExists) {
			bindingResult.rejectValue("name", "UniqueUsername.roleNameAlreadyExists");
		}
		if (hasErrors) {
			model.addAttribute("role", roleDto);
			model.addAttribute("org.springframework.validation.BindingResult.role", bindingResult);
			return REDIRECT_ADMIN_PAGE_ROLES;
		}

		roleService.saveOrUpdate(roleDto);
		redirectAttributes.addFlashAttribute("roleHasbeenUpdated", true);
		return REDIRECT_ADMIN_PAGE_ROLES;

	}

	@GetMapping("/newRole")
	@Secured("ADMIN")
	public String getAddNewUserRoleForm(Model model) {

		model.addAttribute("newRole", new RoleDto());

		return ADMIN_PAGE_ROLE_CREATE_ROLE;
	}

	@PostMapping("/newRole")
	@Secured("ADMIN")
	public String saveNewUserRole(@ModelAttribute("newRole") @Valid RoleDto newRole, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		boolean roleNameAlreadyExists = roleService.findByName(newRole.getUserRoleName()) != null;
		boolean hasErrors = roleNameAlreadyExists || bindingResult.hasErrors();

		String formWithErrors = ADMIN_PAGE_ROLE_CREATE_ROLE;

		if (roleNameAlreadyExists) {

			bindingResult.rejectValue("name", "UniqueUsername.roleNameAlreadyExists");
		}

		if (hasErrors) {
			return formWithErrors;
		}

		roleService.saveOrUpdate(newRole);
		redirectAttributes.addFlashAttribute("roleHasBeenSaved", true);
		return REDIRECT_ADMIN_PAGE_ROLES;
	}
}
