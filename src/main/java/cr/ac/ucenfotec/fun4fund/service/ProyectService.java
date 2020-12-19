package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.*;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import cr.ac.ucenfotec.fun4fund.repository.FavoriteRepository;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
import cr.ac.ucenfotec.fun4fund.security.AuthoritiesConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Proyect}.
 */
@Service
@Transactional
public class ProyectService {

    private final Logger log = LoggerFactory.getLogger(ProyectService.class);

    private final ProyectRepository proyectRepository;

    private final FavoriteRepository favoriteRepository;

    private final MailService mailService;

    private final ApplicationUserRepository applicationUserRepository;

    private final UserService userService;

    public ProyectService(
        ProyectRepository proyectRepository,
        FavoriteRepository favoriteRepository,
        MailService mailService,
        ApplicationUserRepository applicationUserRepository,
        UserService userService
    ) {
        this.proyectRepository = proyectRepository;
        this.favoriteRepository = favoriteRepository;
        this.mailService = mailService;
        this.applicationUserRepository = applicationUserRepository;
        this.userService = userService;
    }

    /**
     * Save a proyect.
     *
     * @param proyect the entity to save.
     * @return the persisted entity.
     */
    public Proyect save(Proyect proyect) {
        log.debug("Request to save Proyect : {}", proyect);
        updateDate(proyect);
        return proyectRepository.save(proyect);
    }

    /**
     * Get all the proyects.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Proyect> findAll() {
        log.debug("Request to get all Proyects");
        return proyectRepository.findAll();
    }


    /**
     * Get one proyect by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Proyect> findOne(Long id) {
        log.debug("Request to get Proyect : {}", id);
        return proyectRepository.findById(id);
    }

    /**
     * Delete the proyect by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Proyect : {}", id);
        proyectRepository.deleteById(id);
    }

    public Proyect updateDate(Proyect proyect) {
        log.debug("Request to update date Proyect : {}", proyect);
        if(proyect.getId() != null){
            Optional<Proyect> getProyect = findOne(proyect.getId());
            if (getProyect.isPresent()){
                Proyect updateProyect = getProyect.get();
                updateProyect.setLastUpdated(ZonedDateTime.now());
                Proyect saved = proyectRepository.save(updateProyect);
                List<Favorite> favorites = favoriteRepository.findByProyect(saved);
                for (Favorite favorite: favorites) {
                    String subject = "El proyecto " + favorite.getProyect().getName() + " ha sido actualizado.";
                    String content = "El proyecto " + favorite.getProyect().getName() + " ha sido actualizado." +
                        "<br> Visítanos para ver cuales son las últimas actualizaciones";
                    String email = favorite.getUser().getInternalUser().getEmail();
                    mailService.sendEmail(email,subject,content,false,true);
                }
            }
        }
        return new Proyect();
    }

    public List<IProyectAnswerStatistics> getProyectStatusReport() {
        log.debug("Request to get all Proyects");
        Optional<ApplicationUser> owner = applicationUserRepository.findByInternalUserId(userService.getUserWithAuthorities().get().getId());
        return proyectRepository.getReportsProyectsStatus(owner.get());
    }

    public List<IProyectCompletedPercentile> getProyectCompletePercentile() {
        log.debug("Request to get all Proyects");
        Optional<ApplicationUser> owner = applicationUserRepository.findByInternalUserId(userService.getUserWithAuthorities().get().getId());
        return proyectRepository.getReportsProyectsComplete(owner.get());
    }

    public List<IProyectCategoryStatistics> getProyectCategoryReport() {
        log.debug("Request to get all Proyects");
        User user = userService.getUserWithAuthorities().get();
        Optional<ApplicationUser> owner = applicationUserRepository.findByInternalUserId(user.getId());

        if (owner.get().isAdmin()){
            return proyectRepository.getAllReportsProyectsCategory();
        } else {
            return proyectRepository.getReportsProyectsCategory(owner.get());
        }

    }
}
