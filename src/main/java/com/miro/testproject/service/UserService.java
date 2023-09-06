package com.miro.testproject.service;

import com.miro.testproject.entity.User;
import com.miro.testproject.repository.HouseRepository;
import com.miro.testproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    private final HouseRepository houseRepository;

    private final AuthService auth;

    public String deleteUser(){
        User user = auth.getAuthUser();
        userRepository.delete(user);
        return "User successfully deleted";
    }


}
