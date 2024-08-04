import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

public class AgregarCliente extends JFrame {
    private JTextField txtUsername;
    private JTextField txtPassword;
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
        setLayout(new GridLayout(5, 2));

        JLabel lblUsername = new JLabel("Username:");
        txtUsername = new JTextField(20);
        add(lblUsername);
        add(txtUsername);

        JLabel lblPassword = new JLabel("Password:");
        txtPassword = new JTextField(20);
        add(lblPassword);
        add(txtPassword);

        JLabel lblRol = new JLabel("Rol:");
        comboRol = new JComboBox<>(new String[]{"CLIENTE", "ADMINISTRADOR"});
        add(lblRol);
        add(comboRol);

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
        add(btnSubirFoto);
        add(lblFoto);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();
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
                            }
                        } else {
                            pstmt.setNull(4, Types.BLOB);
                        }

                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Cliente agregado exitosamente");
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
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                GestionClientes ventanaGestionClientes = new GestionClientes(gestionClientes.adminWindow);
                ventanaGestionClientes.setVisible(true); // Abre la ventana de GestionClientes
            }
        });
        add(btnCancelar);

        setSize(300, 250); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

}

