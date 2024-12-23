package novo.projeto.kabum.utils;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import novo.projeto.kabum.model.Produto;
import novo.projeto.kabum.model.dto.ProdutoDTO;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoMapper INSTANCE = Mappers.getMapper(ProdutoMapper.class);

    ProdutoDTO toDTO(Produto produto);

    Produto toEntity(ProdutoDTO produtoDTO);
}