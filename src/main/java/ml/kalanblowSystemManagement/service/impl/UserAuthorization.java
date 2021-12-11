package ml.kalanblowSystemManagement.service.impl;

import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import ml.kalanblowSystemManagement.dto.model.RoleDto;
import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.model.Privilege;
import ml.kalanblowSystemManagement.model.Role;
import ml.kalanblowSystemManagement.model.User;
import ml.kalanblowSystemManagement.repository.PrivilegeRepository;
import ml.kalanblowSystemManagement.repository.UserRepository;

/**
 * Service de vérification des privileges utilisateurs.
 */
@Service
public class UserAuthorization {
	private final UserRepository userRepository;

	private final PrivilegeRepository privilegeRepository;
	private final ModelMapper modelMapper;


	@Autowired
	public UserAuthorization(UserRepository userRepository, PrivilegeRepository privilegeRepository, ModelMapper modelMapper) {
		super();
		this.userRepository = userRepository;
		this.privilegeRepository = privilegeRepository;
		this.modelMapper=modelMapper;
	}


	/**
	 * Vérifie l'autorisation pour les privileges qui ne sont pas sensé contenir de contraintes sur l'objet visé
	 * @param action le type d'action demandée
	 * @param entity l'objet visé
	 * @return
	 */

	public boolean can(String action, String entity) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Optional<User> uOptional=userRepository.findByEmail(auth.getName());
		UserDto userDto = modelMapper.map(uOptional, UserDto.class);
		//recuperation du privilege par action et par entité : logiquement il n'en existe qu'un par role avec cette action et cet objet
		RoleDto element=new RoleDto();
		Set<RoleDto> roleDtos=userDto.getRoles();
		for (RoleDto roleDto : roleDtos) {
		element=roleDto;
		}
		Privilege privilege = privilegeRepository.findPrivilegeByActionAndEntityAndRole(action, entity, modelMapper.map(element, Role.class));
		//si privileges existe et qu'il n'attend pas de vérification de contrainte
		return (null != privilege && !privilege.isConstrained());
	}


	/**
	 * Vérifie l'autorisations pour les privileges qui comportent des contraintes sur l'objet visé
	 * @param action le type d'action demandée
	 * @param entity l'objet visé
	 * @param entityId l'id de l'objet visé
	 * @return Vrai ou Faux
	 */

	public boolean can(String action, String entity, Long entityId) {

		UserDto currentUser = (UserDto)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		boolean authorized = false;
		//recuperation du privilege par action et par entité : Il ne peut en exister qu'un par role
		RoleDto element=new RoleDto();
		Set<RoleDto> roleDtos=currentUser.getRoles();
		for (RoleDto roleDto : roleDtos) {
		element=roleDto;
		}
		Privilege privilege = privilegeRepository.findPrivilegeByActionAndEntityAndRole(action, entity,modelMapper.map(element, Role.class));

		if (null == privilege) {
			return authorized;
		}

		//implémentation des logiques métier de vérification des contraintes
		/*
		 * switch (entity){ //Vérification si l'utilisateur est affecté au contrat via
		 * la table d'affectation UserContract case "Contract": Optional<User> user =
		 * userRepository.findById(currentUser.getId()); //Récuperation des contrats sur
		 * lesquel est affecté l'utilisateur List<UserContracts> userContracts =
		 * user.get().getUserContracts(); for (UserContract userContract :
		 * userContracts) { if (userContract.getContract().getId().equals(entityId)) {
		 * authorized = true; break; } } break; //Vérification si l'utilisateur visé par
		 * la requete est le même que l'utilisateur actuellement authentifié case
		 * "User": if (currentUser.getId().equals(entityId)) { authorized = true; }
		 * break; default : authorized = false; break; }
		 */

		return authorized;

	}

}
