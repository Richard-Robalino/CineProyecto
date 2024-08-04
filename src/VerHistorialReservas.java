import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerHistorialReservas extends JFrame {
    private JTable tblHistorial;
    private DefaultTableModel modelo;
    private JButton btnCerrar;

    public VerHistorialReservas(int clienteId) {
        super("Historial de Reservas");
        setLayout(new BorderLayout());

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
        pnlResultados.add(new JScrollPane(tblHistorial), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        // Panel de botones
        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new FlowLayout());
        btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        pnlBotones.add(btnCerrar);
        add(pnlBotones, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        cargarHistorialReservas(clienteId);
    }

    private void cargarHistorialReservas(int clienteId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT r.fecha_reserva, h.fecha, h.hora, p.titulo, h.sala, a.fila, a.numero " +
                             "FROM reservas r " +
                             "JOIN horarios h ON r.horario_id = h.id " +
                             "JOIN peliculas p ON h.pelicula_id = p.id " +
                             "JOIN asientos a ON r.asiento_id = a.id " +
                             "WHERE r.cliente_id = ? " +
                             "ORDER BY r.fecha_reserva DESC")) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[6];
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
            // Solicitar clienteId al usuario
            String input = JOptionPane.showInputDialog("Ingrese el ID del cliente:");
            int clienteId;
            try {
                clienteId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ID de cliente no válido. Usando ID predeterminado 3.");
                clienteId = 3; // ID predeterminado en caso de entrada no válida
            }
            VerHistorialReservas ventanaVerHistorialReservas = new VerHistorialReservas(clienteId);
            ventanaVerHistorialReservas.setVisible(true);
        });
    }
}
