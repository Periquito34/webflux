package com.co.webflux.services;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ScheduledService {

    private final WebClient webClient;

    public ScheduledService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083").build(); // Cambia la URL por la de tu orquestador
    }

    @Scheduled(fixedRate = 30000) // Cada 2 minutos (en milisegundos)
    public void callOrchestrator() {

        String requestBody = """
        {
            "data": [
                {
                    "header": {
                        "id": "12345",
                        "type": "TestGiraffeRefrigerator"
                    }
                }
            ]
        }
        """;

        Mono<String> response = webClient.post()
                .uri("/orchestrator/execute") // El endpoint de tu orquestador
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);

        response.subscribe(
                result -> System.out.println("Orquestador respondió: " + result),
                error -> System.err.println("Error al invocar orquestador: " + error.getMessage())
                );
    }

}