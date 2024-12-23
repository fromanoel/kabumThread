package novo.projeto.kabum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import novo.projeto.kabum.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT p FROM Pedido p WHERE p.pedidoId = :id")
    Optional<Pedido> findByIdWithOptimisticLock(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Pedido p WHERE p.pedidoId = :id")
    Optional<Pedido> findByIdWithPessimisticLock(Long id);
}
