import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GestionAsientos extends JFrame {
    private JButton btnAgregarAsiento;
    private JButton btnEliminarAsiento;
    private JButton btnActualizarAsiento;
    private JTable tablaAsientos;
    private DefaultTableModel modeloTabla;

    public GestionAsientos(JFrame parent) {
        super("Gestionar Asientos");
        setLayout(new BorderLayout());

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Sala", "Fila", "Número", "Disponible"}, 0);
        tablaAsientos = new JTable(modeloTabla);
        cargarAsientos();

        JScrollPane scrollPane = new JScrollPane(tablaAsientos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        btnAgregarAsiento = new JButton("Agregar Asiento");
        btnAgregarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarAsiento ventanaAgregarAsiento = new AgregarAsiento(GestionAsientos.this);
                ventanaAgregarAsiento.setVisible(true);
            }
        });
        panelBotones.add(btnAgregarAsiento);

        btnEliminarAsiento = new JButton("Eliminar Asiento");
        btnEliminarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarAsiento();
            }
        });
        panelBotones.add(btnEliminarAsiento);

        btnActualizarAsiento = new JButton("Actualizar Asiento");
        btnActualizarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarAsiento();
            }
        });
        panelBotones.add(btnActualizarAsiento);

        add(panelBotones, BorderLayout.SOUTH);

        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });
        add(btnRegresar, BorderLayout.NORTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarAsientos() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cine_reservas", "root", "123456");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM asientos")) {
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getInt("sala_id"),
                        rs.getString("fila"),
                        rs.getInt("numero"),
                        rs.getBoolean("disponible")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void agregarAsiento() {
        // Implementar lógica para agregar asiento
    }

    private void eliminarAsiento() {
        int selectedRow = tablaAsientos.getSelectedRow();
        if (selectedRow != -1) {
            int asientoId = (int) modeloTabla.getValueAt(selectedRow, 0);
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cine_reservas", "root", "123456");
                 PreparedStatement pstmt = con.prepareStatement("DELETE FROM asientos WHERE id = ?")) {
                pstmt.setInt(1, asientoId);
                pstmt.executeUpdate();
                modeloTabla.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarAsiento() {
        int selectedRow = tablaAsientos.getSelectedRow();
        if (selectedRow != -1) {
            int asientoId = (int) modeloTabla.getValueAt(selectedRow, 0);
            String fila = (String) modeloTabla.getValueAt(selectedRow, 2);
            int numero = (int) modeloTabla.getValueAt(selectedRow, 3);
            boolean disponible = (boolean) modeloTabla.getValueAt(selectedRow, 4);
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cine_reservas", "root", "123456");
                 PreparedStatement pstmt = con.prepareStatement("UPDATE asientos SET fila = ?, numero = ?, disponible = ? WHERE id = ?")) {
                pstmt.setString(1, fila);
                pstmt.setInt(2, numero);
                pstmt.setBoolean(3, disponible);
                pstmt.setInt(4, asientoId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionAsientos ventanaGestionAsientos = new GestionAsientos(null);
            ventanaGestionAsientos.setVisible(true);
        });
    }
}
