package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.PaymentMethod;
import cr.ac.ucenfotec.fun4fund.repository.PaymentMethodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PaymentMethod}.
 */
@Service
@Transactional
public class PaymentMethodService {

    private final Logger log = LoggerFactory.getLogger(PaymentMethodService.class);

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    /**
     * Save a paymentMethod.
     *
     * @param paymentMethod the entity to save.
     * @return the persisted entity.
     */
    public PaymentMethod save(PaymentMethod paymentMethod) {
        log.debug("Request to save PaymentMethod : {}", paymentMethod);
        return paymentMethodRepository.save(paymentMethod);
    }

    /**
     * Get all the paymentMethods.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentMethod> findAll() {
        log.debug("Request to get all PaymentMethods");
        return paymentMethodRepository.findAll();
    }


    /**
     * Get one paymentMethod by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentMethod> findOne(Long id) {
        log.debug("Request to get PaymentMethod : {}", id);
        return paymentMethodRepository.findById(id);
    }

    /**
     * Delete the paymentMethod by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PaymentMethod : {}", id);
        paymentMethodRepository.deleteById(id);
    }
}
