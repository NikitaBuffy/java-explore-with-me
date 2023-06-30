package ru.practicum.ewm.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.CommentMapper;
import ru.practicum.ewm.dto.comment.NewCommentDto;
import ru.practicum.ewm.dto.photo.PhotoMapper;
import ru.practicum.ewm.exception.CommentNotFoundException;
import ru.practicum.ewm.exception.ForbiddenException;
import ru.practicum.ewm.model.comment.Comment;
import ru.practicum.ewm.model.comment.CommentSort;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.photo.Photo;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestStatus;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.comment.CommentRepository;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.photo.PhotoRepository;
import ru.practicum.ewm.repository.request.RequestRepository;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.util.PageRequestUtil;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.Constants.DATE_FORMAT;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl extends PageRequestUtil implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final PhotoRepository photoRepository;

    @Override
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.getExistingUser(userId);
        Event event = eventRepository.getExistingEvent(eventId);

        Request request = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        if (request == null || request.getStatus() != RequestStatus.CONFIRMED) {
            log.info("User with ID = {} must participate in the event with ID = {} to leave a comment.", userId, eventId);
            throw new ForbiddenException("User must participate in the event to leave a comment.");
        }

        Comment existingComment = commentRepository.findByAuthorIdAndEventId(userId, eventId);
        if (existingComment != null) {
            log.info("User with ID = {} has already left a comment. Comment = {}", userId, existingComment);
            throw new ForbiddenException("Cannot leave a comment on the event again.");
        }

        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());

        Comment addedComment = commentRepository.save(comment);
        log.info("Added new comment: comment = {}", addedComment);

        calculateAndUpdateRating(event);
        log.info("Updated rating for the event with ID = {}, new rating = {}", eventId, event.getRating());

        return CommentMapper.toCommentDto(addedComment);
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {
        User user = userRepository.getExistingUser(userId);
        Comment comment = commentRepository.getExistingComment(commentId);
        Event event = comment.getEvent();

        if (comment.getAuthor() != user) {
            log.info("Comment error. Cannot delete comment with ID = {}, because user with ID = {} is not an author.",
                    commentId, userId);
            throw new ForbiddenException("Only author can delete the comment.");
        }

        commentRepository.delete(comment);
        photoRepository.deleteByCommentId(commentId);
        log.info("Deleted comment with ID = {} by author and linked photos", commentId);

        calculateAndUpdateRating(event);
        log.info("Updated rating for the event with ID = {}, new rating = {}", event.getId(), event.getRating());
    }

    @Override
    public CommentDto getComment(Long eventId, Long commentId) {
        Comment comment = commentRepository.getExistingComment(commentId);

        if (!comment.getEvent().getId().equals(eventId)) {
            log.info("Comment error. Comment with ID = {} was not written for the event with ID = {}", commentId, eventId);
            throw new CommentNotFoundException(String.format("Comment with id=%d was not found", commentId));
        }

        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        findPhotos(commentDto);

        return commentDto;
    }

    @Override
    public List<CommentDto> getComments(Long eventId, String sort, int from, int size) {
        Pageable page = createPageRequest(from, size, CommentSort.valueOf(sort));

        List<CommentDto> comments = commentRepository.findByEventId(eventId, page)
                .getContent()
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        comments.forEach(this::findPhotos);
        return comments;
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(List<Long> events, String rangeStart, String rangeEnd, Integer rating,
                                               int from, int size) {
        Pageable page = createPageRequest(from, size);

        Page<Comment> comments = commentRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (events != null) {
                predicates.add(root.get("event").get("id").in(events));
            }
            if (rangeStart != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("created"), LocalDateTime.parse(rangeStart, DATE_FORMAT)));
            }
            if (rangeEnd != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("created"), LocalDateTime.parse(rangeEnd, DATE_FORMAT)));
            }
            if (rating != null) {
                predicates.add(criteriaBuilder.equal(root.get("rating"), rating));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, page);

        List<CommentDto> commentList = comments.getContent().stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        commentList.forEach(this::findPhotos);
        return commentList;
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        Comment comment = commentRepository.getExistingComment(commentId);
        Event event = eventRepository.getExistingEvent(comment.getEvent().getId());
        commentRepository.delete(comment);
        photoRepository.deleteByCommentId(commentId);
        log.info("Deleted comment with ID = {} by admin and linked photos", commentId);

        calculateAndUpdateRating(event);
        log.info("Updated rating for the event with ID = {}, new rating = {}", event.getId(), event.getRating());
    }

    private void findPhotos(CommentDto commentDto) {
        List<Photo> photos = photoRepository.findByCommentId(commentDto.getId());
        commentDto.setPhotos(photos.stream().map(PhotoMapper::toPhotoDto).collect(Collectors.toList()));
    }

    private void calculateAndUpdateRating(Event event) {
        BigDecimal rating = commentRepository.getAverageRatingByEventId(event.getId());
        event.setRating(Objects.requireNonNullElse(rating, BigDecimal.valueOf(0.0)));
        eventRepository.updateEventRating(event.getRating(), event.getId());
    }
}
