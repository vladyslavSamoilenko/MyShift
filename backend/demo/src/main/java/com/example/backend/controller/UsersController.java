package com.example.backend.controller;

import com.example.backend.model.constants.ApiLogMessage;
import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.request.post.userRequests.UserOwnerRequest;
import com.example.backend.model.request.post.userRequests.UserUpdateRequest;
import com.example.backend.model.response.GeneralResponse;
import com.example.backend.service.UserService;
import com.example.backend.utils.ApiUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    @GetMapping("/{id}")
    private ResponseEntity<GeneralResponse<UserDTO>> getById(@PathVariable(name = "id") Integer id){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());

        GeneralResponse<UserDTO> response =userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    private ResponseEntity<GeneralResponse<UserDTO>> updateUser(@NotNull @PathVariable(name = "id") Integer id,@RequestBody UserUpdateRequest userUpdateRequest){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        GeneralResponse<UserDTO> response = userService.updateUser(id, userUpdateRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteUser(@NotNull @PathVariable(name = "id") Integer id){
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getMessage(), ApiUtils.getMethodName());
        userService.softDelete(id);
        return ResponseEntity.ok().build();
    }

}
