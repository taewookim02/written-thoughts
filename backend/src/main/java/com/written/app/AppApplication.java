package com.written.app;

//import org.mybatis.spring.annotation.MapperScan;

import com.github.javafaker.Faker;
import com.written.app.model.*;
import com.written.app.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
//@MapperScan
//@EnableConfigurationProperties(JwtProperties.class)
//@EnableWebMvc
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }


//    @Bean
    public CommandLineRunner fakerRunner(
            EntryRepository entryRepository,
            UserRepository userRepository
    ) {
        return args -> {
            // get user with the id 2
            User user = userRepository.findById(2)
                    .orElseThrow(() -> new EntityNotFoundException("Entity not found"));
            // 50 public entries
            for (int i = 0; i < 50; i++) {
                Faker faker = new Faker(); // fake data generator
                Entry entry = Entry.builder()
                        .title(faker.beer().name())
                        .content(faker.rickAndMorty().quote())
                        .user(user)
                        .isPublic(true)
                        .build();
                entryRepository.save(entry);

                try {
                    // sleep 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread was interrupted during sleep: " + e.getMessage());
                    break;
                }
            }

        };
    }

//    @Bean
    public CommandLineRunner commandLineRunner(
            EntryRepository entryRepository,
            UserRepository userRepository,
            ListRepository listRepository,
            ListItemRepository listItemRepository,
            LabelRepository labelRepository
    ) {
        return args -> {
            // dummy user
            var user = User.builder()
                    .email("helloworld@gmail.com")
                    .password("12341234")
                    .role(Role.USER)
                    .nick("")
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);

            var user2 = User.builder()
                    .email("tata@gmail.com")
                    .password("123123123")
                    .role(Role.USER)
                    .nick("")
                    .isDeleted(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user2);

            // dummy label
            var label = Label.builder()
                    .user(user2)
                    .name("Daily journal")
                    .build();
            labelRepository.save(label);


            // dummy entry
            var entry = Entry.builder()
                    .isPublic(false)
                    .createdAt(LocalDateTime.now())
                    .content("today\n" +
                            "lorem\n" +
                            "ipsum")
                    .title("title01")
                    .user(user)
                    .build();
            entryRepository.save(entry);

            var entry2 = Entry.builder()
                    .isPublic(true)
                    .createdAt(LocalDateTime.now())
                    .content("hello" +
                            "world" +
                            "\n\n\n\nzz")
                    .title("Title 02")
                    .user(user2)
                    .label(label)
                    .build();
            entryRepository.save(entry2);


            // dummy list
            var list = List.builder()
                    .title("To be more articulate")
                    .user(user2)
                    .build();
            listRepository.save(list);


            // dummy list item
            var listItem = ListItem.builder()
                    .list(list)
                    .content("hello world from list item")
                    .build();
            listItemRepository.save(listItem);


            var listItem2 = ListItem.builder()
                    .list(list)
                    .content("second list item from list \n" +
                            "after line break")
                    .build();
            listItemRepository.save(listItem2);




        };
    }

}
