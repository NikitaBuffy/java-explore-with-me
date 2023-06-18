package ru.practicum.explorewithme.statsserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.statsdto.HitDto;
import ru.practicum.explorewithme.statsdto.StatsDto;
import ru.practicum.explorewithme.statsserver.model.Hit;
import ru.practicum.explorewithme.statsserver.model.HitMapper;
import ru.practicum.explorewithme.statsserver.model.Stats;
import ru.practicum.explorewithme.statsserver.model.StatsMapper;
import ru.practicum.explorewithme.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<Stats> statsList;

        if (unique) {
            if (uris == null) {
                statsList = statsRepository.findAllUrisWithUniqueIp(start, end);
            } else {
                statsList = statsRepository.findUrisWithUniqueIp(start, end, uris);
            }
        } else {
            if (uris == null) {
                statsList = statsRepository.findAllUris(start, end);
            } else {
                statsList = statsRepository.findUris(start, end, uris);
            }
        }

        return statsList.isEmpty() ? Collections.emptyList() :
                statsList.stream().map(StatsMapper::statsToDto).collect(Collectors.toList());
    }
}
