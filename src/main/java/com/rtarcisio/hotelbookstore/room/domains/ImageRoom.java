package com.rtarcisio.hotelbookstore.room.domains;

import com.rtarcisio.hotelbookstore.shared.models.AbstractImage;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRoom extends AbstractImage {

    @OneToOne(mappedBy = "imageRoom")
    private Room room;

}