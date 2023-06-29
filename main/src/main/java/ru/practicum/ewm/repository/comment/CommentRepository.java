package ru.practicum.ewm.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.CommentNotFoundException;
import ru.practicum.ewm.model.comment.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findByAuthorIdAndEventId(Long authorId, Long eventId);

    Page<Comment> findByEventId(Long eventId, Pageable pageable);

    Page<Comment> findAll(Specification<Comment> specification, Pageable pageable);

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.event.id = :eventId")
    Double getAverageRatingByEventId(Long eventId);

    default Comment getExistingComment(Long commentId) {
        return findById(commentId).orElseThrow(() -> {
            throw new CommentNotFoundException(String.format("Comment with id=%d was not found", commentId));
        });
    }
}
