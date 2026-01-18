package pt.sd.server.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.sd.server.model.Metricas;
import pt.sd.server.service.MonitorizacaoService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@GrpcService
public class MetricasGrpcService extends MetricasServiceGrpc.MetricasServiceImplBase {

    @Autowired
    private MonitorizacaoService monitorizacaoService;

    @Override
    public void enviarMetrica(PedidoMetrica request, StreamObserver<RespostaMetrica> responseObserver) {
        try {
            Metricas metrica = new Metricas();
            
            metrica.setIdDispositivo(request.getId()); 
            
            metrica.setTemperatura(request.getTemperatura());
            metrica.setHumidade(request.getHumidade());
            
            if (request.getTempo() != null && !request.getTempo().isEmpty()) {
                metrica.setTempo(LocalDateTime.parse(request.getTempo(), DateTimeFormatter.ISO_DATE_TIME));
            } else {
                metrica.setTempo(LocalDateTime.now());
            }

            boolean guardado = monitorizacaoService.processarMetrica(metrica);

            RespostaMetrica response;
            if (guardado) {
                response = RespostaMetrica.newBuilder()
                        .setStatus("OK")
                        .setMensagem("Métrica gRPC gravada.")
                        .build();
            } else {
                response = RespostaMetrica.newBuilder()
                        .setStatus("ERRO")
                        .setMensagem("Dispositivo não registado ou inválido.")
                        .build();
            }

            // 4. Enviar
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            System.out.println("gRPC: Pedido processado para " + request.getId() + " -> " + (guardado ? "SUCESSO" : "RECUSADO"));

        } catch (Exception e) {
            System.err.println("gRPC Erro Crítico: " + e.getMessage());
            responseObserver.onError(e);
        }
    }
}