package pt.sd.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.sd.server.model.Dispositivo;
import pt.sd.server.model.Metricas;
import pt.sd.server.repository.DispositivoRepository;
import pt.sd.server.repository.MetricasRepository;
import java.util.Optional;

@Service
public class DispositivoService {

    @Autowired
    private DispositivoRepository dispositivoRepository;

    @Autowired
    private MetricasRepository metricasRepository;

    // Lógica de Ingestão: Valida e armazena [cite: 96, 97]
    public boolean saveMetric(Metricas metricas) {
        Optional<Dispositivo> dispositivo = dispositivoRepository.findById(metricas.getDispositivoId());
        
        if (dispositivo.isPresent() && dispositivo.get().isActive()) {
            metricasRepository.save(metricas);
            return true;
        }
        // Se o dispositivo não existir ou estiver inativo, descarta 
        return false;
    }

    // Métodos de Gestão CRUD para o DeviceController [cite: 101]
    public Dispositivo registerDispositivo(Dispositivo dispositivo) { return dispositivoRepository.save(dispositivo); }
    public Optional<Dispositivo> getDispositivo(String id) { return dispositivoRepository.findById(id); }
    public void deleteDispositivo(String id) { dispositivoRepository.deleteById(id); }
    // ... outros métodos CRUD
}