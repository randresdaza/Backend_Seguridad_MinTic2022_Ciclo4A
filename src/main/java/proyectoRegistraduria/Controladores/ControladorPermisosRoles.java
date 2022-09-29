package proyectoRegistraduria.Controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proyectoRegistraduria.Modelos.Permiso;
import proyectoRegistraduria.Modelos.PermisosRoles;
import proyectoRegistraduria.Modelos.Rol;
import proyectoRegistraduria.Repositorios.RepositorioPermiso;
import proyectoRegistraduria.Repositorios.RepositorioPermisosRoles;
import proyectoRegistraduria.Repositorios.RepositorioRol;
import java.util.List;
import proyectoRegistraduria.Excepciones.NotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/permisos-roles")
public class ControladorPermisosRoles {
    @Autowired
    private RepositorioPermisosRoles miRepositorioPermisoRoles;

    @Autowired
    private RepositorioPermiso miRepositorioPermiso;

    @Autowired
    private RepositorioRol miRepositorioRol;


    @GetMapping("")
    public List<PermisosRoles> index(){
        return this.miRepositorioPermisoRoles.findAll();
    }

    /**
     * Asignación rol y permiso
     * @param id_rol
     * @param id_permiso
     * @return
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("rol/{id_rol}/permiso/{id_permiso}")
    public PermisosRoles create(@PathVariable String id_rol,@PathVariable String id_permiso){
        PermisosRoles nuevo=new PermisosRoles();
        Rol elRol=this.miRepositorioRol.findById(id_rol).get();
        Permiso elPermiso=this.miRepositorioPermiso.findById(id_permiso).get();
        if (elRol!=null && elPermiso!=null){
            nuevo.setPermiso(elPermiso);
            nuevo.setRol(elRol);
            return this.miRepositorioPermisoRoles.save(nuevo);
        }else{
            return null;
        }
    }

    @GetMapping("{id}")
    public PermisosRoles show(@PathVariable String id){
        if (!miRepositorioPermisoRoles.existsById(id)){
            throw new NotFoundException(("Permiso-Rol: ".concat(id.toString().concat(" no existe"))));
        }
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles
                .findById(id)
                .orElse(null);
        return permisosRolesActual;
    }

    /**
     * Modificación Rol y Permiso
     * @param id
     * @param id_rol
     * @param id_permiso
     * @return
     */
    @PutMapping("{id}/rol/{id_rol}/permiso/{id_permiso}")
    public PermisosRoles update(@PathVariable String id,@PathVariable String id_rol,@PathVariable String id_permiso){
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles
                .findById(id)
                .orElse(null);
        Rol elRol=this.miRepositorioRol.findById(id_rol).get();
        Permiso elPermiso=this.miRepositorioPermiso.findById(id_permiso).get();
        if(permisosRolesActual!=null && elPermiso!=null && elRol!=null){
            permisosRolesActual.setPermiso(elPermiso);
            permisosRolesActual.setRol(elRol);
            return this.miRepositorioPermisoRoles.save(permisosRolesActual);
        }else{
            return null;
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        PermisosRoles permisosRolesActual=this.miRepositorioPermisoRoles.findById(id).orElse(null);
        if (permisosRolesActual==null) {
            throw new NotFoundException(("Permiso Rol: ".concat(id.toString().concat(" no existe"))));
        }
        this.miRepositorioPermisoRoles.delete(permisosRolesActual);
        return ResponseEntity.ok().body("Permiso Rol: ".concat(id.toString().concat(" Eliminado correctamente")));

    }


    @GetMapping("validar-permiso/rol/{id_rol}")
    public PermisosRoles getPermiso(@PathVariable String id_rol,@RequestBody Permiso infoPermiso){
        Permiso elPermiso=this.miRepositorioPermiso.getPermiso(infoPermiso.getUrl(),infoPermiso.getMetodo());
        Rol elRol=this.miRepositorioRol.findById(id_rol).get();
        if (elPermiso!=null && elRol!=null){
            return this.miRepositorioPermisoRoles.getPermisoRol(elRol.getId(),elPermiso.getId());
        }else{
            return null;
        }
    }
}