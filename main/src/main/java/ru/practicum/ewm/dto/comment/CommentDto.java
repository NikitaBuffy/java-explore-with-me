package ru.practicum.ewm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.photo.PhotoDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    /*
     * Идентификатор
     */
    private Long id;

    /*
     * Имя пользователя, опубликовавшего отзыв
     */
    private String authorName;

    /*
     * Текст отзыва
     */
    private String text;

    /*
     * Дата и время создания отзыва (в формате "yyyy-MM-dd HH:mm:ss")
     */
    private String created;

    /*
     * Рейтинг, поставленный событию в отзыве (от 1 до 5)
     */
    private int rating;

    /*
     * Фотографии, прикрепленные к отзыву
     */
    private List<PhotoDto> photos;
}
