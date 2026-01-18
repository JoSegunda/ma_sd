package pt.sd.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.sd.server.model.Dispositivo;
import pt.sd.server.model.Metricas;
import pt.sd.server.repository.DispositivoRepository;
import pt.sd.server.repository.MetricasRepository;

import java.time.LocalDateTime;
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

    //método que decide qual consulta executar com base no parâmetro level (nível de agregação)
    public String consultarMedia(String nivel, String idEntidade, LocalDateTime inicio, LocalDateTime fim) {
    // Exemplo: nivel="sala", idEntidade="B01"
        if (inicio == null) inicio = LocalDateTime.now().minusHours(24); // Default: últimas 24h
        if (fim == null) fim = LocalDateTime.now();

        String resultado = null;

        switch (nivel.toLowerCase()) {
            case "edificio":
                resultado = metricasRepository.obterMediaPorEdificio(idEntidade, inicio, fim);
                break;
            case "sala":
                resultado = metricasRepository.obterMediaPorSala(idEntidade, inicio, fim);
                break;
            // Adicionar casos para 'departamento' e 'piso' conforme necessário
        }

        // Tratar retorno nulo (sem dados)
        return resultado != null ? resultado : "0.0,0.0";
    }
}