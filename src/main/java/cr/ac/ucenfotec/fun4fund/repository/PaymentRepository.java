package cr.ac.ucenfotec.fun4fund.repository;

import cr.ac.ucenfotec.fun4fund.domain.Payment;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, PagingAndSortingRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    @Query("SELECT sum(p.amount) as amount, p.applicationUser " +
        "FROM Payment p " +
        "WHERE p.proyect.id = ?1 " +
        "group by p.applicationUser " +
        "order by amount desc")
    List<?> findTop5ByProyectId(Long proyectId, Pageable pageable);
}
