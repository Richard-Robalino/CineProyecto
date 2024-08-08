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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Espaciado

        // Fondo de la ventana
        getContentPane().setBackground(new Color(250, 250, 250)); // Color gris claro

        // Título
        JLabel lblTitulo = new JLabel("Título:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblTitulo, gbc);

        txtTitulo = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtTitulo, gbc);

        // Duración
        JLabel lblDuracion = new JLabel("Duración (minutos):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblDuracion, gbc);

        txtDuracion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtDuracion, gbc);

        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        add(lblDescripcion, gbc);

        txtDescripcion = new JTextArea(5, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollPaneDescripcion = new JScrollPane(txtDescripcion);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(scrollPaneDescripcion, gbc);

        // Clasificación
        JLabel lblClasificacion = new JLabel("Clasificación:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(lblClasificacion, gbc);

        txtClasificacion = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(txtClasificacion, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(250, 250, 250)); // Color gris claro

        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(34, 139, 34)); // Color verde oscuro
        btnGuardar.setForeground(Color.WHITE); // Color blanco para el texto
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String titulo = txtTitulo.getText();
                int duracion = Integer.parseInt(txtDuracion.getText());
                String descripcion = txtDescripcion.getText();
                String clasificacion = txtClasificacion.getText();

                // Lógica para guardar la película en la base de datos
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "INSERT INTO peliculas (titulo, duracion, descripcion, clasificacion) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, titulo);
                        pstmt.setInt(2, duracion);
                        pstmt.setString(3, descripcion);
                        pstmt.setString(4, clasificacion);
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
        panelBotones.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(255, 69, 58)); // Color rojo
        btnCancelar.setForeground(Color.WHITE); // Color blanco para el texto
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                AdminWindow ventanaAdmin = new AdminWindow();
                ventanaAdmin.setVisible(true); // Abre la ventana de AdminWindow
            }
        });
        panelBotones.add(btnCancelar);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(panelBotones, gbc);

        setSize(400, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }
}
