import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerHistorialReservas extends JFrame {
    private JTable tblHistorial;
    private DefaultTableModel modelo;
    private JButton btnCerrar;
    private JTextField txtNombreUsuario;

    public VerHistorialReservas(String nombreUsuario) {
        super("Historial de Reservas");
        setLayout(new BorderLayout());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel de búsqueda
        JPanel pnlBusqueda = new JPanel();
        pnlBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnlBusqueda.setBackground(new Color(230, 230, 250)); // Color de fondo suave

        JLabel lblNombreUsuario = new JLabel("Nombre de Usuario:");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        txtNombreUsuario = new JTextField(20);
        txtNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombreUsuario.setText(nombreUsuario);
        txtNombreUsuario.setEditable(false); // Nombre de usuario no editable

        pnlBusqueda.add(lblNombreUsuario);
        pnlBusqueda.add(txtNombreUsuario);
        add(pnlBusqueda, BorderLayout.NORTH);

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        tblHistorial = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Fecha Reserva");
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Película");
        modelo.addColumn("Sala");
        modelo.addColumn("Asiento");
        tblHistorial.setModel(modelo);
        tblHistorial.setFont(new Font("Arial", Font.PLAIN, 14));
        tblHistorial.setRowHeight(25);
        tblHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Ajustar el ancho de las columnas
        tblHistorial.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblHistorial.getColumnModel().getColumn(1).setPreferredWidth(100);
        tblHistorial.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(200);
        tblHistorial.getColumnModel().getColumn(4).setPreferredWidth(60);
        tblHistorial.getColumnModel().getColumn(5).setPreferredWidth(80);

        pnlResultados.add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        // Panel de botones
        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
        pnlBotones.setBackground(new Color(230, 230, 250));

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBackground(new Color(70, 130, 180));
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        pnlBotones.add(btnCerrar);
        add(pnlBotones, BorderLayout.SOUTH);

        cargarHistorialReservas(nombreUsuario);
    }

    private void cargarHistorialReservas(String nombreUsuario) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT r.fecha_reserva, h.fecha, h.hora, p.titulo, h.sala, a.fila, a.numero " +
                             "FROM reservas r " +
                             "JOIN horarios h ON r.horario_id = h.id " +
                             "JOIN peliculas p ON h.pelicula_id = p.id " +
                             "JOIN asientos a ON r.asiento_id = a.id " +
                             "JOIN usuarios u ON r.cliente_id = u.id " +
                             "WHERE u.username = ? " +
                             "ORDER BY r.fecha_reserva DESC")) {
            stmt.setString(1, nombreUsuario);
            ResultSet rs = stmt.executeQuery();

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getString("fecha_reserva");
                fila[1] = rs.getString("fecha");
                fila[2] = rs.getString("hora");
                fila[3] = rs.getString("titulo");
                fila[4] = rs.getInt("sala");
                fila[5] = rs.getString("fila") + rs.getInt("numero");
                modelo.addRow(fila);
            }
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial de reservas: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String nombreUsuario = JOptionPane.showInputDialog("Ingrese el nombre del usuario:");
            if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
                VerHistorialReservas ventana = new VerHistorialReservas(nombreUsuario);
                ventana.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nombre de usuario no válido. La aplicación se cerrará.");
                System.exit(0);
            }
        });
    }
}
