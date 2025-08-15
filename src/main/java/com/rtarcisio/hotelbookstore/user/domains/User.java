package com.rtarcisio.hotelbookstore.user.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_user") // <-- Add this line
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String authUserId;

//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private ImageUser imageUser;

    private String profileImageUrl;
    @Column
    private String name;
    @Column(unique = true)
    private String cpf;
    private LocalDate birthdate;
    @Column(name = "created_At")
    @CreatedDate
    @JsonFormat(pattern = "dd/MM/yyyy hh:ss")
    private LocalDateTime createdAt;

    public User(String authUserId, String name, String profileImageUrl) {
        this.authUserId = authUserId;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
    }
}
