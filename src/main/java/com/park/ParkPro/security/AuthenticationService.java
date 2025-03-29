package com.park.ParkPro.security;

import com.park.ParkPro.dto.AuthenticationRequest;
import com.park.ParkPro.dto.AuthenticationResponse;
import com.park.ParkPro.dto.RegisterRequest;
import com.park.ParkPro.exception.BusinessRuleException;
import com.park.ParkPro.exception.ResourceNotFoundException;
import com.park.ParkPro.model.User;
import com.park.ParkPro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessRuleException("Email already in use");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        userRepository.save(user);

        var jwtToken = jwtService.generateTokenFromUser(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new BusinessRuleException("User account is inactive");
        }

        var jwtToken = jwtService.generateTokenFromUser(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
