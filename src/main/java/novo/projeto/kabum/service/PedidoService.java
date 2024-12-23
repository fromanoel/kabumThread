package novo.projeto.kabum.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.Cliente;
import novo.projeto.kabum.model.DetalhesPedido;
import novo.projeto.kabum.model.Pedido;
import novo.projeto.kabum.model.Produto;
import novo.projeto.kabum.model.dto.DetalhesPedidoDTO;
import novo.projeto.kabum.model.dto.PedidoDTO;
import novo.projeto.kabum.repository.ClienteRepository;
import novo.projeto.kabum.repository.PedidoRepository;
import novo.projeto.kabum.repository.ProdutosRepository;
import novo.projeto.kabum.utils.DetalhesPedidoMapper;
import novo.projeto.kabum.utils.PedidoMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PedidoMapper pedidoMapper;
    private final DetalhesPedidoMapper detalhesPedidoMapper;
    private final ProdutosRepository produtosRepository;
    private final ClienteRepository clienteRepository;

    public PedidoDTO criarPedido(PedidoDTO pedidoDTO, String mode) {
        Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido.setCliente(cliente);
        List<DetalhesPedido> detalhes = pedidoDTO.getDetalhes().stream()
                .map((DetalhesPedidoDTO d) -> criarDetalhePedido(d, pedido, mode))
                .collect(Collectors.toList());

        pedido.setDetalhes(detalhes);
        Pedido salvo = pedidoRepository.save(pedido);
        return pedidoMapper.toDTO(salvo);
    }

    private DetalhesPedido criarDetalhePedido(DetalhesPedidoDTO detalheDTO, Pedido pedido, String mode) {
        DetalhesPedido detalhe = detalhesPedidoMapper.toEntity(detalheDTO);

        Produto produto = obterProdutoComLock(detalheDTO.getProdutoId(), mode);

        if (produto.getProdutoUnidadesEmEstoque() < detalheDTO.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getProdutoNome());
        }

        produto.setProdutoUnidadesEmEstoque(produto.getProdutoUnidadesEmEstoque() - detalheDTO.getQuantidade());

        detalhe.setProduto(produto);
        detalhe.setPedido(pedido);

        return detalhe;
    }

    private Produto obterProdutoComLock(Long produtoId, String mode) {
        Optional<Produto> produtoOpt;
        if ("pessimistic".equalsIgnoreCase(mode)) {
            produtoOpt = produtosRepository.findByIdWithPessimisticLock(produtoId);
        } else {
            produtoOpt = produtosRepository.findByIdWithOptimisticLock(produtoId);
        }

        return produtoOpt.orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public List<PedidoDTO> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorIdPedido(Long id) {
        return pedidoRepository.findById(id)
                .map(pedidoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Pedido com ID " + id + " não encontrado."));
    }

    public void deletarPedido(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException("Pedido com ID " + id + " não encontrado.");
        }
        pedidoRepository.deleteById(id);
    }

    public PedidoDTO atualizarPedido(Long id, PedidoDTO pedidoDTO, String mode) {
        return pedidoRepository.findById(id).map(pedido -> {
            Cliente cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
            pedido.setCliente(cliente);

            for (DetalhesPedido detalheAntigo : pedido.getDetalhes()) {
                Produto produtoAntigo = detalheAntigo.getProduto();
                produtoAntigo.setProdutoUnidadesEmEstoque(
                        produtoAntigo.getProdutoUnidadesEmEstoque() + detalheAntigo.getQuantidade());
            }

            pedido.getDetalhes().clear();

            List<DetalhesPedido> novosDetalhes = pedidoDTO.getDetalhes().stream()
                    .map(d -> criarDetalhePedido(d, pedido, mode))
                    .collect(Collectors.toList());

            pedido.getDetalhes().addAll(novosDetalhes);

            Pedido atualizado = pedidoRepository.save(pedido);
            return pedidoMapper.toDTO(atualizado);
        }).orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    }

}
