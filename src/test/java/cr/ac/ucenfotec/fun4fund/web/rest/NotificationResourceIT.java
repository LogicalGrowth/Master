package cr.ac.ucenfotec.fun4fund.web.rest;

import cr.ac.ucenfotec.fun4fund.Fun4FundApp;
import cr.ac.ucenfotec.fun4fund.domain.Notification;
import cr.ac.ucenfotec.fun4fund.domain.ApplicationUser;
import cr.ac.ucenfotec.fun4fund.repository.NotificationRepository;
import cr.ac.ucenfotec.fun4fund.service.NotificationService;
import cr.ac.ucenfotec.fun4fund.service.dto.NotificationCriteria;
import cr.ac.ucenfotec.fun4fund.service.NotificationQueryService;

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

import cr.ac.ucenfotec.fun4fund.domain.enumeration.NotificationType;
import cr.ac.ucenfotec.fun4fund.domain.enumeration.NotificationStatus;
/**
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = Fun4FundApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final NotificationType DEFAULT_TYPE = NotificationType.APP;
    private static final NotificationType UPDATED_TYPE = NotificationType.EMAIL;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final NotificationStatus DEFAULT_STATUS = NotificationStatus.SEEN;
    private static final NotificationStatus UPDATED_STATUS = NotificationStatus.UNSEEN;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .type(DEFAULT_TYPE)
            .message(DEFAULT_MESSAGE)
            .timeStamp(DEFAULT_TIME_STAMP)
            .status(DEFAULT_STATUS);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .timeStamp(UPDATED_TIME_STAMP)
            .status(UPDATED_STATUS);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();
        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testNotification.getTimeStamp()).isEqualTo(DEFAULT_TIME_STAMP);
        assertThat(testNotification.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setType(null);

        // Create the Notification, which fails.


        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setMessage(null);

        // Create the Notification, which fails.


        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = notificationRepository.findAll().size();
        // set the field null
        notification.setTimeStamp(null);

        // Create the Notification, which fails.


        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.timeStamp").value(sameInstant(DEFAULT_TIME_STAMP)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }


    @Test
    @Transactional
    public void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotificationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type equals to DEFAULT_TYPE
        defaultNotificationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the notificationList where type equals to UPDATED_TYPE
        defaultNotificationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type not equals to DEFAULT_TYPE
        defaultNotificationShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the notificationList where type not equals to UPDATED_TYPE
        defaultNotificationShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultNotificationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the notificationList where type equals to UPDATED_TYPE
        defaultNotificationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type is not null
        defaultNotificationShouldBeFound("type.specified=true");

        // Get all the notificationList where type is null
        defaultNotificationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message equals to DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.equals=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.equals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message not equals to DEFAULT_MESSAGE
        defaultNotificationShouldNotBeFound("message.notEquals=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message not equals to UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.notEquals=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message in DEFAULT_MESSAGE or UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.in=" + DEFAULT_MESSAGE + "," + UPDATED_MESSAGE);

        // Get all the notificationList where message equals to UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.in=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message is not null
        defaultNotificationShouldBeFound("message.specified=true");

        // Get all the notificationList where message is null
        defaultNotificationShouldNotBeFound("message.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByMessageContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message contains DEFAULT_MESSAGE
        defaultNotificationShouldBeFound("message.contains=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message contains UPDATED_MESSAGE
        defaultNotificationShouldNotBeFound("message.contains=" + UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void getAllNotificationsByMessageNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where message does not contain DEFAULT_MESSAGE
        defaultNotificationShouldNotBeFound("message.doesNotContain=" + DEFAULT_MESSAGE);

        // Get all the notificationList where message does not contain UPDATED_MESSAGE
        defaultNotificationShouldBeFound("message.doesNotContain=" + UPDATED_MESSAGE);
    }


    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp equals to DEFAULT_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.equals=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp equals to UPDATED_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.equals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp not equals to DEFAULT_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.notEquals=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp not equals to UPDATED_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.notEquals=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp in DEFAULT_TIME_STAMP or UPDATED_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.in=" + DEFAULT_TIME_STAMP + "," + UPDATED_TIME_STAMP);

        // Get all the notificationList where timeStamp equals to UPDATED_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.in=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp is not null
        defaultNotificationShouldBeFound("timeStamp.specified=true");

        // Get all the notificationList where timeStamp is null
        defaultNotificationShouldNotBeFound("timeStamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp is greater than or equal to DEFAULT_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.greaterThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp is greater than or equal to UPDATED_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.greaterThanOrEqual=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp is less than or equal to DEFAULT_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.lessThanOrEqual=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp is less than or equal to SMALLER_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.lessThanOrEqual=" + SMALLER_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsLessThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp is less than DEFAULT_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.lessThan=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp is less than UPDATED_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.lessThan=" + UPDATED_TIME_STAMP);
    }

    @Test
    @Transactional
    public void getAllNotificationsByTimeStampIsGreaterThanSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where timeStamp is greater than DEFAULT_TIME_STAMP
        defaultNotificationShouldNotBeFound("timeStamp.greaterThan=" + DEFAULT_TIME_STAMP);

        // Get all the notificationList where timeStamp is greater than SMALLER_TIME_STAMP
        defaultNotificationShouldBeFound("timeStamp.greaterThan=" + SMALLER_TIME_STAMP);
    }


    @Test
    @Transactional
    public void getAllNotificationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status equals to DEFAULT_STATUS
        defaultNotificationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the notificationList where status equals to UPDATED_STATUS
        defaultNotificationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status not equals to DEFAULT_STATUS
        defaultNotificationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the notificationList where status not equals to UPDATED_STATUS
        defaultNotificationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultNotificationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the notificationList where status equals to UPDATED_STATUS
        defaultNotificationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllNotificationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where status is not null
        defaultNotificationShouldBeFound("status.specified=true");

        // Get all the notificationList where status is null
        defaultNotificationShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByApplicationUserIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        ApplicationUser applicationUser = ApplicationUserResourceIT.createEntity(em);
        em.persist(applicationUser);
        em.flush();
        notification.setApplicationUser(applicationUser);
        notificationRepository.saveAndFlush(notification);
        Long applicationUserId = applicationUser.getId();

        // Get all the notificationList where applicationUser equals to applicationUserId
        defaultNotificationShouldBeFound("applicationUserId.equals=" + applicationUserId);

        // Get all the notificationList where applicationUser equals to applicationUserId + 1
        defaultNotificationShouldNotBeFound("applicationUserId.equals=" + (applicationUserId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].timeStamp").value(hasItem(sameInstant(DEFAULT_TIME_STAMP))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .type(UPDATED_TYPE)
            .message(UPDATED_MESSAGE)
            .timeStamp(UPDATED_TIME_STAMP)
            .status(UPDATED_STATUS);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testNotification.getTimeStamp()).isEqualTo(UPDATED_TIME_STAMP);
        assertThat(testNotification.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
