package com.rtarcisio.hotelbookstore.room.dtos.inputs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class InputRoom {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    private Double price;

    @NotNull(message = "Image is mandatory")
    private MultipartFile roomImage;
}
