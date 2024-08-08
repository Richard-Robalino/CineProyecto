import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * La clase VerEstadisticas muestra una ventana con una tabla que presenta estadísticas relacionadas
 * con las reservas de películas, incluyendo el número de reservas, horarios disponibles y asientos ocupados.
 */
public class VerEstadisticas extends JFrame {
    private JTable tableEstadisticas;
    private JButton btnCancelar;
    private AdminWindow adminWindow;

    /**
     * Constructor de la clase VerEstadisticas.
     * Inicializa la interfaz para mostrar las estadísticas y configura el botón de cancelar.
     *
     * @param adminWindow Ventana principal del administrador desde la cual se accede a esta ventana.
     */
    public VerEstadisticas(AdminWindow adminWindow) {
        super("Ver Estadísticas");
        this.adminWindow = adminWindow;
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
                dispose();
                adminWindow.setVisible(true);
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Actualiza la tabla de estadísticas con datos obtenidos de la base de datos.
     * Realiza una consulta para obtener el número de reservas, horarios disponibles
     * y asientos ocupados para cada película.
     */
    private void actualizarEstadisticas() {
        String url = "jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k";
        String usuario = "ubizbip0ntk5uopb";
        String contrasena = "vFULnkL51YQfK531npMk";

        String query = "SELECT p.titulo, " +
                "       COUNT(r.id) AS reservas, " +
                "       COUNT(DISTINCT h.id) AS horarios, " +
                "       COALESCE(SUM(CASE WHEN a.disponible = FALSE THEN 1 ELSE 0 END), 0) AS asientos_ocupados " +
                "FROM peliculas p " +
                "LEFT JOIN horarios h ON p.id = h.pelicula_id " +
                "LEFT JOIN reservas r ON h.id = r.horario_id " +
                "LEFT JOIN asientos a ON r.asiento_id = a.id " +
                "GROUP BY p.titulo";

        try (Connection conn = DriverManager.getConnection(url, usuario, contrasena);
             PreparedStatement pstmt = conn.prepareStatement(query);
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     * Crea una instancia de AdminWindow y la muestra.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
