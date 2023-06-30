package ru.practicum.ewm.service.photo;

import org.springframework.web.multipart.MultipartFile;
import ru.practicum.ewm.dto.photo.PhotoDto;
import ru.practicum.ewm.model.photo.Photo;

import java.util.List;

public interface PhotoService {

    List<PhotoDto> uploadFiles(List<MultipartFile> files, Long commentId);

    Photo getPhotoById(String id);
}
