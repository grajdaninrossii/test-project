package com.miro.testproject.controller;

import com.miro.testproject.pojo.MessageResponse;
import com.miro.testproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600) // Работа с CORS
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    /** Удалить пользователя из бд
     *
     * @return вернет сообщение с положительным результатом, иначе косяк сервака
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER') or HasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(){
        String message = userService.deleteUser();
        return ResponseEntity.ok(new MessageResponse(message));
    }
}
