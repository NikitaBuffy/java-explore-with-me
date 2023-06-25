package ru.practicum.ewm.statsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.statsdto.HitDto;
import ru.practicum.ewm.statsdto.StatsDto;
import ru.practicum.ewm.statsserver.model.stats.StatsMapper;
import ru.practicum.ewm.statsserver.repository.StatsRepository;
import ru.practicum.ewm.statsserver.model.hit.Hit;
import ru.practicum.ewm.statsserver.model.hit.HitMapper;
import ru.practicum.ewm.statsserver.model.stats.Stats;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.statsserver.util.Constants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void addHit(HitDto hitDto) {
        Hit hit = statsRepository.save(HitMapper.dtoToHit(hitDto));
        log.info("Saved new hit: {}", hit);
    }

    @Override
    public List<StatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        List<Stats> statsList;

        LocalDateTime startDate = LocalDateTime.parse(start, DATE_FORMAT);
        LocalDateTime endDate = LocalDateTime.parse(end, DATE_FORMAT);

        if (startDate.isAfter(endDate)) {
            log.info("Stats-Server Error. Parameter start is before end. Start = {}, end = {}", startDate, endDate);
            throw new ValidationException("Start must be after end.");
        }

        if (unique) {
            if (uris == null) {
                statsList = statsRepository.findAllUrisWithUniqueIp(startDate, endDate);
            } else {
                statsList = statsRepository.findUrisWithUniqueIp(startDate, endDate, uris);
            }
        } else {
            if (uris == null) {
                statsList = statsRepository.findAllUris(startDate, endDate);
            } else {
                statsList = statsRepository.findUris(startDate, endDate, uris);
            }
        }

        return statsList.isEmpty() ? Collections.emptyList() :
                statsList.stream().map(StatsMapper::statsToDto).collect(Collectors.toList());
    }
}
