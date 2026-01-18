package pt.sd.server.grpc;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import pt.sd.server.model.Metricas;
import pt.sd.server.service.MonitorizacaoService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@GrpcService // Anotação necessária para o starter gRPC detetar o serviço
public class MetricasGrpcService extends MetricasServiceGrpc.MetricasServiceImplBase {

    @Autowired
    private MonitorizacaoService monitorizacaoService;

    @Override
    public void enviarMetrica(PedidoMetrica request, StreamObserver<RespostaMetrica> responseObserver) {
        // 1. Converter o pedido gRPC para a nossa Entidade JPA
        Metricas metrica = new Metricas();
        metrica.setIdDispositivo(request.getIdDispositivo());
        metrica.setTemperatura(request.getTemperatura());
        metrica.setHumidade(request.getHumidade());
        
        // Converter String ISO 8601 para LocalDateTime 
        metrica.setTempo(LocalDateTime.parse(request.getTempo(), DateTimeFormatter.ISO_DATE_TIME));

        // 2. Processar e Validar (Regra: descartar se não existir dispositivo) 
        boolean guardado = monitorizacaoService.processarMetrica(metrica);

        // 3. Preparar a resposta gRPC 
        RespostaMetrica response;
        if (guardado) {
            response = RespostaMetrica.newBuilder()
                    .setStatus("OK")
                    .setMensagem("Métrica gRPC processada.")
                    .build();
        } else {
            response = RespostaMetrica.newBuilder()
                    .setStatus("ERRO")
                    .setMensagem("Dispositivo inválido.")
                    .build();
        }

        // 4. Enviar resposta e encerrar a chamada
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}