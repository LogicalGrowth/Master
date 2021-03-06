package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.*;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.ProductType;
import cr.ac.ucenfotec.fun4fund.repository.CheckpointRepository;
import cr.ac.ucenfotec.fun4fund.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final CheckpointRepository checkpointRepository;

    private final CheckpointService checkpointService;

    private final MailService mailService;

    private final ApplicationUserService applicationUserService;

    private final ProyectService proyectService;

    private final ConfigSystemService configSystemService;

    public PaymentService(
        PaymentRepository paymentRepository,
        CheckpointRepository checkpointRepository, CheckpointService checkpointService, MailService mailService,
        ApplicationUserService applicationUserService,
        ProyectService proyectService,
        ConfigSystemService configSystemService
    ) {
        this.paymentRepository = paymentRepository;
        this.checkpointRepository = checkpointRepository;
        this.checkpointService = checkpointService;
        this.mailService = mailService;
        this.applicationUserService = applicationUserService;
        this.proyectService = proyectService;
        this.configSystemService = configSystemService;
    }

    /**
     * Save a payment.
     *
     * @param payment the entity to save.
     * @return the persisted entity.
     */
    public Payment save(Payment payment) {
        log.debug("Request to save Payment : {}", payment);
        return paymentRepository.save(payment);
    }

    /**
     * Get all the payments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        log.debug("Request to get all Payments");
        return paymentRepository.findAll();
    }


    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Payment> findOne(Long id) {
        log.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    public Payment makePayment(Payment payment){
        Optional<ApplicationUser> applicationUser = applicationUserService.findOne(payment.getApplicationUser().getId());

        Proyect proyect = proyectService.findOne(payment.getProyect().getId()).get();

        Optional<ApplicationUser> proyectOwner = applicationUserService.findOne(proyect.getOwner().getId());

        Double fee = payment.getAmount() * proyect.getFee() / 100;

        proyect.setCollected(proyect.getCollected() + payment.getAmount() - fee);

        Double percentageCollected = (proyect.getCollected() + payment.getAmount() - fee) * 100 / proyect.getGoalAmount();

        List<Checkpoint> achieved = new ArrayList<>();

        List<Checkpoint> checkpoint = checkpointRepository.findByProyectIdAndCompletitionPercentageLessThanEqual(proyect.getId(), percentageCollected, Sort.unsorted());
        for (Checkpoint item: checkpoint) {
            if(!item.isCompleted()) {
                item.setCompleted(true);
                checkpointService.save(item);

                achieved.add(item);
            }
        }

        String type = "";

        if(payment.getType() == ProductType.AUCTION){
            type = "la puja de $" + payment.getAmount() + " de la subasta en el proyecto " + proyect.getName();
        } else if(payment.getType() == ProductType.DONATION){
            type = "la donación de $" + payment.getAmount() +" al proyecto " + proyect.getName();

            String msgSubject = "Donación recibida";
            String msg = "Ha recibido una donación de $" + payment.getAmount() +" en el proyecto " + proyect.getName() + ".";
            mailService.sendEmail(proyectOwner.get().getInternalUser().getEmail(),msgSubject,msg,false,true);

        }else if(payment.getType() == ProductType.EXCLUSIVE_CONTENT){
            type = "la compra de contenido exlusivo en el proyecto " + proyect.getName() + "monto final: $" + payment.getAmount();
        }else if(payment.getType() == ProductType.PARTNERSHIP){
            type = "la solicitud de socio de $" + payment.getAmount() +" al proyecto " + proyect.getName();

            String msgSubject = "Solicitud de socio aprobada";
            String msg = "Ha recibido un dinero de $" + payment.getAmount() +" en el proyecto " + proyect.getName() + ".";
            mailService.sendEmail(proyectOwner.get().getInternalUser().getEmail(),msgSubject,msg,false,true);
        }else if(payment.getType() == ProductType.RAFFLE){
            type = "participar en la rifa del proyecto " + proyect.getName()+ "el monto final fue de: $" + payment.getAmount();
        }
        String subject = "Recibido de pago";

        String content = "Muchas gracias por " + type;
        mailService.sendEmail(applicationUser.get().getInternalUser().getEmail(),subject,content,false,true);

        if(!achieved.isEmpty()) {

            for (Checkpoint item: achieved) {
                mailService.sendEmail(proyectOwner.get().getInternalUser().getEmail(),"Checkpoint alcanzado",
                    "¡Felicidades! Se ha alcanzado el checkpoint del " + item.getCompletitionPercentage() + "% para el proyecto " + proyect.getName() + ".",false,true);
            }
        }

        Proyect proyectResult = proyectService.save(proyect);
        Payment result = save(payment);

        ConfigSystem config = configSystemService.findByType("FeeValue").get(0);
        Double value = (Double.parseDouble(config.getValue()) + fee);
        config.setValue(value.toString());
        configSystemService.save(config);

        return result;
    }
}
