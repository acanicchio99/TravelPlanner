package com.academy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.academy.dto.SearchHistoryDto;
import com.academy.entity.SearchHistory;
import com.academy.entity.User;
import com.academy.exception.ResourceNotFoundException;
import com.academy.repository.SearchHistoryRepository;
import com.academy.repository.UserRepository;
import com.academy.security.CustomUserDetails;
import com.academy.security.CustomUserDetailsService;
import com.academy.security.JwtService;
import com.academy.service.SearchHistoryService;

// Keep a basic context load test
class FApplicationTests {

    @Test
    void contextLoads() {
    }
}

// Repository-level test
@DataJpaTest
class SearchHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Test
    void testFindByUserOrderBySearchedAtDesc() throws InterruptedException {
        User u1 = User.builder().username("user1").email("u1@example.com").password("password").build();
        userRepository.save(u1);

        SearchHistory s1 = SearchHistory.builder().user(u1).searchTerm("Rome").resultsCount(5).build();
        searchHistoryRepository.save(s1);
        Thread.sleep(10);
        SearchHistory s2 = SearchHistory.builder().user(u1).searchTerm("Paris").resultsCount(3).build();
        searchHistoryRepository.save(s2);

        User u2 = User.builder().username("user2").email("u2@example.com").password("password").build();
        userRepository.save(u2);
        SearchHistory s3 = SearchHistory.builder().user(u2).searchTerm("Berlin").resultsCount(2).build();
        searchHistoryRepository.save(s3);

        List<SearchHistory> results = searchHistoryRepository.findByUserOrderBySearchedAtDesc(u1);
        assertEquals(2, results.size());
        assertEquals("Paris", results.get(0).getSearchTerm());
        assertEquals("Rome", results.get(1).getSearchTerm());
    }
}

// Service unit tests (Mockito)
@ExtendWith(MockitoExtension.class)
class SearchHistoryServiceTest {

    @Mock
    private com.academy.repository.SearchHistoryRepository searchHistoryRepository;

    @Mock
    private com.academy.repository.UserRepository userRepository;

    @InjectMocks
    private SearchHistoryService searchHistoryService;

    @Test
    void testGetAllSearchHistorySuccess() {
        String username = "tester";
        User user = User.builder().id(1L).username(username).email("t@example.com").password("pwd").build();

        SearchHistory sh = SearchHistory.builder().id(10L).user(user).searchTerm("Rome").resultsCount(4).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(searchHistoryRepository.findByUserOrderBySearchedAtDesc(user)).thenReturn(List.of(sh));

        List<SearchHistoryDto> dtos = searchHistoryService.getAllSearchHistory(username);
        assertEquals(1, dtos.size());
        assertEquals("Rome", dtos.get(0).getSearchTerm());
    }

    @Test
    void testGetAllSearchHistoryUserNotFound() {
        String username = "missing";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> searchHistoryService.getAllSearchHistory(username));
    }
}

// Controller test (MockMvc)
@WebMvcTest(com.academy.controller.SearchHistoryController.class)
class SearchHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchHistoryService searchHistoryService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testGetAllSearchHistoryAuthenticated() throws Exception {
        String username = "webuser";
        SearchHistoryDto dto = SearchHistoryDto.builder().id(1L).searchTerm("Rome").searchedAt(LocalDateTime.now()).resultsCount(5).build();
        when(searchHistoryService.getAllSearchHistory(username)).thenReturn(List.of(dto));

        User user = User.builder().id(2L).username(username).email("w@example.com").password("pwd").build();
        CustomUserDetails cud = new CustomUserDetails(user);

        mockMvc.perform(get("/api/search-history").with(SecurityMockMvcRequestPostProcessors.user(cud)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].searchTerm").value("Rome"));
    }
}