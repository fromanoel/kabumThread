package novo.projeto.kabum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.Produto;
import novo.projeto.kabum.model.dto.ProdutoDTO;
import novo.projeto.kabum.repository.ProdutosRepository;
import novo.projeto.kabum.utils.ProdutoMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoService {
    private final ProdutosRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public void salvarProduto(ProdutoDTO produtoDTO) {
        try {
            Produto produto = produtoMapper.toEntity(produtoDTO);
            produtoRepository.save(produto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o produto: " + e.getMessage(), e);
        }
    }

    public ProdutoDTO buscarPorIdProduto(Long id) {
        return produtoRepository.findById(id)
                .map(produtoMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Produto com ID " + id + " não encontrado."));
    }

    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deletarProduto(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Produto com ID " + id + " não encontrado.");
        }
        produtoRepository.deleteById(id);
    }

    public ProdutoDTO atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        return produtoRepository.findById(id)
                .map(produto -> {
                    produto.setProdutoNome(produtoDTO.getProdutoNome());
                    produto.setProdutoCategoria(produtoDTO.getProdutoCategoria());
                    produto.setProdutoPreco(produtoDTO.getProdutoPreco());
                    produto.setProdutoUnidadesEmEstoque(produtoDTO.getProdutoUnidadesEmEstoque());
                    produto.setProdutoImagem(produtoDTO.getProdutoImagem());
                    Produto produtoAtualizado = produtoRepository.save(produto);
                    return produtoMapper.toDTO(produtoAtualizado);
                })
                .orElseThrow(() -> new RuntimeException("Produto com ID " + id + " não encontrado."));
    }
}
