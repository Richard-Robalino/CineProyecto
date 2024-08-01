import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgregarPelicula extends JFrame {
    private JTextField txtTitulo;
    private JTextField txtDuracion;
    private JTextArea txtDescripcion;
    private JTextField txtClasificacion;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private AdminWindow adminWindow; // Referencia al AdminWindow

    public AgregarPelicula(AdminWindow adminWindow) {
        super("Agregar Película");
        this.adminWindow = adminWindow; // Guardar la referencia
        setLayout(new GridLayout(5, 2));

        JLabel lblTitulo = new JLabel("Título:");
        txtTitulo = new JTextField(20);
        add(lblTitulo);
        add(txtTitulo);

        JLabel lblDuracion = new JLabel("Duración (minutos):");
        txtDuracion = new JTextField(20);
        add(lblDuracion);
        add(txtDuracion);

        JLabel lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextArea(5, 20);
        add(lblDescripcion);
        add(new JScrollPane(txtDescripcion)); // Usar JScrollPane para el JTextArea

        JLabel lblClasificacion = new JLabel("Genero:");
        txtClasificacion = new JTextField(20);
        add(lblClasificacion);
        add(txtClasificacion);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String titulo = txtTitulo.getText();
                int duracion = Integer.parseInt(txtDuracion.getText());
                String descripcion = txtDescripcion.getText();
                String genero = txtClasificacion.getText();

                // Lógica para guardar la película en la base de datos
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
                    String query = "INSERT INTO peliculas (titulo, duracion, descripcion, genero) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, titulo);
                        pstmt.setInt(2, duracion);
                        pstmt.setString(3, descripcion);
                        pstmt.setString(4, genero);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Película agregada exitosamente");
                        dispose(); // Cierra la ventana actual

                        // Regresa a AdminWindow
                        AdminWindow ventanaAdmin = new AdminWindow();
                        ventanaAdmin.setVisible(true);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace(); // Esto te ayudará a depurar el problema en la conexión
                }
            }
        });
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                AdminWindow ventanaAdmin = new AdminWindow();
                ventanaAdmin.setVisible(true); // Abre la ventana de AdminWindow
            }
        });
        add(btnCancelar);

        setSize(400, 300); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }
}
