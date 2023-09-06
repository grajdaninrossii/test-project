package com.miro.testproject.controller;

import com.miro.testproject.pojo.*;
import com.miro.testproject.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600) // Работа с CORS
@RequiredArgsConstructor
public class AuthController {

//    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authUser(@RequestBody JwtRequest loginRequest) {
        JwtResponse userData = authService.getUserData(loginRequest);
        if (userData.getAccessToken() == null){
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid username or password"));
        }
        return ResponseEntity.ok(userData);
    }


    /** Регистрация пользователя
     *
     * @param signupRequest - JSON(Объект) вида {
     *     "username": String,
     *     "password": String,
     *     "roles": List<Role> (в виде json, например, список из всех доступных ролей: ["admin", "mod", "user"])
     * }
     * @return в случае успеха вернет "User CREATED" иначе 4** ошибку
     */
    // Создание пользователя
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {

        List<String> result = authService.addUser(signupRequest);
        MessageResponse message = new MessageResponse(result.get(1));
        if ("bad".equals(result.get(0))){
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.ok(message);
    }


    @GetMapping("/test")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello");
    }



}
