package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ProyectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ProyectService(ProyectRepository proyectRepository) {
        this.proyectRepository = proyectRepository;
    }

    /**
     * Save a proyect.
     *
     * @param proyect the entity to save.
     * @return the persisted entity.
     */
    public Proyect save(Proyect proyect) {
        log.debug("Request to save Proyect : {}", proyect);
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
}
