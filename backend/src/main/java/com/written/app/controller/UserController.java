package com.written.app.controller;

import com.written.app.dto.ChangeNickRequestDto;
import com.written.app.dto.ChangePasswordRequestDto;
import com.written.app.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /* user shouldn't see user list
    // @Hidden // hide from swagger
    @Operation(
            description = "Get endpoint for all user list",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping("/users/list")
    public List<User> findAllUser() {
        return userService.findAllUser();
    }*/

    /* findUser is not needed
    @GetMapping("/users")
    public User findUserByPrincipal(
            Principal connectedUser
    ) {
        return userService.findUserByPrincipal(connectedUser);
    }*/


    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Principal connectedUser) {
        userService.delete(connectedUser);
    }

    @PatchMapping("/users/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDto request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/users/nick")
    public ResponseEntity<?> changeNick(
            @RequestBody ChangeNickRequestDto dto,
            Principal connectedUser
    ) {
        userService.changeNick(dto, connectedUser);
        return ResponseEntity.ok().build();
    }


    // if i just create a @Getmapping("/valid") here and send Status.ok(),
    // won't spring security do the validity checking with its filters?
    // i don't necessarily have to code the service or anything as the filters and stuff are set up already.
    @GetMapping("/users/valid-token")
    public ResponseEntity<String> validateToken() {
        // if this point is reached, means the token is valid
        // Spring Security filters have already validated it
        return ResponseEntity.ok("Token is valid");
    }

}
