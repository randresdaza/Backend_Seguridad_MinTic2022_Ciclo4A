package proyectoRegistraduria.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyectoRegistraduria.Modelos.Permiso;
import proyectoRegistraduria.Modelos.Rol;
import proyectoRegistraduria.Repositorios.RepositorioPermiso;
import proyectoRegistraduria.Excepciones.NotFoundException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/permisos")
public class ControladorPermiso {
    @Autowired
    private RepositorioPermiso miRepositorioPermiso;

    @GetMapping("")
    public List<Permiso> index(){
        return this.miRepositorioPermiso.findAll();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Permiso create(@RequestBody  Permiso infoPermiso){


        return this.miRepositorioPermiso.save(infoPermiso);
    }
    @GetMapping("{id}")
    public Permiso show(@PathVariable String id){
        if (!miRepositorioPermiso.existsById(id)){
            throw new NotFoundException(("Permiso: ".concat(id.toString().concat(" no existe"))));
        }

        Permiso permisoActual=this.miRepositorioPermiso.findById(id).orElse(null);
        return permisoActual;
    }

    @PutMapping("{id}")
    public Permiso update(@PathVariable String id,@RequestBody  Permiso infoPermiso){
        Permiso permisoActual=this.miRepositorioPermiso.findById(id).orElse(null);
        if(permisoActual==null) {
            throw new NotFoundException(("Permiso: ".concat(id.toString().concat(" no existe"))));
        }else{
            permisoActual.setMetodo(infoPermiso.getMetodo());
            permisoActual.setUrl(infoPermiso.getUrl());
            return this.miRepositorioPermiso.save(permisoActual);
        }

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id) throws Exception{
        Permiso permisoActual=this.miRepositorioPermiso.findById(id).orElse(null);
        if (permisoActual ==null){
            throw new NotFoundException(("Permiso: ".concat(id.toString().concat(" no existe"))));
        }
        this.miRepositorioPermiso.delete(permisoActual);
        return ResponseEntity.ok().body("Permiso: ".concat(id.toString().concat(" Eliminado correctamente")));
    }
}