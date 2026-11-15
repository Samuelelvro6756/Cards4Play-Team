package cards4play.modelo;

public abstract class Usuario {

    private String id;
    private String nombreUsuario;
    private String contrasena;
    private Rol rol;

    protected Usuario(String id, String nombreUsuario, String contrasena, Rol rol) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getId() { return id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public Rol getRol() { return rol; }

    /** Solo para guardar en usuarios.json. Nunca se devuelve en una respuesta de la API. */
    public String getContrasena() { return contrasena; }

    public boolean autenticar(String usuario, String clave) {
        return nombreUsuario.equals(usuario) && contrasena.equals(clave);
    }
}
