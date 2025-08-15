package com.rtarcisio.hotelbookstore.user.services;

import com.rtarcisio.hotelbookstore.shared.dtos.UserDTO;
import com.rtarcisio.hotelbookstore.shared.dtos.UserOAuthDTO;
import com.rtarcisio.hotelbookstore.user.domains.User;
import com.rtarcisio.hotelbookstore.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void registerUser(UserDTO dto){

    }

    public void registerUser(UserOAuthDTO dto){
        User user = new User(dto.userAuthId(), dto.name(), dto.profileImageUrl());
        repository.save(user);
    }

//    private final PasswordEncoder passwordEncoder;
////    @Value("${app.base-url}")
////    private String baseUrl;
//
//    public User getByEmail(String email) {
//        Optional<User> user = repository.findByEmail(email);
//
//        if (user.isEmpty()) {
//            throw new NoSuchElementException("Usuário não encontrado!");
//        }
//        return user.get();
//    }
//
//    public User getById(Long id) {
//
//        Optional<User> user = repository.findById(id);
//
//        if (user.isEmpty()) {
//            throw new NoSuchElementException("Usuário não encontrado!");
//        }
//        return user.get();
//    }
//
//    public User getByCpf(String cpf) {
//        Optional<User> user = repository.findByCpf(cpf);
//
//        if (user.isEmpty()) {
//            throw new NoSuchElementException("Usuário não encontrado!");
//        }
//        return user.get();
//    }

//    @Transactional
//    public User save(InputUserRegister input) {
//
//        var possibleUser= repository.existsByEmailOrCpf(input.getEmail(), input.getCpf());
//
//        if (possibleUser) {
//            throw new DuplicatedTupleException("User already registered!");
//        }
//        User user = inputToUser(input);
//        user.setFullyRegistered(true);
//        encodePassword(user);
//        user.setCodToken(TokenUtil.gerarTokenCurto());
//
//        String template = mailTo.ativarUsuario(user);
//        emailSenderService.enviarEmail(template, "ATIVACAO_DE_CONTA", user.getEmail());
//        user = repository.save(user);
//
//
//        if (input.getProfileImage() != null && !input.getProfileImage().isEmpty()) {
//            ImageUser imageUser = mapToImage(input.getProfileImage());
//            imageUser.setUser(user);
//            user.setImageUser(imageUser);String imageUrl = baseUrl + "/v1/users/profile/photo/" + user.getImageUser().getId();
//            user.setProfileImageUrl(imageUrl);
//            return user;
//        }
//        return user;
//
//    }
//
//    public UserDTO getUser(Long id) {
//        User user = getById(id);
//        return userMapper.userToDto(user);
//    }
//
//    private void encodePassword(User user) {
//        String rawPassword = user.getPassword();
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//        user.setPassword(encodedPassword);
//    }
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
//    @Transactional
//    public Optional<ImageUser> getImageByUserId(Long id) {
//
//        return imageUserRepository.findByUserId(id);
//    }

}
