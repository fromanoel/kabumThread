package novo.projeto.kabum.simular;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import novo.projeto.kabum.model.dto.DetalhesPedidoDTO;
import novo.projeto.kabum.model.dto.PedidoDTO;

public class SimuladorPedidos {

    private static final String BASE_URL = "http://localhost:8080";
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        int numeroDeRequisicoes = 100;
        String modo = "noLock"; // Modo: pessimista, otimista, noLock

        Long clienteId = criarCliente();
        Long categoriaId = criarCategoria();
        Long produtoId = criarProduto(categoriaId);

        simularPedidos(numeroDeRequisicoes, modo, clienteId, produtoId);
    }

    private static void simularPedidos(int numeroDeRequisicoes, String modo, Long clienteId, Long produtoId) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < numeroDeRequisicoes; i++) {
            executor.submit(() -> {
                try {

                    PedidoDTO pedido = criarPedidoSimulado(clienteId, produtoId);

                    String url = BASE_URL + "/pedidos?mode=" + modo;

                    PedidoDTO resposta = restTemplate.postForObject(url, pedido, PedidoDTO.class);
                    System.out.println("Pedido criado: " + resposta);
                } catch (Exception e) {
                    System.err.println("Erro ao criar pedido: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Simulação finalizada.");
    }

    private static PedidoDTO criarPedidoSimulado(Long clienteId, Long produtoId) {
        PedidoDTO pedido = new PedidoDTO();
        pedido.setClienteId(clienteId);
        pedido.setDetalhes(Collections.singletonList(criarDetalhePedidoSimulado(produtoId)));
        return pedido;
    }

    private static DetalhesPedidoDTO criarDetalhePedidoSimulado(Long produtoId) {
        DetalhesPedidoDTO detalhe = new DetalhesPedidoDTO();
        detalhe.setDesconto(0.0);
        detalhe.setPrecoVenda(1999.99);
        detalhe.setProdutoId(produtoId);
        detalhe.setQuantidade((short) 1);
        return detalhe;
    }

    private static Long criarCliente() {
        String url = BASE_URL + "/clientes";
        String clienteRequest = """
                                    {
                  "nome": "Fernanda Almeida",
                  "cargo": "Gerente de Projetos",
                  "endereco": "Praça da Liberdade, 200",
                  "cidade": "Belo Horizonte",
                  "cep": "30140-010",
                  "pais": "Brasil",
                  "telefone": "(31)99887-6655",
                  "fax": "(31)34567-4321"
                }

                                """;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(clienteRequest, headers);

        var response = restTemplate.postForObject(url, request, Object.class);
        System.out.println("Cliente criado: " + response);
        return extrairId(response);
    }

    private static Long criarCategoria() {
        String url = BASE_URL + "/categorias";
        String categoriaRequest = """
                    {
                      "categoriaNome": "Video games",
                      "categoriaDescricao": "Consoles de mesa e portáteis"
                    }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(categoriaRequest, headers);

        var response = restTemplate.postForObject(url, request, Object.class);
        System.out.println("Categoria criada: " + response);
        return extrairId(response);
    }

    private static Long criarProduto(Long categoriaId) {
        String url = BASE_URL + "/produtos";
        String produtoRequest = String.format("""
                    {
                      "produtoNome": "Playstation 5",
                      "produtoCategoria": %d,
                      "produtoPreco": 1999.99,
                      "produtoUnidadesEmEstoque": 1000,
                      "produtoImagem": "Playstation5.jpg"
                    }
                """, categoriaId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<>(produtoRequest, headers);

        var response = restTemplate.postForObject(url, request, Object.class);
        System.out.println("Produto criado: " + response);
        return extrairId(response);
    }

    private static Long extrairId(Object response) {

        return 1L;
    }
}
