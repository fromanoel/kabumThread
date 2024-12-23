package novo.projeto.kabum.utils;

import org.mapstruct.Mapper;

import novo.projeto.kabum.model.DetalhesPedido;
import novo.projeto.kabum.model.dto.DetalhesPedidoDTO;

@Mapper(componentModel = "spring")
public interface DetalhesPedidoMapper {
    DetalhesPedidoDTO toDTO(DetalhesPedido detalhesPedido);

    DetalhesPedido toEntity(DetalhesPedidoDTO detalhesPedidoDTO);
}