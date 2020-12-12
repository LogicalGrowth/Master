package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.AuctionAnswerStatistics;
import cr.ac.ucenfotec.fun4fund.domain.Raffle;
import cr.ac.ucenfotec.fun4fund.repository.ApplicationUserRepository;
import cr.ac.ucenfotec.fun4fund.repository.RaffleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Raffle}.
 */
@Service
@Transactional
public class RaffleService {

    private final Logger log = LoggerFactory.getLogger(RaffleService.class);

    private final RaffleRepository raffleRepository;

    private final ApplicationUserRepository applicationUserRepository;

    private final UserService userService;

    public RaffleService(
        RaffleRepository raffleRepository,
        ApplicationUserRepository applicationUserRepository,
        UserService userService
    ) {
        this.raffleRepository = raffleRepository;
        this.applicationUserRepository = applicationUserRepository;
        this.userService = userService;
    }

    /**
     * Save a raffle.
     *
     * @param raffle the entity to save.
     * @return the persisted entity.
     */
    public Raffle save(Raffle raffle) {
        log.debug("Request to save Raffle : {}", raffle);
        return raffleRepository.save(raffle);
    }

    /**
     * Get all the raffles.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Raffle> findAll() {
        log.debug("Request to get all Raffles");
        return raffleRepository.findAll();
    }


    /**
     * Get one raffle by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Raffle> findOne(Long id) {
        log.debug("Request to get Raffle : {}", id);
        return raffleRepository.findById(id);
    }

    /**
     * Delete the raffle by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Raffle : {}", id);
        raffleRepository.deleteById(id);
    }
    public List<List<AuctionAnswerStatistics>> getDataRaffle(int numberMonths){
        List<Integer> months = new ArrayList<Integer>();
        List<Integer> years = new ArrayList<Integer>();
        LocalDate actual = LocalDate.now();
        months.add(actual.getMonthValue());
        years.add(actual.getYear());
        for (int i = 0; i < numberMonths - 1; i++) {
            actual = actual.minusMonths(1);
            months.add(actual.getMonthValue());
            years.add(actual.getYear());
        }
        Optional<ApplicationUser> winner = applicationUserRepository.findByInternalUserId(userService.getUserWithAuthorities().get().getId());
        List<List<AuctionAnswerStatistics>> raffleData = new ArrayList<>();
        raffleData.add(raffleRepository.getRaffleParticipate(winner.get(), months, years));
        raffleData.add(raffleRepository.getWinnerRaffleByMonth(winner.get(), months, years));

        return raffleData;
    }
}
