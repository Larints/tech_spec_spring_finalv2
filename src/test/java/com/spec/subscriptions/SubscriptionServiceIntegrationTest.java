package com.spec.subscriptions;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spec.subscriptions.dto.request.CreateSubscriptionRequest;
import com.spec.subscriptions.dto.response.TopSubscriptionDto;
import com.spec.subscriptions.model.Subscription;
import com.spec.subscriptions.model.User;
import com.spec.subscriptions.repository.SubscriptionRepository;
import com.spec.subscriptions.repository.UserRepository;
import com.spec.subscriptions.service.SubscriptionsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public class SubscriptionServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionsService subscriptionService;

    @Autowired
    private ObjectMapper mapper;

    private User user;

    @BeforeEach
    public void setUp() {

        user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user = userRepository.save(user);


        subscriptionRepository.deleteAll();
    }

    @Test
    public void testAddSubscriptionToUser() throws Exception {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest("Netflix");

        mockMvc.perform(post("/users/{userId}/subscriptions", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))  // Передаем тело запроса
                .andExpect(status().isOk())  // Ожидаем успешный ответ
                .andExpect(jsonPath("$.serviceName").value("Netflix"));  // Проверка, что подписка с именем Netflix

        Pageable pageable = PageRequest.of(0, 10);

        Page<Subscription> subscriptionsPage = subscriptionRepository.findByUserId(user.getId(), pageable);

        assertThat(subscriptionsPage.getTotalElements()).isEqualTo(1);  // Проверка, что подписок 5

        assertThat(subscriptionsPage.getContent().get(0).getServiceName()).isEqualTo("Netflix");

        assertThat(subscriptionsPage.getContent().get(0).getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    public void testGetUserSubscriptions() throws Exception {
        // Добавляем подписки для пользователя
        subscriptionService.addSubscriptionToUser(user.getId(), "Netflix");
        subscriptionService.addSubscriptionToUser(user.getId(), "Spotify");
        subscriptionService.addSubscriptionToUser(user.getId(), "YouTube");

        mockMvc.perform(get("/users/{userId}/subscriptions", user.getId())
                        .param("page", "0")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)))  // Проверка, что подписок на странице 2
                .andExpect(jsonPath("$.content[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$.content[1].serviceName").value("Spotify"));

        mockMvc.perform(get("/users/{userId}/subscriptions", user.getId())
                        .param("page", "1")
                        .param("size", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))  // На второй странице только 1 подписка
                .andExpect(jsonPath("$.content[0].serviceName").value("YouTube"));
    }


    @Test
    public void testDeleteSubscription() throws Exception {

        subscriptionService.addSubscriptionToUser(user.getId(), "Netflix");
        subscriptionService.addSubscriptionToUser(user.getId(), "Spotify");
        subscriptionService.addSubscriptionToUser(user.getId(), "YouTube");


        Subscription subscriptionToDelete = user.getSubscriptions().stream()
                .filter(sub -> sub.getServiceName().equals("Spotify"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Subscription not found"));

        mockMvc.perform(delete("/users/{userId}/subscriptions/{subscriptionId}", user.getId(), subscriptionToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Ожидаем успешный статус
                .andExpect(jsonPath("$.id").value(subscriptionToDelete.getId()))  // Проверяем, что id совпадает
                .andExpect(jsonPath("$.serviceName").value("Spotify"));  // Проверка, что имя подписки правильно
    }

    @Test
    public void testGetTopSubscriptions() throws Exception {

        for (int i = 0; i < 5; i++) {
            User user = new User();
            user.setName("NetflixUser" + i);
            user.setEmail("netflix" + i + "@example.com");
            userRepository.save(user);
            subscriptionService.addSubscriptionToUser(user.getId(), "Netflix");
        }

        for (int i = 0; i < 3; i++) {
            User user = new User();
            user.setName("SpotifyUser" + i);
            user.setEmail("spotify" + i + "@example.com");
            userRepository.save(user);
            subscriptionService.addSubscriptionToUser(user.getId(), "Spotify");
        }

        for (int i = 0; i < 2; i++) {
            User user = new User();
            user.setName("YouTubeUser" + i);
            user.setEmail("youtube" + i + "@example.com");
            userRepository.save(user);
            subscriptionService.addSubscriptionToUser(user.getId(), "YouTube");
        }

        User outsider = new User();
        outsider.setName("Bob");
        outsider.setEmail("bob@example.com");
        userRepository.save(outsider);
        subscriptionService.addSubscriptionToUser(outsider.getId(), "Discord");


        mockMvc.perform(get("/subscriptions/top")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))  // Проверка, что есть только 3 топ подписки
                .andExpect(jsonPath("$[0].serviceName").value("Netflix"))
                .andExpect(jsonPath("$[0].count").value(5))
                .andExpect(jsonPath("$[1].serviceName").value("Spotify"))
                .andExpect(jsonPath("$[1].count").value(3))
                .andExpect(jsonPath("$[2].serviceName").value("YouTube"))
                .andExpect(jsonPath("$[2].count").value(2));


        List<TopSubscriptionDto> topSubscriptions = subscriptionService.getTopSubscriptions();
        boolean hasDiscord = topSubscriptions.stream()
                .anyMatch(dto -> dto.serviceName().equals("Discord"));
        assertThat(hasDiscord).isFalse();
    }

}
