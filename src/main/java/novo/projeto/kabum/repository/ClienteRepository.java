package novo.projeto.kabum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import novo.projeto.kabum.model.Cliente;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
}
