package ru.practicum.ewm.dto.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm.dto.event.EventMapper;
import ru.practicum.ewm.model.compilation.Compilation;

import java.util.Collections;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(
                null,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle(),
                null
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getPinned(),
                compilation.getTitle(),
                compilation.getEvents() != null ?
                        compilation.getEvents()
                                .stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList()) : Collections.emptyList()
        );
    }
}
