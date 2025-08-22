package com.rtarcisio.hotelbookstore.auth_boundary.domains;

import com.rtarcisio.hotelbookstore.auth_boundary.enums.ProviderEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserConnectedAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProviderEnum provider;
    private String providerId;
    private LocalDateTime connectedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AuthUser authUser;

    public UserConnectedAccount(ProviderEnum provider, String providerId, AuthUser user) {
        this.provider = provider;
        this.providerId = providerId;
        this.connectedAt = LocalDateTime.now();
        this.authUser = user;
    }
}
