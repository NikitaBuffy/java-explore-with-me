package ru.practicum.ewm.service.photo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.ewm.dto.photo.PhotoDto;
import ru.practicum.ewm.dto.photo.PhotoMapper;
import ru.practicum.ewm.exception.PhotoUploadException;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.photo.Photo;
import ru.practicum.ewm.repository.comment.CommentRepository;
import ru.practicum.ewm.repository.photo.PhotoRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public List<PhotoDto> uploadFiles(List<MultipartFile> files, Long commentId) {
        Comment comment = commentRepository.getExistingComment(commentId);
        List<Photo> uploaded = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")) {
                    Photo photo = PhotoMapper.toPhoto(file);
                    photo.setComment(comment);
                    Photo savedPhoto = photoRepository.save(photo);
                    uploaded.add(savedPhoto);
                } else {
                    log.info("Attached file = {} has unsupported format = {}", file.getName(), file.getContentType());
                    throw new UnsupportedOperationException("Unsupported file format.");
                }
            } catch (IOException e) {
                throw new PhotoUploadException(e.getMessage());
            }
        }
        log.info("Add {} photos to comment with ID = {}", uploaded.size(), commentId);
        return uploaded.stream().map(PhotoMapper::toPhotoDto).collect(Collectors.toList());
    }

    @Override
    public Photo getPhotoById(String id) {
        return photoRepository.getExistingPhoto(UUID.fromString(id));
    }
}
