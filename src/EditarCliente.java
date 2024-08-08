import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EditarCliente extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionClientes gestionClientes;
    private int clienteId;

    public EditarCliente(GestionClientes gestionClientes, int clienteId) {
        super("Editar Cliente");
        this.gestionClientes = gestionClientes;
        this.clienteId = clienteId;
        setLayout(new GridLayout(4, 2));
        setBackground(new Color(255, 255, 255)); // Fondo blanco

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsername.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblUsername);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblPassword);
        add(txtPassword);

        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(new Font("Arial", Font.BOLD, 14));
        lblRol.setForeground(new Color(50, 50, 50)); // Gris oscuro
        comboRol = new JComboBox<>(new String[]{"CLIENTE", "ADMINISTRADOR"});
        comboRol.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblRol);
        add(comboRol);

        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(34, 139, 34)); // Verde oscuro
        btnGuardar.setForeground(Color.WHITE); // Texto blanco
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword()); // Obtener texto de JPasswordField
                String rol = (String) comboRol.getSelectedItem();

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "UPDATE usuarios SET username=?, password=?, rol=? WHERE id=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        pstmt.setString(3, rol);
                        pstmt.setInt(4, clienteId);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Cliente actualizado exitosamente");
                        dispose(); // Cierra la ventana actual
                        GestionClientes ventanaGestionClientes = new GestionClientes(gestionClientes.adminWindow);
                        ventanaGestionClientes.setVisible(true); // Abre la ventana de GestionClientes
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace();
                }
            }
        });
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(255, 69, 58)); // Rojo claro
        btnCancelar.setForeground(Color.WHITE); // Texto blanco
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                GestionClientes ventanaGestionClientes = new GestionClientes(gestionClientes.adminWindow);
                ventanaGestionClientes.setVisible(true); // Abre la ventana de GestionClientes
            }
        });
        add(btnCancelar);

        cargarDatosCliente();

        setSize(350, 200); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private void cargarDatosCliente() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
            String query = "SELECT * FROM usuarios WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, clienteId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        txtUsername.setText(rs.getString("username"));
                        txtPassword.setText(rs.getString("password"));
                        comboRol.setSelectedItem(rs.getString("rol"));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
            ex.printStackTrace();
        }
    }
}
