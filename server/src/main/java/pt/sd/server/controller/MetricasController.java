package pt.sd.server.controller;

import java.time.LocalDateTime;

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

    @GetMapping("/average")
    public ResponseEntity<String> obterMedia(
            @RequestParam String level,
            @RequestParam String id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        // Conversão de datas (simplificada)
        LocalDateTime dataInicio = (from != null) ? LocalDateTime.parse(from) : null;
        LocalDateTime dataFim = (to != null) ? LocalDateTime.parse(to) : null;

        String medias = service.consultarMedia(level, id, dataInicio, dataFim);
        
        // Formata a resposta JSON manualmente ou cria um DTO
        String[] valores = medias.split(",");
        return ResponseEntity.ok(String.format("{\"temperaturaMedia\": %s, \"humidadeMedia\": %s}", valores[0], valores[1]));
    }   
}