package ru.practicum.ewm.repository.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.photo.Photo;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, String> {

    Optional<Photo> findById(UUID id);

    @Transactional
    void deleteByCommentId(Long commentId);

    List<Photo> findByCommentId(Long commentId);

    default Photo getExistingPhoto(UUID id) {
        return findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("Photo with id=%s was not found", id));
        });
    }
}
