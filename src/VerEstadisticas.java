import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class VerEstadisticas extends JFrame {
    private JTable tableEstadisticas;
    private JButton btnCancelar;
    private AdminWindow adminWindow; // Referencia al AdminWindow

    public VerEstadisticas(AdminWindow adminWindow) {
        super("Ver Estadísticas");
        this.adminWindow = adminWindow; // Guardar la referencia
        setLayout(new BorderLayout());

        // Panel para la tabla de estadísticas
        JPanel panelTabla = new JPanel(new BorderLayout());
        tableEstadisticas = new JTable();
        tableEstadisticas.setFillsViewportHeight(true);
        tableEstadisticas.setRowHeight(25);
        actualizarEstadisticas();
        panelTabla.add(new JScrollPane(tableEstadisticas), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // Panel para el botón de cancelar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaciado

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                adminWindow.setVisible(true); // Abre la ventana de AdminWindow
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(600, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private void actualizarEstadisticas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
            // Ejemplo de consulta para mostrar estadísticas de ocupación
            String query = "SELECT p.titulo, COUNT(r.id) AS reservas, COUNT(h.id) AS horarios, SUM(r.cantidad_asientos) AS asientos_ocupados " +
                    "FROM peliculas p " +
                    "LEFT JOIN horarios h ON p.id = h.pelicula_id " +
                    "LEFT JOIN reservas r ON h.id = r.horario_id " +
                    "GROUP BY p.titulo";

            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                DefaultTableModel model = new DefaultTableModel(new String[]{"Título", "Reservas", "Horarios", "Asientos Ocupados"}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getString("titulo"),
                            rs.getInt("reservas"),
                            rs.getInt("horarios"),
                            rs.getInt("asientos_ocupados")
                    });
                }
                tableEstadisticas.setModel(model);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
