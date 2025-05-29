package dev.danilo.moto2000.service.impl;


import dev.danilo.moto2000.dto.LoginRequest;
import dev.danilo.moto2000.dto.RegisterRequest;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.UserDTO;
import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.enums.UserRole;
import dev.danilo.moto2000.exceptions.InvalidCredentialsException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.exceptions.DataAlreadyExistsException;
import dev.danilo.moto2000.repository.UserRepository;
import dev.danilo.moto2000.security.JwtUtils;
import dev.danilo.moto2000.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final JwtUtils jwtUtils;

    @Override
    public Response registerUser(RegisterRequest request) {
        UserRole userRole = UserRole.CLIENT;


        if (repository.existsByUsername(request.getUsername())) {

            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("Este usernamwe já existe!")
                    .build();
            throw new DataAlreadyExistsException(conflictResponse);
        };

        if (request.getRole() != null) {
            userRole = request.getRole();
        }

        User userToSave = User.builder()
                .username(request.getUsername())
                .password(encoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        repository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("Usuário criado com sucesso")
                .build();
    }

    @Override
    public String loginAndGetToken(LoginRequest request) {
        User user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Senha inválida para usuário: {}", request.getUsername());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        return jwtUtils.generateToken(request.getUsername());
    }

    @Override
    public Response getAllUsers() {
        List<User> users = repository.findAll();

        List<UserDTO> usersDTO = users.stream().map(user -> mapper.map(users, UserDTO.class)).collect(Collectors.toList());

        // usersDTO.forEach(user -> user.setTransactions(null));

        return Response.builder()
                .status(200)
                .message("Sucesso")
                .users(usersDTO)
                .build();
    }

    @Override
    public User getCurrentLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return repository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    @Override
    public Response updateUser(UUID id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (dto.getUsername() != null) user.setUsername(dto.getUsername());
        if (dto.getRole() != null) user.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(encoder.encode(dto.getPassword()));
        }

        repository.save(user);

        return Response.builder()
                .status(200)
                .message("Usuário atualizado com sucesso")
                .build();

    }

    @Override
    public void deleteUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        repository.delete(user);

    }

}
