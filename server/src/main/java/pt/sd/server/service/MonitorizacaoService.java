package pt.sd.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.sd.server.model.Dispositivo;
import pt.sd.server.model.Metricas;
import pt.sd.server.repository.DispositivoRepository;
import pt.sd.server.repository.MetricasRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MonitorizacaoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private MetricasRepository metricasRepository;

    // --- Gestão de Dispositivos ---

    public Dispositivo registarDispositivo(Dispositivo d) {
        return dispositivoRepository.save(d); 
    }

    public List<Dispositivo> listarTodos() {
        return dispositivoRepository.findAll(); 
    }

    public Optional<Dispositivo> procurarPorId(String id) {
        return dispositivoRepository.findById(id); 
    }

    public void eliminarDispositivo(String id) {
        dispositivoRepository.deleteById(id); 
    }

    // --- Processamento de Métricas ---

    /**
     * Valida e guarda a métrica recebida por qualquer protocolo.
     * Se o dispositivo não existir, a métrica é descartada .
     */
    public boolean processarMetrica(Metricas metrica) {
        Optional<Dispositivo> disp = dispositivoRepository.findById(metrica.getIdDispositivo());
        
        if (disp.isPresent() && disp.get().isAtivo()) {
            metricasRepository.save(metrica); 
            return true;
        }
        
        // Dispositivo inválido ou inativo: descarta a métrica [cite: 96]
        return false;
    }
}