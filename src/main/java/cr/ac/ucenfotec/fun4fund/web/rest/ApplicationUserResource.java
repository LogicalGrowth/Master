package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.User;
import cr.ac.ucenfotec.fun4fund.security.AuthoritiesConstants;
import cr.ac.ucenfotec.fun4fund.service.ApplicationUserService;
import cr.ac.ucenfotec.fun4fund.service.MailService;
import cr.ac.ucenfotec.fun4fund.service.UserService;
import cr.ac.ucenfotec.fun4fund.service.dto.UserDTO;
import cr.ac.ucenfotec.fun4fund.web.rest.errors.BadRequestAlertException;
import cr.ac.ucenfotec.fun4fund.service.dto.ApplicationUserCriteria;
import cr.ac.ucenfotec.fun4fund.service.ApplicationUserQueryService;

import cr.ac.ucenfotec.fun4fund.web.rest.vm.ManagedUserVM;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link cr.ac.ucenfotec.fun4fund.domain.ApplicationUser}.
 */
@RestController
@RequestMapping("/api")
public class ApplicationUserResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationUserResource.class);

    private static final String ENTITY_NAME = "applicationUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final ApplicationUserService applicationUserService;
    private final UserService userService;
    private final ApplicationUserQueryService applicationUserQueryService;
    private final MailService mailService;

    public ApplicationUserResource(ApplicationUserService applicationUserService, UserService userService, ApplicationUserQueryService applicationUserQueryService, MailService mailService) {
        this.applicationUserService = applicationUserService;
        this.userService = userService;
        this.applicationUserQueryService = applicationUserQueryService;
        this.mailService = mailService;
    }

    /**
     * {@code POST  /application-users} : Create a new applicationUser.
     *
     * @param applicationUser the applicationUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new applicationUser, or with status {@code 400 (Bad Request)} if the applicationUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/application-users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ANONYMOUS + "\")")
    public ResponseEntity<ApplicationUser> createApplicationUser(@Valid @RequestBody ApplicationUser applicationUser) throws URISyntaxException {
        String subject = "Correo de confirmación del registro de usuario";
        String content = "Gracias por la suscripción";
        String template = "<subject>Gracias por la suscripción</subject>\n" +
            "<message>\n" +
            "Hola,<br/><br/>\n" +
            "<b>Este</b> correo es para agreadecerte por la suscripción <b>{ProjectTitle}</b>.\n" +
            "Que disfrutes de las busquedas y las donaciones\n" +
            "<br/><br/>\n" +
            "<br/><table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "<tr>\n" +
            "<td>\n" +
            "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n" +
            "<tr>\n" +
            "<td align=\"center\" style=\"border-radius: 3px;\" bgcolor=\"#33AA55\"><a href=\"{Url}\" target=\"_blank\" style=\"font-size: 16px; font-family: Helvetica, Arial, sans-serif; color: #ffffff; text-decoration: none; text-decoration: none;border-radius: 3px; padding: 12px 18px; border: 1px solid #33AA55; display: inline-block;\">View</a></td>\n" +
            "</tr>\n" +
            "</table>\n" +
            "</td>\n" +
            "</tr>\n" +
            "</table><br/><br/>\n" +
            "<br/>El equipo de Fun4Found<br/><br/></message>";
        log.debug("REST request to save ApplicationUser : {}", applicationUser);
        if (applicationUser.getId() != null) {
            throw new BadRequestAlertException("A new applicationUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = applicationUser.getInternalUser();
        user.setPassword(user.getRawpassword());
        ManagedUserVM userManagedVM = new ManagedUserVM(user);
        User newUser = userService.registerUser(userManagedVM,user.getRawpassword());
        Long jhisperUserId = newUser.getId();
        applicationUser.getInternalUser().setId(jhisperUserId);
        ApplicationUser result = applicationUserService.save(applicationUser);
        mailService.sendEmailFromTemplate(applicationUser.getInternalUser(),template,"TEST");
        mailService.sendEmail(applicationUser.getInternalUser().getEmail(),subject,content,false,true);
        return ResponseEntity.created(new URI("/api/application-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /application-users} : Updates an existing applicationUser.
     *
     * @param applicationUser the applicationUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated applicationUser,
     * or with status {@code 400 (Bad Request)} if the applicationUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the applicationUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/application-users")
    public ResponseEntity<ApplicationUser> updateApplicationUser(@Valid @RequestBody ApplicationUser applicationUser) throws URISyntaxException {
        log.debug("REST request to update ApplicationUser : {}", applicationUser);
        if (applicationUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        User user = applicationUser.getInternalUser();
        UserDTO userDTO = new UserDTO(user);
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
            userDTO.getLangKey(), userDTO.getImageUrl());
        ApplicationUser result = applicationUserService.save(applicationUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, applicationUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /application-users} : get all the applicationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applicationUsers in body.
     */
    @GetMapping("/application-users")
    public ResponseEntity<List<ApplicationUser>> getAllApplicationUsers(ApplicationUserCriteria criteria) {
        log.debug("REST request to get ApplicationUsers by criteria: {}", criteria);
        List<ApplicationUser> entityList = applicationUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /application-users/count} : count all the applicationUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/application-users/count")
    public ResponseEntity<Long> countApplicationUsers(ApplicationUserCriteria criteria) {
        log.debug("REST request to count ApplicationUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(applicationUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /application-users/:id} : get the "id" applicationUser.
     *
     * @param id the id of the applicationUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the applicationUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/application-users/{id}")
    public ResponseEntity<ApplicationUser> getApplicationUser(@PathVariable Long id) {
        log.debug("REST request to get ApplicationUser : {}", id);
        Optional<ApplicationUser> applicationUser = applicationUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(applicationUser);
    }

    /**
     * {@code DELETE  /application-users/:id} : delete the "id" applicationUser.
     *
     * @param id the id of the applicationUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/application-users/{id}")
    public ResponseEntity<Void> deleteApplicationUser(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationUser : {}", id);
        applicationUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
