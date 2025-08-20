package com.rtarcisio.hotelbookstore.user.services;

import com.rtarcisio.hotelbookstore.auth.domains.AuthUser;
import com.rtarcisio.hotelbookstore.auth.services.AuthUserService;
import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared.events.UserOAuthRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared.events.UserRegisteredEvent;
import com.rtarcisio.hotelbookstore.shared.exceptions.DuplicatedTupleException;
import com.rtarcisio.hotelbookstore.shared.exceptions.ObjetoNaoEncontradoException;
import com.rtarcisio.hotelbookstore.user.domains.ImageUser;
import com.rtarcisio.hotelbookstore.user.domains.User;
import com.rtarcisio.hotelbookstore.user.dtos.inputs.InputCompletedUser;
import com.rtarcisio.hotelbookstore.user.mappers.ImageUserMapper;
import com.rtarcisio.hotelbookstore.user.mappers.UserMapper;
import com.rtarcisio.hotelbookstore.user.repositories.ImageUserRepository;
import com.rtarcisio.hotelbookstore.user.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.rtarcisio.hotelbookstore.user.mappers.ImageUserMapper.mapToImage;

@Service
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository repository;
    private final ImageUserRepository imageUserRepository;
    private final AuthUserService authUserService;
    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public void registerUser(UserRegisteredEvent event) {
        var possibleUser= repository.existsByCpf(event.getCpf());

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

        if (event.getPhoto() != null) {
            user.setImageUser(ImageUserMapper.mapToImage(event.getPhoto()));
            user.getImageUser().setUser(user);
            String imageUrl = baseUrl + "/v1/users/profile/" + user.getUserId() + "/photo";
            user.setProfileImageUrl(imageUrl);
            repository.save(user);
        }

        authUserService.makeFullyRegistred(user.getAuthUserId());
    }

    public void registerUser(UserOAuthRegisteredEvent event){
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
    @Transactional
    public ImageUser getImageByUserId(String id) {
        var user = getUserById(id).orElseThrow(()-> new ObjetoNaoEncontradoException("User not found!"));
        if(user.getImageUser() == null) {
            throw new ObjetoNaoEncontradoException("");
        }
        return imageUserRepository.findByUser(user).orElse(null);
    }

    public void completeRegistration(AuthUser authUser, @Valid InputCompletedUser input) {
        if (authUser.isEnabled()) {
            throw new RuntimeException("Usuário já possui cadastro completo");
        }

        if (existsByCpf(input.cpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }

        User user = UserMapper.inputUserCompletedtTouser(authUser.getAuthUserId(), authUser.getEmail(), input);
        ImageUser image = mapToImage(input.photo());


        authUserService.makeFullyRegistred(authUser);
    }
}
