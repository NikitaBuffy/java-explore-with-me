package ru.practicum.ewm.repository.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.exception.CompilationNotFoundException;
import ru.practicum.ewm.model.compilation.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);

    default Compilation getExistingCompilation(Long compId) {
        return findById(compId).orElseThrow(() -> {
            throw new CompilationNotFoundException(String.format("Compilation with id=%d was not found", compId));
        });
    }
}
