package novo.projeto.kabum.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.Categoria;
import novo.projeto.kabum.model.dto.CategoriaDTO;
import novo.projeto.kabum.repository.CategoriaRepository;
import novo.projeto.kabum.utils.CategoriaMapper;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    public List<CategoriaDTO> listarTodos() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toDTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO salvarCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = categoriaMapper.toEntity(categoriaDTO);
        Categoria salvo = categoriaRepository.save(categoria);
        return categoriaMapper.toDTO(salvo);
    }

    public Optional<CategoriaDTO> buscarPorIdCategoria(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDTO);
    }

    public void deletarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoria com ID " + id + " não encontrada.");
        }
        categoriaRepository.deleteById(id);
    }

    public CategoriaDTO atualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        return categoriaRepository.findById(id)
                .map(cat -> {
                    cat.setCategoriaNome(categoriaDTO.getCategoriaNome());
                    cat.setCategoriaDescricao(categoriaDTO.getCategoriaDescricao());
                    Categoria atualizado = categoriaRepository.save(cat);
                    return categoriaMapper.toDTO(atualizado);
                })
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
    }
}
