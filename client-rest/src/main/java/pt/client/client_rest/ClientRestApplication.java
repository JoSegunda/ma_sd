package pt.client.client_rest;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import pt.client.client_rest.model.Metricas;

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

            Metricas m = new Metricas();
            m.setIdDispositivo(idDispositivo);
            m.setTemperatura(gerador.getTemperatura());
            m.setHumidade(gerador.getHumidade());
            
            m.setTempo(java.time.LocalDateTime.now()); 

            try {
                restTemplate.postForEntity(URL_SERVIDOR, m, String.class);
                System.out.println("REST: Métrica enviada de " + idDispositivo);
            } catch (Exception e) {
                System.err.println("REST Falha: " + e.getMessage());
            }
            Thread.sleep(10000);
        }
    }
}
