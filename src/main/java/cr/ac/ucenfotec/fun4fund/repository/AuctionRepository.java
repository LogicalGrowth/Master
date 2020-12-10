package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Auction;

import cr.ac.ucenfotec.fun4fund.domain.AuctionAnswerStatistics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Auction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long>, JpaSpecificationExecutor<Auction> {

    @Query(nativeQuery = true, value =
        "SELECT Months.id as month, " +
            "year(a.expiration_date) as year, " +
            "COUNT(a.expiration_date) as count " +
            "FROM (SELECT 1 as ID UNION SELECT 2 as ID UNION  SELECT 3 as ID UNION SELECT 4 as ID UNION " +
            "  SELECT 5 as ID UNION SELECT 6 as ID UNION SELECT 7 as ID UNION SELECT 8 as ID " +
            "  UNION  " +
            "  SELECT 9 as ID UNION SELECT 10 as ID UNION SELECT 11 as ID UNION SELECT 12 as ID " +
            ") as Months " +
            "LEFT JOIN Auction as a on Months.id = month(a.expiration_date) " +
            "AND a.state = 'FINISHED' " +
            "AND MONTH(a.expiration_date) in (?2) " +
            "AND YEAR(a.expiration_date) in (?3) " +
            "AND a.winner_id = ?1 " +
            "WHERE Months.id in (?2) " +
            "GROUP BY Months.id " +
            "ORDER BY Months.id, year ASC")
    List<AuctionAnswerStatistics> getWinnerAuctionsByMonth(ApplicationUser winner, List<Integer> months, List<Integer> years);
}
