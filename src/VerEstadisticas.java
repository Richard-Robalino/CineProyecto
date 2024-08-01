import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class VerEstadisticas extends JFrame {
    private JTable tableEstadisticas;
    private JButton btnCancelar;
    private AdminWindow adminWindow;

    public VerEstadisticas(AdminWindow adminWindow) {
        super("Ver Estadísticas");
        this.adminWindow = adminWindow;
        setLayout(new BorderLayout());

        // Panel para la tabla de estadísticas
        JPanel panelTabla = new JPanel(new BorderLayout());
        tableEstadisticas = new JTable();
        actualizarEstadisticas();
        panelTabla.add(new JScrollPane(tableEstadisticas), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // Panel para el botón de cancelar
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnCancelar = new JButton("Cancelar");
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

    private void actualizarEstadisticas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
            String query = "SELECT p.titulo, COUNT(r.id) AS reservas, COUNT(h.id) AS horarios, COUNT(r.asiento_id) AS asientos_ocupados " +
                    "FROM Peliculas p " +
                    "LEFT JOIN Horarios h ON p.id = h.pelicula_id " +
                    "LEFT JOIN Reservas r ON h.id = r.horario_id " +
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
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
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
