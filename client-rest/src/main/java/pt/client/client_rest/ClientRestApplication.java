package pt.client.client_rest;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import pt.sd.server.model.Metricas; // Partilhar o modelo [cite: 58]

@SpringBootApplication
public class ClientRestApplication implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String URL_SERVIDOR = "http://localhost:8080/api/metrics/ingest";

    public static void main(String[] args) {
        SpringApplication.run(ClientRestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GeradorSimulado gerador = new GeradorSimulado();
        String idDispositivo = "SENSOR_REST_01";

        while (true) {
            gerador.gerarNovosDados();

            // Preparar o payload JSON [cite: 50]
            Metricas m = new Metricas();
            m.setIdDispositivo(idDispositivo);
            m.setTemperatura(gerador.getTemperatura());
            m.setHumidade(gerador.getHumidade());
            // No REST, enviamos LocalDateTime ou String ISO 8601 [cite: 77]

            try {
                // Comunicação síncrona padrão [cite: 49, 55]
                restTemplate.postForEntity(URL_SERVIDOR, m, String.class);
                System.out.println("REST: Métrica enviada.");
            } catch (Exception e) {
                System.err.println("REST Falha: " + e.getMessage()); // Retry logic sugerido [cite: 57]
            }

            Thread.sleep(10000);
        }
    }
}
