package com.rtarcisio.hotelbookstore.auth.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String authUserId;
    @Column
    private String password;
    @Column(unique = true)
    private String email;
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy hh:ss")
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus = RegistrationStatus.SOCIAL_INCOMPLETE;
    /*
    private String number;
    private boolean isNumberValidated
* */
    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER)
    private Set<UserConnectedAccount> connectedAccounts = new HashSet<>();
    @JsonIgnore
    @Column(unique = true)
    private String codToken;
    private boolean isEnabled = false;

    public enum RegistrationStatus {
        SOCIAL_INCOMPLETE ("INCOMPLETE"),  // Apenas login social
        COMPLETE ("COMPLETE") ;          // Cadastro completo com todos os dados

        private final String value;
        RegistrationStatus(String status) {
            value = status;
        }
        public String getValue() {return value;}
    }

    public void markAsFullyRegistered() {
        this.registrationStatus = RegistrationStatus.COMPLETE;
        this.isEnabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public AuthUser(OAuth2User oAuth2User) {
        AuthUser user = new AuthUser();
        user.email = oAuth2User.getAttribute("email");
    }

    public void addConnectedAccount(UserConnectedAccount connectedAccount) {
        connectedAccounts.add(connectedAccount);
    }

}
