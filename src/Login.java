import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clase Login proporciona una interfaz para el inicio de sesión de usuarios.
 * Permite a los usuarios ingresar su nombre de usuario, contraseña y rol para autenticar el acceso a la aplicación.
 @author Richard Robalino
  * @version 1.1
 */
public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboRol;
    private JButton btnIniciarSesion;
    private int usuarioId;

    /**
     * Constructor de la clase Login.
     * Inicializa la interfaz de usuario para el inicio de sesión.
     */
    public Login() {
        super("Login");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Establecer fondo de color
        getContentPane().setBackground(new Color(200, 220, 240));

        // Título
        JLabel lblTitle = new JLabel("Inicio de Sesión", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        // Etiqueta Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        add(lblUsuario, gbc);

        // Campo de texto Usuario
        txtUsuario = new JTextField(20);
        gbc.gridx = 1;
        add(txtUsuario, gbc);

        gbc.gridy = 2;

        // Etiqueta Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        add(lblContrasena, gbc);

        // Campo de texto Contraseña
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        gbc.gridy = 3;

        // Etiqueta Rol
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        add(lblRol, gbc);

        // ComboBox Rol
        String[] roles = {"ADMINISTRADOR", "CLIENTE"};
        comboRol = new JComboBox<>(roles);
        gbc.gridx = 1;
        add(comboRol, gbc);

        gbc.gridy = 4;

        // Botón Iniciar Sesión
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciarSesion.setBackground(new Color(70, 130, 180));
        btnIniciarSesion.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        add(btnIniciarSesion, gbc);

        // Acción del botón Iniciar Sesión
        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String contrasena = new String(txtContrasena.getPassword());
                String rolSeleccionado = ((String) comboRol.getSelectedItem()).toUpperCase();

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "SELECT * FROM usuarios WHERE username=? AND password=? AND rol=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, usuario);
                        pstmt.setString(2, contrasena);
                        pstmt.setString(3, rolSeleccionado);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            usuarioId = rs.getInt("id");
                            JOptionPane.showMessageDialog(null, "Login exitoso como " + rolSeleccionado);

                            // Redirigir a la ventana correspondiente
                            if (rolSeleccionado.equals("ADMINISTRADOR")) {
                                AdminWindow ventanaAdmin = new AdminWindow();
                                ventanaAdmin.setVisible(true);
                            } else if (rolSeleccionado.equals("CLIENTE")) {
                                ClientWindow ventanaCliente = new ClientWindow(usuarioId); // Pasar el ID del usuario a ClientWindow
                                ventanaCliente.setVisible(true);
                            }

                            dispose();
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace();
                }
            }
        });

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método main.
     * Punto de entrada para la aplicación. Crea e inicia una instancia de la ventana Login.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login ventanaLogin = new Login();
            ventanaLogin.setVisible(true);
        });
    }
}
