package ru.practicum.ewm.dto.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {

    /*
     * Название загруженного файла
     */
    private String name;

    /*
     * URL файла на сервере
     */
    private String url;

    /*
     * Тип файла
     */
    private String type;

    /*
     * Размер файла
     */
    private long size;
}
