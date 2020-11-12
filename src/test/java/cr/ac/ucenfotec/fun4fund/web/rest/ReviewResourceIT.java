package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Review;
import cr.ac.ucenfotec.fun4fund.domain.Proyect;
import cr.ac.ucenfotec.fun4fund.repository.ReviewRepository;
import cr.ac.ucenfotec.fun4fund.service.ReviewService;
import cr.ac.ucenfotec.fun4fund.service.dto.ReviewCriteria;
import cr.ac.ucenfotec.fun4fund.service.ReviewQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static cr.ac.ucenfotec.fun4fund.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReviewResourceIT {

    private static final ZonedDateTime DEFAULT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_USER = "AAAAAAAAAA";
    private static final String UPDATED_USER = "BBBBBBBBBB";

    private static final Double DEFAULT_RATING = 1D;
    private static final Double UPDATED_RATING = 2D;
    private static final Double SMALLER_RATING = 1D - 1D;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewQueryService reviewQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReviewMockMvc;

    private Review review;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity(EntityManager em) {
        Review review = new Review()
            .timeStamp(DEFAULT_TIME_STAMP)
            .message(DEFAULT_MESSAGE)
            .user(DEFAULT_USER)
            .rating(DEFAULT_RATING);
        return review;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity(EntityManager em) {
        Review review = new Review()
            .timeStamp(UPDATED_TIME_STAMP)
            .message(UPDATED_MESSAGE)
            .user(UPDATED_USER)
            .rating(UPDATED_RATING);
        return review;
    }

    @BeforeEach
    public void initTest() {
        review = createEntity(em);
    }

    @Test
    @Transactional
    public void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();
        // Create the Review
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isCreated());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
        assertThat(testReview.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testReview.getUser()).isEqualTo(DEFAULT_USER);
        assertThat(testReview.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    public void createReviewWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().size();

        // Create the Review with an existing ID
        review.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setTimeStamp(null);

        // Create the Review, which fails.


        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setMessage(null);

        // Create the Review, which fails.


        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setUser(null);

        // Create the Review, which fails.


        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().size();
        // set the field null
        review.setRating(null);

        // Create the Review, which fails.


        restReviewMockMvc.perform(post("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllReviews() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getReview() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", review.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(review.getId().intValue()))
            .andExpect(jsonPath("$.timeStamp").value(sameInstant(DEFAULT_TIME_STAMP)))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.user").value(DEFAULT_USER))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.doubleValue()));
    }


    @Test
    @Transactional
    public void getReviewsByIdFiltering() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        Long id = review.getId();

        defaultReviewShouldBeFound("id.equals=" + id);
        defaultReviewShouldNotBeFound("id.notEquals=" + id);

        defaultReviewShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultReviewShouldNotBeFound("id.greaterThan=" + id);

        defaultReviewShouldBeFound("id.lessThanOrEqual=" + id);
        defaultReviewShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp equals to DEFAULT_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.equals=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp equals to UPDATED_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.equals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp not equals to DEFAULT_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.notEquals=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp not equals to UPDATED_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.notEquals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp in DEFAULT_TIME_STAMP or UPDATED_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.in=" + DEFAULT_TIME_STAMP + "," + UPDATED_TIME_STAMP);

        // Get all the reviewList where timeStamp equals to UPDATED_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.in=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp is not null
        defaultReviewShouldBeFound("timeStamp.specified=true");

        // Get all the reviewList where timeStamp is null
        defaultReviewShouldNotBeFound("timeStamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp is greater than or equal to DEFAULT_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.greaterThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp is greater than or equal to UPDATED_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.greaterThanOrEqual=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp is less than or equal to DEFAULT_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.lessThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp is less than or equal to SMALLER_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.lessThanOrEqual=" + SMALLER_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp is less than DEFAULT_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.lessThan=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp is less than UPDATED_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.lessThan=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllReviewsByTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where timeStamp is greater than DEFAULT_TIME_STAMP
        defaultReviewShouldNotBeFound("timeStamp.greaterThan=" + DEFAULT_TIME_STAMP);

        // Get all the reviewList where timeStamp is greater than SMALLER_TIME_STAMP
        defaultReviewShouldBeFound("timeStamp.greaterThan=" + SMALLER_TIME_STAMP);
    }


    @Test
    @Transactional
    public void getAllReviewsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message equals to DEFAULT_MESSAGE
        defaultReviewShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the reviewList where message equals to UPDATED_MESSAGE
        defaultReviewShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message not equals to DEFAULT_MESSAGE
        defaultReviewShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the reviewList where message not equals to UPDATED_MESSAGE
        defaultReviewShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultReviewShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the reviewList where message equals to UPDATED_MESSAGE
        defaultReviewShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message is not null
        defaultReviewShouldBeFound("message.specified=true");

        // Get all the reviewList where message is null
        defaultReviewShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllReviewsByMessageContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message contains DEFAULT_MESSAGE
        defaultReviewShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the reviewList where message contains UPDATED_MESSAGE
        defaultReviewShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllReviewsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where message does not contain DEFAULT_MESSAGE
        defaultReviewShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the reviewList where message does not contain UPDATED_MESSAGE
        defaultReviewShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllReviewsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user equals to DEFAULT_USER
        defaultReviewShouldBeFound("user.equals=" + DEFAULT_USER);

        // Get all the reviewList where user equals to UPDATED_USER
        defaultReviewShouldNotBeFound("user.equals=" + UPDATED_USER);
    }

    @Test
    @Transactional
    public void getAllReviewsByUserIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user not equals to DEFAULT_USER
        defaultReviewShouldNotBeFound("user.notEquals=" + DEFAULT_USER);

        // Get all the reviewList where user not equals to UPDATED_USER
        defaultReviewShouldBeFound("user.notEquals=" + UPDATED_USER);
    }

    @Test
    @Transactional
    public void getAllReviewsByUserIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user in DEFAULT_USER or UPDATED_USER
        defaultReviewShouldBeFound("user.in=" + DEFAULT_USER + "," + UPDATED_USER);

        // Get all the reviewList where user equals to UPDATED_USER
        defaultReviewShouldNotBeFound("user.in=" + UPDATED_USER);
    }

    @Test
    @Transactional
    public void getAllReviewsByUserIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user is not null
        defaultReviewShouldBeFound("user.specified=true");

        // Get all the reviewList where user is null
        defaultReviewShouldNotBeFound("user.specified=false");
    }
                @Test
    @Transactional
    public void getAllReviewsByUserContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user contains DEFAULT_USER
        defaultReviewShouldBeFound("user.contains=" + DEFAULT_USER);

        // Get all the reviewList where user contains UPDATED_USER
        defaultReviewShouldNotBeFound("user.contains=" + UPDATED_USER);
    }

    @Test
    @Transactional
    public void getAllReviewsByUserNotContainsSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where user does not contain DEFAULT_USER
        defaultReviewShouldNotBeFound("user.doesNotContain=" + DEFAULT_USER);

        // Get all the reviewList where user does not contain UPDATED_USER
        defaultReviewShouldBeFound("user.doesNotContain=" + UPDATED_USER);
    }


    @Test
    @Transactional
    public void getAllReviewsByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating equals to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating not equals to DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the reviewList where rating not equals to UPDATED_RATING
        defaultReviewShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultReviewShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the reviewList where rating equals to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is not null
        defaultReviewShouldBeFound("rating.specified=true");

        // Get all the reviewList where rating is null
        defaultReviewShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than or equal to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the reviewList where rating is greater than or equal to UPDATED_RATING
        defaultReviewShouldNotBeFound("rating.greaterThanOrEqual=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than or equal to DEFAULT_RATING
        defaultReviewShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the reviewList where rating is less than or equal to SMALLER_RATING
        defaultReviewShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is less than DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating is less than UPDATED_RATING
        defaultReviewShouldBeFound("rating.lessThan=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllReviewsByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);

        // Get all the reviewList where rating is greater than DEFAULT_RATING
        defaultReviewShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the reviewList where rating is greater than SMALLER_RATING
        defaultReviewShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllReviewsByProyectIsEqualToSomething() throws Exception {
        // Initialize the database
        reviewRepository.saveAndFlush(review);
        Proyect proyect = ProyectResourceIT.createEntity(em);
        em.persist(proyect);
        em.flush();
        review.setProyect(proyect);
        reviewRepository.saveAndFlush(review);
        Long proyectId = proyect.getId();

        // Get all the reviewList where proyect equals to proyectId
        defaultReviewShouldBeFound("proyectId.equals=" + proyectId);

        // Get all the reviewList where proyect equals to proyectId + 1
        defaultReviewShouldNotBeFound("proyectId.equals=" + (proyectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReviewShouldBeFound(String filter) throws Exception {
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(review.getId().intValue())))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].user").value(hasItem(DEFAULT_USER)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.doubleValue())));

        // Check, that the count call also returns 1
        restReviewMockMvc.perform(get("/api/reviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReviewShouldNotBeFound(String filter) throws Exception {
        restReviewMockMvc.perform(get("/api/reviews?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReviewMockMvc.perform(get("/api/reviews/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingReview() throws Exception {
        // Get the review
        restReviewMockMvc.perform(get("/api/reviews/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReview() throws Exception {
        // Initialize the database
        reviewService.save(review);

        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).get();
        // Disconnect from session so that the updates on updatedReview are not directly saved in db
        em.detach(updatedReview);
        updatedReview
            .timeStamp(UPDATED_TIME_STAMP)
            .message(UPDATED_MESSAGE)
            .user(UPDATED_USER)
            .rating(UPDATED_RATING);

        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReview)))
            .andExpect(status().isOk());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
        assertThat(testReview.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testReview.getUser()).isEqualTo(UPDATED_USER);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    public void updateNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReviewMockMvc.perform(put("/api/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(review)))
            .andExpect(status().isBadRequest());

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReview() throws Exception {
        // Initialize the database
        reviewService.save(review);

        int databaseSizeBeforeDelete = reviewRepository.findAll().size();

        // Delete the review
        restReviewMockMvc.perform(delete("/api/reviews/{id}", review.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Review> reviewList = reviewRepository.findAll();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
