package com.written.app.controller;

import com.written.app.dto.ChangePasswordRequestDto;
import com.written.app.model.User;
import com.written.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // user list
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
    }

    // user by id
    @GetMapping("/users/{id}")
    // @Hidden // hide from swagger
    public User findUserById(
            @PathVariable Integer id
    ) {
        return userService.findUserById(id);
    }


    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Integer id) {
        // TODO: check if userId matches
        userService.deleteById(id);
    }

    @PatchMapping("/users/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequestDto request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

}
