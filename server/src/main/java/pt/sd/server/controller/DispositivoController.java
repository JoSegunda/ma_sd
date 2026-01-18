package pt.sd.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.sd.server.model.Dispositivo;
import pt.sd.server.service.MonitorizacaoService;
import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DispositivoController {

    @Autowired
    private MonitorizacaoService service;

    @PostMapping
    public Dispositivo criar(@RequestBody Dispositivo d) {
        return service.registarDispositivo(d); 
    }

    @GetMapping
    public List<Dispositivo> listar() {
        return service.listarTodos(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dispositivo> obter(@PathVariable String id) {
        return service.procurarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable String id) {
        service.eliminarDispositivo(id); 
    }
}