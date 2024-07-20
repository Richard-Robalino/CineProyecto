import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JComboBox<String> comboRol;
    private JButton btnIniciarSesion;

    public Login() {
        super("Login");
        setLayout(new FlowLayout());

        JLabel lblUsuario = new JLabel("Usuario:");
        add(lblUsuario);

        txtUsuario = new JTextField(20);
        add(txtUsuario);

        JLabel lblContrasena = new JLabel("Contraseña:");
        add(lblContrasena);

        txtContrasena = new JPasswordField(20);
        add(txtContrasena);

        JLabel lblRol = new JLabel("Rol:");
        add(lblRol);

        String[] roles = {"ADMINISTRADOR", "CLIENTE"}; // Usar roles en mayúsculas para la comparación
        comboRol = new JComboBox<>(roles);
        add(comboRol);

        btnIniciarSesion = new JButton("Iniciar Sesión");
        add(btnIniciarSesion);

        btnIniciarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usuario = txtUsuario.getText();
                String contrasena = new String(txtContrasena.getPassword());
                String rolSeleccionado = ((String) comboRol.getSelectedItem()).toUpperCase(); // Convertir a mayúsculas el rol seleccionado

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
                    String query = "SELECT * FROM usuarios WHERE username=? AND password=? AND rol=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, usuario);
                        pstmt.setString(2, contrasena);
                        pstmt.setString(3, rolSeleccionado);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
                            JOptionPane.showMessageDialog(null, "Login exitoso como " + rolSeleccionado);

                            if (rolSeleccionado.equals("ADMINISTRADOR")) {
                                AdminWindow ventanaAdmin = new AdminWindow();
                                ventanaAdmin.setVisible(true);
                            } else if (rolSeleccionado.equals("CLIENTE")) {
                                ClientWindow ventanaCliente = new ClientWindow();
                                ventanaCliente.setVisible(true);
                            }

                            dispose(); // Cierra la ventana de login después del login exitoso
                        } else {
                            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace(); // Esto te ayudará a depurar el problema en la conexión
                }
            }
        });

        setSize(300, 250); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login ventanaLogin = new Login();
            ventanaLogin.setVisible(true);
        });
    }
}
