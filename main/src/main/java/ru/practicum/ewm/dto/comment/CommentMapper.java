package ru.practicum.ewm.dto.comment;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.photo.PhotoMapper;
import ru.practicum.ewm.model.comment.Comment;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.Constants.*;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(NewCommentDto newCommentDto) {
        return new Comment(
                null,
                newCommentDto.getText(),
                null,
                null,
                null,
                newCommentDto.getRating(),
                new ArrayList<>()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getAuthor().getName(),
                comment.getText(),
                comment.getCreated().format(DATE_FORMAT),
                comment.getRating(),
                comment.getPhotos().stream().map(PhotoMapper::toPhotoDto).collect(Collectors.toList())
        );
    }
}
