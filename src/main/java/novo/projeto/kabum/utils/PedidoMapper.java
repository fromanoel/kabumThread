package novo.projeto.kabum.utils;

import org.mapstruct.Mapper;

import novo.projeto.kabum.model.Pedido;
import novo.projeto.kabum.model.dto.PedidoDTO;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    PedidoDTO toDTO(Pedido pedido);

    Pedido toEntity(PedidoDTO pedidoDTO);
}