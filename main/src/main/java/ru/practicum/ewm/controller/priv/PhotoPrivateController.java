package ru.practicum.ewm.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
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
    public ResponseEntity<String> getFile(@PathVariable String id) {
        Photo photo = photoService.getPhotoById(id);
        byte[] fileData = photo.getData();

        String base64Data = Base64Utils.encodeToString(fileData);

        String imageUrl = "data:" + photo.getType() + ";base64," + base64Data;
        String imgTag = "<img src=\"" + imageUrl + "\">";
        String htmlContent = "<!DOCTYPE html><html><head><title>" + photo.getName() + "</title></head><body>" + imgTag + "</body></html>";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(htmlContent, headers, HttpStatus.OK);
    }
}
