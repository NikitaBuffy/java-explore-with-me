package ru.practicum.ewm.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.statsserver.model.hit.Hit;
import ru.practicum.ewm.statsserver.model.stats.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new ru.practicum.ewm.statsserver.model.stats.Stats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri, h.ip " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Stats> findAllUrisWithUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.statsserver.model.stats.Stats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri, h.ip " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC")
    List<Stats> findUrisWithUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.statsserver.model.stats.Stats(h.app, h.uri, COUNT(h.uri)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findAllUris(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewm.statsserver.model.stats.Stats(h.app, h.uri, COUNT(h.uri)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "AND h.uri IN :uris " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.uri) DESC")
    List<Stats> findUris(LocalDateTime start, LocalDateTime end, List<String> uris);
}
