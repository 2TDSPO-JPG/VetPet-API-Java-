package com.challenger.sprint.javaspg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties={"spring.profiles.active=local", "spring.datasource.url=jdbc:h2:mem:context;DB_CLOSE_DELAY=-1"})
class JavaspgApplicationTests {

	@Test
	void contextLoads() {
	}

}
