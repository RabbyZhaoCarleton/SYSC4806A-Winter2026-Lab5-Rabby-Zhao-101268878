package org.carleton;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point. @SpringBootApplication turns on component scanning for this package,
 * so the @RestController / @Controller classes are discovered automatically, and
 * spring-boot-starter-web starts an embedded Tomcat server on port 8080.
 *
 * The Lab 3 CommandLineRunner that seeded demo data was removed: in Lab 4 the
 * address books are populated through HTTP requests (see requests.http / requests.txt).
 */
@SpringBootApplication
public class AddressBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressBookApplication.class, args);
    }
}
