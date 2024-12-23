package novo.projeto.kabum.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.Cliente;
import novo.projeto.kabum.model.dto.ClienteDTO;
import novo.projeto.kabum.repository.ClienteRepository;
import novo.projeto.kabum.utils.ClienteMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteDTO salvarCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente salvo = clienteRepository.save(cliente);
        return clienteMapper.toDTO(salvo);
    }

    public ClienteDTO buscarPorIdCliente(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + id + " não encontrado."));
    }

    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deletarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente com ID " + id + " não encontrado.");
        }
        clienteRepository.deleteById(id);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNome(clienteDTO.getNome());
                    cliente.setCargo(clienteDTO.getCargo());
                    cliente.setEndereco(clienteDTO.getEndereco());
                    cliente.setCidade(clienteDTO.getCidade());
                    cliente.setCep(clienteDTO.getCep());
                    cliente.setPais(clienteDTO.getPais());
                    cliente.setTelefone(clienteDTO.getTelefone());
                    cliente.setFax(clienteDTO.getFax());
                    Cliente atualizado = clienteRepository.save(cliente);
                    return clienteMapper.toDTO(atualizado);
                })
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));
    }

}
