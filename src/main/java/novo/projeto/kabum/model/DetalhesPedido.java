package novo.projeto.kabum.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class DetalhesPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detalhePedidoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private Double precoVenda;
    private Short quantidade;
    private Double desconto;
}