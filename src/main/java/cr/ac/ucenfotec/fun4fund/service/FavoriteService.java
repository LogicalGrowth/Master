package cr.ac.ucenfotec.fun4fund.service;

import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.domain.Favorite;
import cr.ac.ucenfotec.fun4fund.domain.IProyectCompletedPercentile;
import cr.ac.ucenfotec.fun4fund.domain.ITopFavorites;
import cr.ac.ucenfotec.fun4fund.repository.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Favorite}.
 */
@Service
@Transactional
public class FavoriteService {

    private final Logger log = LoggerFactory.getLogger(FavoriteService.class);

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * Save a favorite.
     *
     * @param favorite the entity to save.
     * @return the persisted entity.
     */
    public Favorite save(Favorite favorite) {
        log.debug("Request to save Favorite : {}", favorite);
        return favoriteRepository.save(favorite);
    }

    /**
     * Get all the favorites.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Favorite> findAll() {
        log.debug("Request to get all Favorites");
        return favoriteRepository.findAll();
    }


    /**
     * Get one favorite by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Favorite> findOne(Long id) {
        log.debug("Request to get Favorite : {}", id);
        return favoriteRepository.findById(id);
    }

    /**
     * Delete the favorite by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Favorite : {}", id);
        favoriteRepository.deleteById(id);
    }

    public List<ITopFavorites> getTop5Favorites() {
        log.debug("Request to get top 5 favorites");
        return favoriteRepository.getTopFavorites();
    }
}
