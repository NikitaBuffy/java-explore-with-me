package ru.practicum.ewm.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {

    /*
     * Текст отзыва
     */
    @NotNull
    @Size(min = 50, max = 2000)
    private String text;

    /*
     * Рейтинг, поставленный событию в отзыве (от 1 до 5)
     */
    @NotNull
    @Min(1)
    @Max(5)
    private int rating;
}
