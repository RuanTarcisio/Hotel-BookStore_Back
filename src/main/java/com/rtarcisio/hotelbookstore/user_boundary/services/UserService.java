package com.rtarcisio.hotelbookstore.user_boundary.services;

import com.rtarcisio.hotelbookstore.auth_boundary.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth_boundary.services.AuthUserService;
import com.rtarcisio.hotelbookstore.shared_boundary.clients.ImageClientService;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared_boundary.exceptions.DuplicatedTupleException;
import com.rtarcisio.hotelbookstore.shared_boundary.dtos.UserResponse;
import com.rtarcisio.hotelbookstore.shared_boundary.utils.SecurityUtils;
import com.rtarcisio.hotelbookstore.storage_boundary.dtos.inputs.ImageResponse;
import com.rtarcisio.hotelbookstore.user_boundary.domains.User;
import com.rtarcisio.hotelbookstore.user_boundary.dtos.inputs.InputCompletedUser;
import com.rtarcisio.hotelbookstore.user_boundary.mappers.UserMapper;
import com.rtarcisio.hotelbookstore.user_boundary.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final AuthUserService authUserService;
    @Value("${app.base-url}")
    private String baseUrl;
    private final ImageClientService imageClientService;

    @Transactional
    public void registerUser(UserRegisteredEvent event) {
        var possibleUser = repository.existsByCpf(event.getCpf());

        if (possibleUser) {
            throw new DuplicatedTupleException("User already registered!");
        }
        User user = UserMapper.eventToUser(event);
//        user.setFullyRegistered(true);
//        user.setCodToken(TokenUtil.gerarTokenCurto());
//
//        String template = mailTo.ativarUsuario(user);
//        emailSenderService.enviarEmail(template, "ATIVACAO_DE_CONTA", user.getEmail());
        user = repository.save(user);

        authUserService.makeFullyRegistred(user.getAuthUserId());
    }

    {/*
        Trocar email deve

    */}

    public void registerUser(UserOAuthRegisteredEvent event) {
        User user = UserMapper.eventToUser(event);
        repository.save(user);
    }

    public Optional<User> getByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<User> getUserById(String id) {
        return repository.findById(id);
    }

    public Optional<User> getUserByAuthUserId(String id) {
        return repository.findByAuthUserId(id);
    }

    public Optional<User> getUserByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }

    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    public UserDTO getUserDTO(String id) {
        var user = getUserById(id);
        return null;
    }

    public UserResponse getUserResponseByAuthUserId(String id) {
        var user = getUserByAuthUserId(id);
        UserResponse response = UserMapper.userToUserResponse(user.get());

        return null;
    }

    //
//    public UserDTO updateUser(Long id, InputUserUpdate input) {
//
//        User user = getById(id);
//        user.setName(input.name());
//        user.setEmail(input.email());
//        user.setBirthdate(input.birthdate());
//
//        if (!input.file().isEmpty()) {
//            try {
//                user.setImageUser(mapToImage(input.file()));
//            } catch (Exception e) {
//                throw new RuntimeException("Invalid image");
//            }
//        }
//        return userToDto(repository.save(user));
//    }
//

    public void completeRegistration(AuthUser authUser, @Valid InputCompletedUser input) {
        if (authUser.isEnabled()) {
            throw new RuntimeException("Usuário já possui cadastro completo");
        }
        if (existsByCpf(input.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        User user = UserMapper.inputUserCompletedtTouser(authUser.getAuthUserId(), authUser.getEmail(), input);
        authUserService.makeFullyRegistred(authUser);
    }

    public void uploadUserProfileImage(MultipartFile file, String token) {
        User user = repository.findByAuthUserId(SecurityUtils.getSubjectOrThrow())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        try {
            ImageResponse response = imageClientService.uploadImage(
                    file,
                    "USER",
                    user.getAuthUserId(),
                    "PROFILE",
                    token);

            user.setProfileImageUrl(response.getUrlImage());
            repository.save(user);

            log.info("Imagem de perfil atualizada para o usuário: {}", user.getUserId());

        } catch (Exception e) {
            log.error("Falha ao fazer upload da imagem de perfil", e);
            throw new RuntimeException("Falha no upload da imagem: " + e.getMessage(), e);
        }
    }

}

