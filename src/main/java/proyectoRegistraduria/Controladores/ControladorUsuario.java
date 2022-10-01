package proyectoRegistraduria.Controladores;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;
import proyectoRegistraduria.Modelos.Rol;
import proyectoRegistraduria.Modelos.Usuario;
import proyectoRegistraduria.Repositorios.RepositorioRol;
import proyectoRegistraduria.Repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import proyectoRegistraduria.Excepciones.NotFoundException;
@CrossOrigin
@RestController
@RequestMapping("/usuarios")

public class ControladorUsuario {

    @Autowired
    private RepositorioUsuario miRepositorioUsuario;
    @Autowired
    private RepositorioRol miRepositorioRol;
    @GetMapping("")
    public List<Usuario> index(){
        return this.miRepositorioUsuario.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Usuario create(@RequestBody  Usuario infoUsuario){
        //Se valida que correo indicado no exista
        List<Usuario> usuarioCorreo = this.miRepositorioUsuario.findByCorreo(infoUsuario.getCorreo());
        if (usuarioCorreo.size()>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El correo indicado ya existe: "+ infoUsuario.getCorreo());
        }
        //si correo no existe se crea usuario
        infoUsuario.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
        return this.miRepositorioUsuario.save(infoUsuario);
    }

    //Mostrar detalle de un usuario
    @GetMapping("{id}")
    public Usuario show(@PathVariable String id){
        if (!miRepositorioUsuario.existsById(id)){
            throw new NotFoundException(("Usuario: ".concat(id.toString().concat(" no existe"))));
        }
        Usuario usuarioActual=this.miRepositorioUsuario.findById(id).orElse(null);
        return usuarioActual;
    }

    @PutMapping("{id}")
    public Usuario update(@PathVariable String id,@RequestBody  Usuario infoUsuario){
        Usuario usuarioActual = this.miRepositorioUsuario.findById(id).orElse(null);
        if(usuarioActual == null) {
            throw new NotFoundException(("Usuario: ".concat(id.toString().concat(" no existe"))));
        }
        List<Usuario> usuarioCorreo = this.miRepositorioUsuario.findByCorreo(infoUsuario.getCorreo());
        if (usuarioCorreo.size()>0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El correo indicado ya existe: "+ infoUsuario.getCorreo());
        }
        else{
            usuarioActual.setSeudonimo(infoUsuario.getSeudonimo());
            usuarioActual.setCorreo(infoUsuario.getCorreo());
            usuarioActual.setContrasena(convertirSHA256(infoUsuario.getContrasena()));
            return this.miRepositorioUsuario.save(usuarioActual);
        }
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        Usuario usuarioActual = this.miRepositorioUsuario.findById(id).orElse(null);
        if(usuarioActual == null) {
            throw new NotFoundException(("Usuario: ".concat(id.toString().concat(" no existe"))));
        }
        this.miRepositorioUsuario.delete(usuarioActual);
        return ResponseEntity.ok().body("Usuario: ".concat(id.toString().concat(" Eliminado correctamente")));
    }
    /**
     * Relación (1 a n) entre rol y usuario
     * @param id
     * @param id_rol
     * @return
     */
    @PutMapping("/{id}/rol/{id_rol}")
    public Usuario asignarRolAUsuario(@PathVariable String id,@PathVariable String id_rol){
        Usuario usuarioActual=this.miRepositorioUsuario
                .findById(id)
                .orElse(null);
        Rol rolActual=this.miRepositorioRol
                .findById(id_rol)
                .orElse(null);
        if (usuarioActual!=null && rolActual!=null){
            usuarioActual.setRol(rolActual);
            return this.miRepositorioUsuario.save(usuarioActual);
        }else{
            return null;
        }

    }

    public String convertirSHA256(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    @PostMapping("validate")
    public Usuario validate(@RequestBody Usuario info, final HttpServletResponse response) throws IOException {
        Usuario user = this.miRepositorioUsuario.getUserByEmail(info.getCorreo());
        String cipher_password = convertirSHA256(info.getContrasena());
        if(user != null && user.getContrasena().equals(cipher_password)){
            user.setContrasena("");
            return user;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }

    @PostMapping("/validar")
    public Usuario validar(@RequestBody Usuario infoUsuario, final HttpServletResponse response)
            throws IOException{
        Usuario usuarioActual=this.miRepositorioUsuario.getUserByEmail(infoUsuario.getCorreo());
        if (usuarioActual!=null && usuarioActual.getContrasena().equals(convertirSHA256(infoUsuario.getContrasena()))){
            usuarioActual.setContrasena("");
            return usuarioActual;
        }else{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
    }
}
