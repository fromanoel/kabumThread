# COMANDOS NECESSARIOS:

## Os JSONS de cliente, categorias e pedidos foram inseridos manualmente no SimuladorPedidos.java.

1. INICIAR KABUMAPPLICATION.JAVA
2. INICIAR SIMULADORPEDIDOS.JAVA
3. Mudar o número de pedidos de requisições em SimuladorPedidos.java de acordo com a necessidade. Exemplo: 10000 requisições
           int numeroDeRequisicoes = 10000;
4. Mudar o modo da requisição em SimuladorPedidos.java de acordo com a necessidade. Ex: pessimista, otimista ou noLock.
        String modo = "noLock"; // Modo: pessimista, otimista, noLock
5. Também é possível mudar o número de threads no SimuladorPedidos.java
        ExecutorService executor = Executors.newFixedThreadPool(10);
