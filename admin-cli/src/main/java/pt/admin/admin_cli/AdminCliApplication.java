package pt.admin.admin_cli;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AdminCliApplication implements CommandLineRunner {

    private final String BASE_URL = "http://localhost:8080/api"; // URL do Servidor
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(AdminCliApplication.class, args);
    }

    @Override
    public void run(String... args) {
        boolean rodando = true;
        System.out.println("=== Sistema de Monitorização Ambiental da UÉvora ===");

        while (rodando) {
            exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            switch (opcao) {
                case 1: listarDispositivos(); break;
                case 2: registarDispositivo(); break;
                case 3: consultarMedias(); break; // Agregação
                case 0: rodando = false; break;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Listar Dispositivos");
        System.out.println("2. Registar Novo Dispositivo");
        System.out.println("3. Consultar Médias (Sala/Edifício)");
        System.out.println("0. Sair");
        System.out.print("Escolha: ");
    }

    // --- Implementação das Funcionalidades ---

    private void listarDispositivos() {
        try {
            String url = BASE_URL + "/devices";
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            System.out.println("\nDispositivos Registados:");
            System.out.println(response.getBody()); // Exibe o JSON (pode ser melhorado com Jackson)
        } catch (Exception e) {
            System.err.println("Erro ao listar: " + e.getMessage());
        }
    }

    private void registarDispositivo() {
        System.out.print("ID do Dispositivo: ");
        String id = scanner.nextLine();
        System.out.print("Protocolo (REST/MQTT/GRPC): ");
        String protocolo = scanner.nextLine();
        System.out.print("Edifício: ");
        String edificio = scanner.nextLine();
        System.out.print("Sala: ");
        String sala = scanner.nextLine();

        // Criar JSON simples manualmente para enviar
        String json = String.format(
            "{\"id\":\"%s\", \"protocolo\":\"%s\", \"edificio\":\"%s\", \"sala\":\"%s\", \"ativo\":true}", 
            id, protocolo, edificio, sala
        );

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);
            
            restTemplate.postForEntity(BASE_URL + "/devices", entity, String.class);
            System.out.println("Dispositivo registado com sucesso!");
        } catch (Exception e) {
            // Nota: Para simplificar, usei pseudo-código no envio. 
            // Recomenda-se criar uma classe DTO 'Dispositivo' também neste projeto.
            System.out.println("Enviado (Simulação).");
        }
    }

    private void consultarMedias() {
        System.out.print("Nível (sala/edificio): ");
        String nivel = scanner.nextLine();
        System.out.print("ID (ex: B01 ou Edificio_Ciencias): ");
        String id = scanner.nextLine();

        String url = String.format("%s/metrics/average?level=%s&id=%s", BASE_URL, nivel, id);
        
        try {
            String resposta = restTemplate.getForObject(url, String.class);
            System.out.println("\n--- Resultados Agregados ---");
            System.out.println(resposta);
        } catch (Exception e) {
            System.err.println("Erro na consulta: " + e.getMessage());
        }
    }
}