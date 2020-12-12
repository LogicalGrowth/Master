package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.AuctionAnswerStatistics;
import cr.ac.ucenfotec.fun4fund.domain.Raffle;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Raffle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RaffleRepository extends JpaRepository<Raffle, Long>, JpaSpecificationExecutor<Raffle> {
    @Query(nativeQuery = true, value =
        "SELECT Months.id as month, " +
            "   year(r.expiration_date) as year, " +
            "   COUNT(r.expiration_date) as count " +
            "   FROM (SELECT 1 as ID UNION SELECT 2 as ID UNION  SELECT 3 as ID UNION SELECT 4 as ID UNION " +
            "     SELECT 5 as ID UNION SELECT 6 as ID UNION SELECT 7 as ID UNION SELECT 8 as ID " +
            "     UNION  " +
            "     SELECT 9 as ID UNION SELECT 10 as ID UNION SELECT 11 as ID UNION SELECT 12 as ID " +
            "   ) as Months " +
            "   LEFT JOIN Raffle as r on Months.id = month(r.expiration_date) " +
            "   AND r.state = 'FINISHED' " +
            "   AND MONTH(r.expiration_date) in (?2) " +
            "   AND YEAR(r.expiration_date) in (?3) " +
            "   AND r.buyer_id = ?1 " +
            "   WHERE Months.id in (?2) " +
            "   GROUP BY Months.id " +
            "   ORDER BY Months.id, year ASC")
    List<AuctionAnswerStatistics> getWinnerRaffleByMonth(ApplicationUser winner, List<Integer> months, List<Integer> years);

    @Query(nativeQuery = true, value =
    "SELECT Months.id as month, " +
        "   year(r.expiration_date) as year, " +
        "   COUNT(t.id) as count " +
        "   FROM (SELECT 1 as ID UNION SELECT 2 as ID UNION  SELECT 3 as ID UNION SELECT 4 as ID UNION " +
        "     SELECT 5 as ID UNION SELECT 6 as ID UNION SELECT 7 as ID UNION SELECT 8 as ID " +
        "     UNION  " +
        "     SELECT 9 as ID UNION SELECT 10 as ID UNION SELECT 11 as ID UNION SELECT 12 as ID " +
        "   ) as Months " +
        "   LEFT JOIN Raffle as r on Months.id = month(r.expiration_date) " +
        "   left JOIN Ticket as t on r.id = t.raffle_id " +
        "   AND MONTH(r.expiration_date) in (?2) " +
        "   AND YEAR(r.expiration_date) in (?3) " +
        "   AND t.buyer_id = ?1 " +
        "   WHERE Months.id in (?2) " +
        "   GROUP BY Months.id " +
        "   ORDER BY Months.id, year ASC")
    List<AuctionAnswerStatistics> getRaffleParticipate(ApplicationUser buyer, List<Integer> months, List<Integer> years);
}
