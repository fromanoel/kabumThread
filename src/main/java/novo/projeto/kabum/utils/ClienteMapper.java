package novo.projeto.kabum.utils;

import org.mapstruct.Mapper;

import novo.projeto.kabum.model.Cliente;
import novo.projeto.kabum.model.dto.ClienteDTO;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteDTO toDTO(Cliente cliente);

    Cliente toEntity(ClienteDTO clienteDTO);
}