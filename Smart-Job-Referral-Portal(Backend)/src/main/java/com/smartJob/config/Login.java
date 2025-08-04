package com.smartJob.config;

import com.smartJob.entity.User;
import com.smartJob.exception.NotFound;
import com.smartJob.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Login implements UserDetailsService {

    private final UserRepository userRepo;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username).orElseThrow(()->new NotFound("User Not found..!"));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail()).password(user.getPassword())
                .roles(String.valueOf(user.getRole())).build();
    }
}
