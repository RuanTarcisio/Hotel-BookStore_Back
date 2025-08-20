package com.rtarcisio.hotelbookstore.user.domains;

import com.rtarcisio.hotelbookstore.shared.models.AbstractImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_images")
public class ImageUser extends AbstractImage {

    @OneToOne
    private User user;

}
