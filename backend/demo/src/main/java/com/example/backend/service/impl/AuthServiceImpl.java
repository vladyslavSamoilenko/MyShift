package com.example.backend.service.impl;

import com.example.backend.mapper.UserMapper;
import com.example.backend.model.constants.ApiErrorMessage;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.entities.Employee;
import com.example.backend.model.entities.Project;
import com.example.backend.model.entities.User;
import com.example.backend.model.enums.Role;
import com.example.backend.model.exception.InvalidDataException;
import com.example.backend.model.request.post.userRequests.UserOwnerRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.repository.EmployeeRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.model.JwtTokenProvider;
import com.example.backend.security.model.profiles.UserProfileDTO;
import com.example.backend.security.model.requests.LoginRequest;
import com.example.backend.service.AuthService;
import com.example.backend.service.ProjectService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectService projectService;

    @Override
    public GeneralResponse<UserProfileDTO> login(@NotNull LoginRequest request) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage());
        }

        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new InvalidDataException(ApiErrorMessage.INVALID_USER_OR_PASSWORD.getMessage()));

        String token = jwtTokenProvider.generateToken(user);
        UserProfileDTO userProfileDTO = userMapper.toUserProfileDTO(user, token);
        userProfileDTO.setToken(token);
        return GeneralResponse.createSuccessfulWithNewToken(userProfileDTO);
    }

    @Override
    @Transactional
    public GeneralResponse<UserDTO> registerUserOwner(UserOwnerRequest userOwnerRequest){

        try{
            projectService.createProject(userOwnerRequest);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(ApiErrorMessage.PROJECT_ALREADY_EXIST.getMessage());
        }

        Employee employee = new Employee();
        employee.setFirstName(userOwnerRequest.getEmployeeData().getFirstName());
        employee.setLastName(userOwnerRequest.getEmployeeData().getLastName());
        employee.setEmail(userOwnerRequest.getUserData().getEmail());
        employee.setPhone(userOwnerRequest.getEmployeeData().getPhone());
        employee.setCreatedAt(LocalDateTime.now());

        Employee savedEmployee = employeeRepository.save(employee);

        User user = new User();
        user.setEmail(userOwnerRequest.getUserData().getEmail());
        user.setPassword(passwordEncoder.encode(userOwnerRequest.getUserData().getPassword()));
        user.setRole(Role.ADMIN);
        user.setEmployee(savedEmployee);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserDTO userDTO = userMapper.toUserDTO(user);
        return GeneralResponse.createSuccessful(userDTO);
    }
}
