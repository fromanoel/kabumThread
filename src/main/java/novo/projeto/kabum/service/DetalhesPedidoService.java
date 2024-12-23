package novo.projeto.kabum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.DetalhesPedido;
import novo.projeto.kabum.model.Pedido;
import novo.projeto.kabum.model.Produto;
import novo.projeto.kabum.model.dto.DetalhesPedidoDTO;
import novo.projeto.kabum.repository.DetalhesPedidoRepository;
import novo.projeto.kabum.repository.PedidoRepository;
import novo.projeto.kabum.repository.ProdutosRepository;
import novo.projeto.kabum.utils.DetalhesPedidoMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class DetalhesPedidoService {

    private final DetalhesPedidoRepository detalhesPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutosRepository produtoRepository;
    private final DetalhesPedidoMapper detalhesPedidoMapper;

    public DetalhesPedidoDTO criarDetalhe(DetalhesPedidoDTO dto) {
        DetalhesPedido detalhe = detalhesPedidoMapper.toEntity(dto);
        Pedido pedido = buscarPedido(detalhe.getPedido().getPedidoId());
        Produto produto = buscarProduto(dto.getProdutoId());

        detalhe.setPedido(pedido);
        detalhe.setProduto(produto);

        DetalhesPedido salvo = detalhesPedidoRepository.save(detalhe);
        return detalhesPedidoMapper.toDTO(salvo);
    }

    public DetalhesPedidoDTO atualizarDetalhe(Long id, DetalhesPedidoDTO dto) {
        return detalhesPedidoRepository.findById(id)
                .map(detalhe -> {
                    Produto produto = produtoRepository.findById(dto.getProdutoId())
                            .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
                    detalhe.setProduto(produto);
                    detalhe.setPrecoVenda(dto.getPrecoVenda());
                    detalhe.setQuantidade(dto.getQuantidade());
                    detalhe.setDesconto(dto.getDesconto());
                    DetalhesPedido atualizado = detalhesPedidoRepository.save(detalhe);
                    return detalhesPedidoMapper.toDTO(atualizado);
                })
                .orElseThrow(() -> new RuntimeException("Detalhe não encontrado."));
    }

    private Pedido buscarPedido(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
    }

    private Produto buscarProduto(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado."));
    }

    public void deletarDetalhe(Long detalheId) {
        detalhesPedidoRepository.deleteById(detalheId);
    }

    public DetalhesPedidoDTO buscarPorId(Long detalheId) {
        return detalhesPedidoRepository.findById(detalheId)
                .map(detalhesPedidoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Detalhe não encontrado."));
    }

    public List<DetalhesPedidoDTO> listarTodos() {
        return detalhesPedidoRepository.findAll().stream()
                .map(detalhesPedidoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
