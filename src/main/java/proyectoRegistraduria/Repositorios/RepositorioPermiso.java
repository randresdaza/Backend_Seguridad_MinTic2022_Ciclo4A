package proyectoRegistraduria.Repositorios;
import proyectoRegistraduria.Modelos.Permiso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import proyectoRegistraduria.Modelos.Rol;

import java.util.List;

public interface RepositorioPermiso extends MongoRepository<Permiso,String>{
    @Query("{'url': ?0, 'metodo': ?1}")
    Permiso getPermiso(String url, String metodo);

 }
