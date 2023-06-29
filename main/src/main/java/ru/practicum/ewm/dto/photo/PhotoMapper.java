package ru.practicum.ewm.dto.photo;

import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.practicum.ewm.model.photo.Photo;

import java.io.IOException;
import java.util.Objects;

@UtilityClass
public class PhotoMapper {

    public static PhotoDto toPhotoDto(Photo photo) {
        return new PhotoDto(
                photo.getName(),
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/files/")
                        .path(photo.getId().toString())
                        .toUriString(),
                photo.getType(),
                photo.getData().length
        );
    }

    public static Photo toPhoto(MultipartFile file) throws IOException {
        return new Photo(
                null,
                null,
                StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())),
                file.getContentType(),
                file.getBytes()
        );
    }
}
