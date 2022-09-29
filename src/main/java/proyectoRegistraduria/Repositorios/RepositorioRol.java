package proyectoRegistraduria.Repositorios;
import proyectoRegistraduria.Modelos.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RepositorioRol extends MongoRepository<Rol,String> {
    List<Rol> findByNombre(String nombre);
}
