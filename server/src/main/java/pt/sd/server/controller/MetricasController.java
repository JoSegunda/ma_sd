package pt.sd.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.sd.server.model.Metricas;
import pt.sd.server.service.MonitorizacaoService;

@RestController
@RequestMapping("/api/metrics")
public class MetricasController {

    @Autowired
    private MonitorizacaoService service;

    @PostMapping("/ingest")
    public ResponseEntity<String> ingerir(@RequestBody Metricas m) {
        boolean sucesso = service.processarMetrica(m);
        
        if (sucesso) {
            return ResponseEntity.ok("Métrica recebida com sucesso."); 
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Erro: Dispositivo não registado ou inativo."); 
        }
    }
}