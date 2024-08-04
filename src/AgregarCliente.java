import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class AgregarCliente extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> comboRol;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JButton btnSubirFoto;
    private JLabel lblFoto;
    private File fotoFile;
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
        btnSubirFoto = new JButton("Subir Foto");
        lblFoto = new JLabel("No se ha seleccionado ninguna foto");
        btnSubirFoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(AgregarCliente.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fotoFile = fileChooser.getSelectedFile();
                    lblFoto.setText(fotoFile.getName());
                }
            }
        });
        add(btnSubirFoto, gbc);
        gbc.gridx = 1;
        add(lblFoto, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String rol = (String) comboRol.getSelectedItem();

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
                    String query = "INSERT INTO usuarios (username, password, rol, foto) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, username);
                        pstmt.setString(2, password);
                        pstmt.setString(3, rol);

                        if (fotoFile != null) {
                            try (FileInputStream fis = new FileInputStream(fotoFile)) {
                                pstmt.setBinaryStream(4, fis, (int) fotoFile.length());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, "Error al leer la foto");
                                return;
                            }
                        } else {
                            pstmt.setNull(4, Types.BLOB);
                        }

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
                dispose(); // Cierra la ventana actual
                gestionClientes.setVisible(true); // Abre la ventana de GestionClientes
            }
        });
        add(btnCancelar, gbc);

        setSize(400, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Suponiendo que tienes una instancia de GestionClientes llamada gestionClientes
            GestionClientes gestionClientes = new GestionClientes(null);
            new AgregarCliente(gestionClientes).setVisible(true);
        });
    }
}
