package ru.practicum.ewm.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.RequestNotFoundException;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestStatus;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequesterId(Long userId);

    Request findByRequesterIdAndEventId(Long userId, Long eventId);

    Integer countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEventId(Long eventId);

    @Query("SELECT r FROM Request r WHERE r.id IN :ids AND r.event.id = :eventId")
    List<Request> findAllByIdAndEventId(Iterable<Long> ids, Long eventId);

    default Request getExistingRequest(Long requestId) {
        return findById(requestId).orElseThrow(() -> {
            throw new RequestNotFoundException(String.format("Participation request with id=%d was not found", requestId));
        });
    }
}
