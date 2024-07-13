package com.written.app;

//import org.mybatis.spring.annotation.MapperScan;

import com.written.app.model.Entry;
import com.written.app.model.List;
import com.written.app.model.Role;
import com.written.app.model.User;
import com.written.app.repository.EntryRepository;
import com.written.app.repository.ListRepository;
import com.written.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
//@MapperScan
//@EnableConfigurationProperties(JwtProperties.class)
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(
            EntryRepository entryRepository,
            UserRepository userRepository,
            ListRepository listRepository
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


            // dummy entry
            var entry = Entry.builder()
                    .isPrivate(false)
                    .createdAt(LocalDateTime.now())
                    .content("today\n" +
                            "lorem\n" +
                            "ipsum")
                    .title("title01")
                    .user(user)
                    .build();
            entryRepository.save(entry);

            var entry2 = Entry.builder()
                    .isPrivate(true)
                    .createdAt(LocalDateTime.now())
                    .content("hello" +
                            "world" +
                            "\n\n\n\nzz")
                    .title("Title 02")
                    .user(user)
                    .build();
            entryRepository.save(entry2);


            // dummy list
            var list = List.builder()
                    .title("To be more articulate")
                    .user(user2)
                    .build();
            listRepository.save(list);
        };
    }

}
