package com.miro.testproject.service;


import com.miro.testproject.config.jwt.JwtUtils;
import com.miro.testproject.entity.ERole;
import com.miro.testproject.entity.Role;
import com.miro.testproject.entity.User;
import com.miro.testproject.pojo.JwtRequest;
import com.miro.testproject.pojo.JwtResponse;
import com.miro.testproject.pojo.SignupRequest;
import com.miro.testproject.repository.UserRepository;
import com.miro.testproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    Authentication auth;

    public User getAuthUser(){
        auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(auth.getName()).orElseThrow(() -> new RuntimeException("Error, User is not found!))"));
    }


    public JwtResponse getUserData(JwtRequest loginRequest) {

        String username = loginRequest.getUsername();

        // Если пользователь есть в базе, то возвращаем пустой JWTResponse
        if (!userRepository.existsByUsername(username)){
            return new JwtResponse(null);
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("Error, user is not found"));

        // Менеджер аутентификации, передаем в конструктор токен аутентификации, в котором имя пользователя и пароль
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        loginRequest.getPassword()));

        // Устанавливаем аутентификацию
        SecurityContextHolder.getContext().setAuthentication(authentication);
        username = authentication.getName();
        String jwt = jwtUtils.generateJwtToken(username); // генерируем токен

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt);
    }

    public List<String> addUser(SignupRequest signupRequest){
        // Если пользователь есть в базе, то возвращаем сообщение об ошибке
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return Arrays.asList("bad", "Error: Username is exist");
        }

        User user = new User(signupRequest.getUsername(),
                signupRequest.getAge(),
                passwordEncoder.encode(signupRequest.getPassword()));

        Set<String> reqRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

//        System.out.println(reqRoles);

        if (reqRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
            roles.add(userRole);
        } else {
            reqRoles.forEach(r -> {
                if (r.equals("admin")) {
                    Role adminRole = roleRepository
                            .findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error, Role ADMIN is not found"));
                    roles.add(adminRole);

                    // По дефолту добавляем роль User
                } else {
                    Role userRole = roleRepository
                            .findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error, Role USER is not found"));
                    roles.add(userRole);
                }
            });
        }
        // Устанавливаем роли нашему пользователю (код сверху это жесть, нужно переписать)
        user.setRoles(roles);
        userRepository.save(user); // сохраняем пользователя в бд
        return Arrays.asList("good", "User CREATED");
    }

    public List<String> checkUsername(String username){
        if (userRepository.existsByUsername(username)){
            return Arrays.asList("bad", "Error: Username is exist");
        }
        return Arrays.asList("good", "Username isn't exist yet");
    }





}