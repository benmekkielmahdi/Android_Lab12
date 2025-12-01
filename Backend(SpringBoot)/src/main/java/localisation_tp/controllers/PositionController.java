package localisation_tp.controllers;




import localisation_tp.entities.Position;
import localisation_tp.services.PositionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/positions")
@CrossOrigin("*")
public class PositionController {

    private final PositionService service;

    public PositionController(PositionService service) {
        this.service = service;
    }

    @PostMapping
    public Position create(@RequestBody Position position) {
        return service.create(position);
    }

    @GetMapping
    public List<Position> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Position getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Position update(@PathVariable Long id, @RequestBody Position p) {
        p.setId(id);
        return service.update(p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
