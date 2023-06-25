package ru.practicum.ewm.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.UserNotFoundException;
import ru.practicum.ewm.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    Page<User> findByUserIds(@Param("ids") Iterable<Long> ids, Pageable page);

    default User getExistingUser(Long userId) {
        return findById(userId).orElseThrow(() -> {
            throw new UserNotFoundException(String.format("User with id=%d was not found", userId));
        });
    }

}
