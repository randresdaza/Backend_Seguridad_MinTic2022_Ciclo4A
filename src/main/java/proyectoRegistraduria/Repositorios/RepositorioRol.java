package proyectoRegistraduria.Repositorios;
import proyectoRegistraduria.Modelos.Rol;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RepositorioRol extends MongoRepository<Rol,String> {
}
