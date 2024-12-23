package novo.projeto.kabum.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import novo.projeto.kabum.model.dto.DetalhesPedidoDTO;
import novo.projeto.kabum.service.DetalhesPedidoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/detalhesPedido")
public class DetalhesPedidoController {
    private final DetalhesPedidoService detalhesPedidoService;

    @GetMapping
    public ResponseEntity<List<DetalhesPedidoDTO>> listarDetalhes() {
        return ResponseEntity.ok(detalhesPedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalhesPedidoDTO> buscarDetalhePorId(@PathVariable Long id) {
        return ResponseEntity.ok(detalhesPedidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<DetalhesPedidoDTO> criarDetalhe(@RequestBody DetalhesPedidoDTO detalheDTO) {
        DetalhesPedidoDTO novoDetalhe = detalhesPedidoService.criarDetalhe(detalheDTO);
        return new ResponseEntity<>(novoDetalhe, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDetalhe(@PathVariable Long id) {
        detalhesPedidoService.deletarDetalhe(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalhesPedidoDTO> atualizarDetalhe(@PathVariable Long id,
            @RequestBody DetalhesPedidoDTO detalheDTO) {
        DetalhesPedidoDTO detalheAtualizado = detalhesPedidoService.atualizarDetalhe(id, detalheDTO);
        return new ResponseEntity<>(detalheAtualizado, HttpStatus.OK);
    }
}
