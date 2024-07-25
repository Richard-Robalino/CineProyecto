import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerHistorialReservas extends JFrame {
    private JTable tblHistorial;
    private DefaultTableModel modelo;
    private JButton btnCerrar;

    public VerHistorialReservas() {
        super("Historial de Reservas");
        setLayout(new BorderLayout());

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        tblHistorial = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Pelicula");
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

        cargarHistorialReservas();
    }

    private void cargarHistorialReservas() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT r.fecha, r.hora, p.titulo, s.nombre, a.asiento " +
                    "FROM reservas r " +
                    "JOIN peliculas p ON r.pelicula_id = p.id " +
                    "JOIN salas s ON r.sala_id = s.id " +
                    "JOIN asientos a ON r.asiento_id = a.id " +
                    "ORDER BY r.fecha DESC");

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getString("fecha");
                fila[1] = rs.getString("hora");
                fila[2] = rs.getString("titulo");
                fila[3] = rs.getString("nombre");
                fila[4] = rs.getString("asiento");
                modelo.addRow(fila);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial de reservas: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerHistorialReservas ventanaVerHistorialReservas = new VerHistorialReservas();
            ventanaVerHistorialReservas.setVisible(true);
        });
    }
}