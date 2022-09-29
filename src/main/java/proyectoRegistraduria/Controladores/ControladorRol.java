package proyectoRegistraduria.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import proyectoRegistraduria.Modelos.Rol;
import proyectoRegistraduria.Repositorios.RepositorioRol;
import proyectoRegistraduria.Excepciones.NotFoundException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/roles")
public class ControladorRol {
    @Autowired
    private RepositorioRol miRepositorioRol;


    @GetMapping("")
    public List<Rol> index(){
        return this.miRepositorioRol.findAll();
    }

    @GetMapping("{id}")
    public Rol show(@PathVariable String id){
        if (!miRepositorioRol.existsById(id)){
            throw new NotFoundException(("Rol: ".concat(id.toString().concat(" no existe"))));
        }

        Rol rolActual=this.miRepositorioRol.findById(id).orElse(null);
        return rolActual;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Rol create(@RequestBody  Rol infoRol){
        List<Rol> rolAct =this.miRepositorioRol.findByNombre((infoRol.getNombre()));
        if (rolAct.size()>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El Rol indicado ya existe: "+ infoRol.getNombre());
        }
        return this.miRepositorioRol.save(infoRol);
    }

    @PutMapping("{id}")
    public Rol update(@PathVariable String id,@RequestBody  Rol infoRol){
        Rol rolActual=this.miRepositorioRol.findById(id).orElse(null);
        if(rolActual == null) {
            throw new NotFoundException(("Rol: ".concat(id.toString().concat(" no existe"))));
        }
        List<Rol> rol = this.miRepositorioRol.findByNombre(infoRol.getNombre());
        if (rol.size()>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El Rol indicado ya existe: "+ infoRol.getNombre());
        }
        else {
            rolActual.setNombre(infoRol.getNombre());
            rolActual.setDescripcion(infoRol.getDescripcion());
            return this.miRepositorioRol.save(rolActual);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        Rol rolActual=this.miRepositorioRol.findById(id).orElse(null);
        if (rolActual==null){
            throw new NotFoundException(("Rol: ".concat(id.toString().concat(" no existe"))));
        }
        this.miRepositorioRol.delete(rolActual);
        return ResponseEntity.ok().body("Rol: ".concat(id.toString().concat(" Eliminado correctamente")));

    }
}