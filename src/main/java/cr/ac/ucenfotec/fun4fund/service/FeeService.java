package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.Fee;
import cr.ac.ucenfotec.fun4fund.repository.FeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Fee}.
 */
@Service
@Transactional
public class FeeService {

    private final Logger log = LoggerFactory.getLogger(FeeService.class);

    private final FeeRepository feeRepository;

    public FeeService(FeeRepository feeRepository) {
        this.feeRepository = feeRepository;
    }

    /**
     * Save a fee.
     *
     * @param fee the entity to save.
     * @return the persisted entity.
     */
    public Fee save(Fee fee) {
        log.debug("Request to save Fee : {}", fee);
        return feeRepository.save(fee);
    }

    /**
     * Get all the fees.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Fee> findAll() {
        log.debug("Request to get all Fees");
        return feeRepository.findAll();
    }


    /**
     * Get one fee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Fee> findOne(Long id) {
        log.debug("Request to get Fee : {}", id);
        return feeRepository.findById(id);
    }

    /**
     * Delete the fee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Fee : {}", id);
        feeRepository.deleteById(id);
    }
}
