package com.rtarcisio.hotelbookstore.auth.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    @Column
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String cpf;

    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER)
    private Set<UserConnectedAccount> connectedAccounts = new HashSet<>();
    @JsonIgnore
    @Column(unique = true)
    private String codToken;
    private boolean enabled = false;
    private boolean isFullyRegistered = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    public AuthUser(OAuth2User oAuth2User) {
        AuthUser user = new AuthUser();
        user.email = oAuth2User.getAttribute("email");
        user.setFullyRegistered(false);
        user.setEnabled(true);
    }

    public void addConnectedAccount(UserConnectedAccount connectedAccount) {
        connectedAccounts.add(connectedAccount);
    }

}
