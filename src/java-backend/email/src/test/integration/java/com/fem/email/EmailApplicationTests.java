package com.fem.email;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.kafka.enabled=false",
    "spring.kafka.bootstrap-servers=localhost:0"
})
class EmailApplicationTests {

	@Test
	void contextLoads() {
	}

}
