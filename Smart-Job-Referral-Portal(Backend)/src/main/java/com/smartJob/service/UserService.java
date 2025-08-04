package com.smartJob.service;

import com.smartJob.dtos.UserRequestDto;
import com.smartJob.dtos.UserResponseDto;
import com.smartJob.entity.User;
import com.smartJob.exception.AlreadyRegisterException;
import com.smartJob.exception.NotFound;
import com.smartJob.jwtUtil.JwtUtil;
import com.smartJob.repo.ReferralRepository;
import com.smartJob.repo.UserRepository;
import com.smartJob.entity.RefreshToken;
import com.smartJob.security.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepo;
    private final ReferralRepository referralRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public UserResponseDto saveUser(UserRequestDto requestDto){
        User existUser = userRepo.findByEmail(requestDto.getEmail()).orElse(null);
        if(existUser != null){
            throw new AlreadyRegisterException("User already Exist with user id "+existUser.getId());
        }
        User user = new User();
        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRole(requestDto.getRole());

        userRepo.save(user);

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail() ,String.valueOf(user.getRole())) ;
    }

    public UserResponseDto getUser(String email){
        User existUser = userRepo.findByEmail(email).orElseThrow(()->new NotFound("User Not found"));

        return new UserResponseDto(existUser.getId(), existUser.getName(), existUser.getEmail() ,String.valueOf(existUser.getRole()) ) ;
    }

    public String generateToken(String username){
        User user = userRepo.findByEmail(username).get();
        return jwtUtil.generateToken(username , List.of(String.valueOf(user.getRole())));
    }



    public String generateRefreshToken(String username){

        return refreshTokenService.generateRefreshToken(username);
    }

    public String generateNewJwtToken(String rToken){

        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(rToken);
        User user = userRepo.findByEmail(refreshToken.getUserName()).get();

        return jwtUtil.generateToken(refreshToken.getUserName() ,List.of(String.valueOf(user.getRole())));
    }
}
