package pt.admin.admin_cli;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType; // <--- IMPORTANTE
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder; // <--- IMPORTANTE
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AdminCliApplication implements CommandLineRunner {

    private final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // A MAGIA ESTÁ AQUI: .web(WebApplicationType.NONE)
        // Isto impede que o admin-cli tente roubar a porta 8080
        new SpringApplicationBuilder(AdminCliApplication.class)
            .web(WebApplicationType.NONE) 
            .run(args);
    }

    @Override
    public void run(String... args) {
        System.out.println("=== CLI de Administração Iniciado ===");
        boolean rodando = true;

        while (rodando) {
            exibirMenu();
            System.out.print("Escolha: ");
            String entrada = scanner.next(); 

            switch (entrada) {
                case "1":
                    listarDispositivos();
                    break;
                case "2":
                    // Implementar lógica de registo
                    registarDispositivo();
                    break;
                case "3":
                    consultarMedias();
                    break;
                case "0":
                    rodando = false;
                    System.out.println("A sair...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Listar Dispositivos");
        System.out.println("2. Registar Novo Dispositivo");
        System.out.println("3. Consultar Médias");
        System.out.println("0. Sair");
    }

    private void listarDispositivos() {
        System.out.println("--> [DEBUG] 1. A preparar pedido...");
        try {
            String url = BASE_URL + "/devices";
            System.out.println("--> [DEBUG] 2. A contactar: " + url);
            
            // Se bloquear aqui, é problema de rede/servidor
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            System.out.println("--> [DEBUG] 3. Resposta recebida! Status: " + response.getStatusCode());
            System.out.println("\n--- Lista de Dispositivos ---");
            System.out.println(response.getBody());
            
        } catch (Exception e) {
            System.err.println("--> [ERRO] Falha na comunicação: " + e.getMessage());
            e.printStackTrace(); // Ver o erro completo
        }
    }

    private void consultarMedias() {
        System.out.println("\n--- Consultar Médias ---");
        System.out.println("Escolha o nível de agregação:");
        System.out.println("1. ID do Dispositivo (ex: SENSOR_REST_01)");
        System.out.println("2. Sala (ex: C20)");
        System.out.println("3. Edificio (ex: Ciencias)");
        System.out.println("4. Piso (ex: 1)");
        System.out.print("> Opção: ");
        
        String opcao = scanner.next();
        String nivel = "";
        
        switch (opcao) {
            case "1": nivel = "id"; break;
            case "2": nivel = "sala"; break;
            case "3": nivel = "edificio"; break;
            case "4": nivel = "piso"; break; // ou "andar" dependendo do servidor
            default: System.out.println("Opção inválida."); return;
        }

        System.out.print("Introduza o valor (ex: Ciencias, C20, SENSOR_01): ");
        String valor = scanner.next();

        try {
            String url = BASE_URL + "/metrics/average?level=" + nivel + "&id=" + valor;
            String resultado = restTemplate.getForObject(url, String.class);
            
            if (resultado == null || resultado.isEmpty()) {
                System.out.println("⚠️ Recebido resultado vazio. Verifique se o nome está exato.");
            } else {
                System.out.println("\n📊 Resultado das Médias:");
                System.out.println(resultado);
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar: " + e.getMessage());
            System.err.println("Verifique se o nome (Sala/Edificio) está igual ao registo (Maiúsculas/Minúsculas).");
        }
    }

    private void registarDispositivo() {
        System.out.println("\n--- Novo Registo ---");
        
        // texto sem espaços 
        System.out.print("ID (ex: SENSOR_01): ");
        String id = scanner.next();
        
        System.out.print("Protocolo (REST/MQTT/GRPC): ");
        String protocolo = scanner.next();
        
        System.out.print("Edifício (ex: Ciencias): ");
        String edificio = scanner.next();
        
        System.out.print("Sala (ex: B01): ");
        String sala = scanner.next();

        System.out.print("Departamento (ex: Informatica): ");
        String departamento = scanner.next();

        System.out.print("Piso (ex: 1): ");
        String piso = scanner.next();

        // Atualizar o JSON para incluir departamento e piso
        String jsonBody = String.format(
            "{\"id\":\"%s\", \"protocolo\":\"%s\", \"edificio\":\"%s\", \"sala\":\"%s\", \"departamento\":\"%s\", \"piso\":\"%s\", \"ativo\":true}", 
            id, protocolo, edificio, sala, departamento, piso
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            String url = BASE_URL + "/devices";
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("Sucesso! Dispositivo " + id + " registado.");
        } catch (Exception e) {
            System.err.println("Erro ao registar: " + e.getMessage());
        }
}
}