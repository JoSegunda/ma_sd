package pt.client_gRPC.client_gRPC;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pt.sd.server.grpc.*; // Gerado pelo plugin 

@SpringBootApplication
public class ClientGRpcApplication implements CommandLineRunner {

    @GrpcClient("server-grpc") // Configurado no application.properties
    private MetricasServiceGrpc.MetricasServiceBlockingStub stub;

    public static void main(String[] args) {
        SpringApplication.run(ClientGRpcApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        GeradorSimulado gerador = new GeradorSimulado();
        String idDispositivo = "SENSOR_GRPC_01"; // Deve estar registado na BD [cite: 96]

        while (true) {
            gerador.gerarNovosDados();

            // Criar o pedido binário forte [cite: 38, 39]
            PedidoMetrica pedido = PedidoMetrica.newBuilder()
                    .setId(idDispositivo)
                    .setTemperatura(gerador.getTemperatura())
                    .setHumidade(gerador.getHumidade())
                    .setTempo(gerador.getTimestampAtual())
                    .build();

            try {
                // Chamada síncrona [cite: 37, 45]
                RespostaMetrica resposta = stub.enviarMetrica(pedido);
                System.out.println("gRPC: " + resposta.getMensagem());
            } catch (Exception e) {
                System.err.println("Erro na ligação gRPC: " + e.getMessage());
            }

            Thread.sleep(5000); // Envia a cada 5 segundos [cite: 43]
        }
    }
}
