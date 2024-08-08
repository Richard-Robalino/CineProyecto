import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgregarCliente extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionClientes gestionClientes;

    public AgregarCliente(GestionClientes gestionClientes) {
        super("Agregar Cliente");
        this.gestionClientes = gestionClientes;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Agregar Cliente", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        gbc.gridx = 0;
        add(lblUsername, gbc);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        add(lblPassword, gbc);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel lblRol = new JLabel("Rol:");
        comboRol = new JComboBox<>(new String[]{"CLIENTE", "ADMINISTRADOR"});
        add(lblRol, gbc);
        gbc.gridx = 1;
        add(comboRol, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String rol = (String) comboRol.getSelectedItem();

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "INSERT INTO usuarios (username, password, rol) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        pstmt.setString(3, rol);

                        int filasInsertadas = pstmt.executeUpdate();
                        if (filasInsertadas > 0) {
                            JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente");

                            dispose(); // Cierra la ventana actual
                            gestionClientes.setVisible(true); // Abre la ventana de GestionClientes
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al agregar el cliente");
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace();
                }
            }
        });
        add(btnGuardar, gbc);

        gbc.gridx = 1;
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                gestionClientes.setVisible(true);
            }
        });
        add(btnCancelar, gbc);

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            GestionClientes gestionClientes = new GestionClientes(null);
            new AgregarCliente(gestionClientes).setVisible(true);
        });
    }
}
