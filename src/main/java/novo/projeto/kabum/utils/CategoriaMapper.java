package novo.projeto.kabum.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import novo.projeto.kabum.model.Categoria;
import novo.projeto.kabum.model.dto.CategoriaDTO;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    Categoria toEntity(CategoriaDTO categoriaDTO);

    CategoriaDTO toDTO(Categoria categoria);

    default Set<Categoria> toEntitySet(Set<CategoriaDTO> categoriaDTOs) {
        if (categoriaDTOs == null || categoriaDTOs.isEmpty()) {
            return new HashSet<>();
        }
        return categoriaDTOs.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }
}