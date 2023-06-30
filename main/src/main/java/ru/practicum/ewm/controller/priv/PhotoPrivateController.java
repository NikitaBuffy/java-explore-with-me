package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.ewm.dto.photo.PhotoDto;
import ru.practicum.ewm.model.photo.Photo;
import ru.practicum.ewm.service.photo.PhotoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PhotoPrivateController {

    private final PhotoService photoService;

    /*
     * Добавление фотографий к комментариям
     * @param files - фотографии формата form-data
     * @param commentId - id комментария
     * Возвращает список добавленных фотографий, код ошибки 404 (комментарий не найден), либо код 417 (превышен допустимый размер файла)
     */
    @PostMapping("/upload/comments/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<PhotoDto> uploadFiles(@RequestParam("files") List<MultipartFile> files, @PathVariable Long commentId) {
        return photoService.uploadFiles(files, commentId);
    }

    /*
     * Технический эндпоинт для отображения загруженных фотографий в клиенте
     * @param id - uuid фотографии
     * Возвращает HTML страницу с загруженной фотографией в теге <img>
     */
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable String id) {
        Photo photo = photoService.getPhotoById(id);
        byte[] fileData = photo.getData();

        MediaType mediaType;
        if (photo.getType().equals("image/png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (photo.getType().equals("image/jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        } else {
            throw new UnsupportedOperationException("Unsupported file format.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);

        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }
}
