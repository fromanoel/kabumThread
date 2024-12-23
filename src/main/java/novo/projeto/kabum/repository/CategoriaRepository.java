package novo.projeto.kabum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import novo.projeto.kabum.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
