package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.EventNotFoundException;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByIdIn(Iterable<Long> eventIds);

    Page<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByIdAndState(Long eventId, EventState state);

    Page<Event> findAll(Specification<Event> specification, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    List<Event> findByCategoryId(Long catId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE events SET rating = :rating WHERE event_id = :eventId", nativeQuery = true)
    void updateEventRating(Double rating, Long eventId);

    default Event getExistingEvent(Long eventId) {
        return findById(eventId).orElseThrow(() -> {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        });
    }

    default Event getExistingPublishedEvent(Long eventId, EventState state) {
        return findByIdAndState(eventId, state).orElseThrow(() -> {
            throw new EventNotFoundException(String.format("Event with id=%d was not found", eventId));
        });
    }
}
