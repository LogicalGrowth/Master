package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.PartnerRequest;
import cr.ac.ucenfotec.fun4fund.repository.PartnerRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PartnerRequest}.
 */
@Service
@Transactional
public class PartnerRequestService {

    private final Logger log = LoggerFactory.getLogger(PartnerRequestService.class);

    private final PartnerRequestRepository partnerRequestRepository;

    public PartnerRequestService(PartnerRequestRepository partnerRequestRepository) {
        this.partnerRequestRepository = partnerRequestRepository;
    }

    /**
     * Save a partnerRequest.
     *
     * @param partnerRequest the entity to save.
     * @return the persisted entity.
     */
    public PartnerRequest save(PartnerRequest partnerRequest) {
        log.debug("Request to save PartnerRequest : {}", partnerRequest);
        return partnerRequestRepository.save(partnerRequest);
    }

    /**
     * Get all the partnerRequests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PartnerRequest> findAll() {
        log.debug("Request to get all PartnerRequests");
        return partnerRequestRepository.findAll();
    }


    /**
     * Get one partnerRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PartnerRequest> findOne(Long id) {
        log.debug("Request to get PartnerRequest : {}", id);
        return partnerRequestRepository.findById(id);
    }

    /**
     * Delete the partnerRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PartnerRequest : {}", id);
        partnerRequestRepository.deleteById(id);
    }
}
