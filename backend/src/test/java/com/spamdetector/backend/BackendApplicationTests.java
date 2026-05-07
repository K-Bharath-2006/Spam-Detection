package com.spamdetector.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.spamdetector.backend.repository.UserRepository;
import com.spamdetector.backend.repository.SpamCheckRepository;

@SpringBootTest
class BackendApplicationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SpamCheckRepository spamCheckRepository;

	@Test
	void contextLoads() {
	}

}
