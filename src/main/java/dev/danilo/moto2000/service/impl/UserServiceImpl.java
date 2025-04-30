package dev.danilo.moto2000.service.impl;


import dev.danilo.moto2000.dto.LoginRequest;
import dev.danilo.moto2000.dto.RegisterRequest;
import dev.danilo.moto2000.dto.Response;
import dev.danilo.moto2000.dto.UserDTO;
import dev.danilo.moto2000.entity.User;
import dev.danilo.moto2000.enums.UserRole;
import dev.danilo.moto2000.exceptions.InvalidCredentialsException;
import dev.danilo.moto2000.exceptions.NotFoundException;
import dev.danilo.moto2000.exceptions.UsernameAlreadyExistsException;
import dev.danilo.moto2000.repository.UserRepository;
import dev.danilo.moto2000.security.JwtUtils;
import dev.danilo.moto2000.service.UserService;
import io.jsonwebtoken.security.Password;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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


        if (repository.existsByUsername(request.username())) {

            Response conflictResponse = Response.builder()
                    .status(409)
                    .message("Este usernamwe já existe!")
                    .build();
            throw new UsernameAlreadyExistsException(conflictResponse);
        };

        if (request.role() != null) {
            userRole = request.role();
        }

        User userToSave = User.builder()
                .username(request.username())
                .password(encoder.encode(request.password()))
                .role(userRole)
                .build();

        repository.save(userToSave);

        return Response.builder()
                .status(200)
                .message("Usuário criado com sucesso")
                .build();
    }

    @Override
    public Response loginUser(LoginRequest request) {
        User user = repository.findByUsername(request.username()).orElseThrow(() -> new InvalidCredentialsException("Credenciais inválidas"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            log.warn("Tentativa de login com senha inválida para o usuário: {}", request.username());
            throw new InvalidCredentialsException("Credenciais inválidas");
        }

        String token = jwtUtils.generateToken(request.username());

        return Response.builder()
                .status(200)
                .message("Usuário logado com sucesso")
                .role(user.getRole())
                .token(token)
                .expirationTime("6 meses")
                .build();
    }

    @Override
    public Response getAllUsers() {
        List<User> users = repository.findAll();

        List<UserDTO> usersDTO = mapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());

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
        String useername = authentication.getName();

        User user = repository.findByUsername(useername)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return user;
    }

    @Override
    public Response updateUser(UUID id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (dto.username() != null) user.setUsername(dto.username());
        if (dto.role() != null) user.setRole(dto.role());

        if (dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(encoder.encode(dto.password()));
        }

        repository.save(user);

        return Response.builder()
                .status(200)
                .message("Usuário atualizado com sucesso")
                .build();

    }

    @Override
    public Response deleteUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        repository.delete(user);

        return Response.builder()
                .status(200)
                .message("Usuário deletado com sucesso")
                .build();
    }

}
