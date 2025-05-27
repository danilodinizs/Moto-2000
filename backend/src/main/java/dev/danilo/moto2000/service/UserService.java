package dev.danilo.moto2000.service;

import dev.danilo.moto2000.dto.LoginRequest;
import dev.danilo.moto2000.dto.RegisterRequest;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.UserDTO;
import dev.danilo.moto2000.entity.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface UserService {

    Response registerUser(RegisterRequest request);
    Response getAllUsers();
    User getCurrentLoggedInUser();
    Response updateUser(UUID id, UserDTO dto);
    void deleteUser(UUID id);
    String loginAndGetToken(LoginRequest request);

}
