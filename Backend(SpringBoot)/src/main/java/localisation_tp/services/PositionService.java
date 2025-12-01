package localisation_tp.services;



import localisation_tp.entities.Position;
import localisation_tp.repositories.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {

    private final PositionRepository repository;

    public PositionService(PositionRepository repository) {
        this.repository = repository;
    }

    public Position create(Position p) {
        return repository.save(p);
    }

    public List<Position> getAll() {
        return repository.findAll();
    }

    public Position getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Position update(Position p) {
        return repository.save(p);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
