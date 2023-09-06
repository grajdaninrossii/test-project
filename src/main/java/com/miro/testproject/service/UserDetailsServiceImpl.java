package com.miro.testproject.service;


import com.miro.testproject.entity.User;
import com.miro.testproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

//    public UserDetailsServiceImpl(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username:" + username));
        return UserDetailsImpl.build(user);
    }

    public Boolean setAge(Long age){
        return false;
    }

}
