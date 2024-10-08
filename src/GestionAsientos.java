import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clase GestionAsientos proporciona una interfaz para gestionar los asientos en el sistema.
 * Permite agregar, eliminar y actualizar asientos, y muestra una tabla con la información de los asientos existentes.
 */
public class GestionAsientos extends JFrame {
    private JButton btnAgregarAsiento;
    private JButton btnEliminarAsiento;
    private JButton btnActualizarAsiento;
    private JTable tablaAsientos;
    private DefaultTableModel modeloTabla;
    private JButton btnCancelar;
    private AdminWindow adminWindow;

    /**
     * Constructor de la clase GestionAsientos.
     * Inicializa la interfaz de usuario para la gestión de asientos.
     *
     * @param adminWindow La instancia de la ventana AdminWindow desde la cual se invoca esta ventana.
     */
    public GestionAsientos(AdminWindow adminWindow) {
        super("Gestionar Asientos");
        this.adminWindow = adminWindow;
        setLayout(new BorderLayout());

        // Configuración de la tabla y el modelo de datos
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Sala", "Fila", "Número", "Disponible"}, 0);
        tablaAsientos = new JTable(modeloTabla);
        cargarAsientos();

        JScrollPane scrollPane = new JScrollPane(tablaAsientos);
        add(scrollPane, BorderLayout.CENTER);

        // Panel para los botones de acción
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
                int selectedRow = tablaAsientos.getSelectedRow();
                if (selectedRow != -1) {
                    int asientoId = (int) modeloTabla.getValueAt(selectedRow, 0);
                    ActualizarAsiento ventanaActualizar = new ActualizarAsiento(GestionAsientos.this, asientoId);
                    ventanaActualizar.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(GestionAsientos.this, "Debe seleccionar un asiento para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelBotones.add(btnActualizarAsiento);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.GRAY);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                if (adminWindow != null) {
                    adminWindow.setVisible(true); // Asegura que adminWindow se haga visible
                }
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método cargarAsientos.
     * Carga los datos de los asientos desde la base de datos y los muestra en la tabla.
     */
    private void cargarAsientos() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
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

    /**
     * Método eliminarAsiento.
     * Elimina el asiento seleccionado en la tabla de la base de datos y actualiza la tabla.
     */
    private void eliminarAsiento() {
        int selectedRow = tablaAsientos.getSelectedRow();
        if (selectedRow != -1) {
            int asientoId = (int) modeloTabla.getValueAt(selectedRow, 0);
            try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
                 PreparedStatement pstmt = con.prepareStatement("DELETE FROM asientos WHERE id = ?")) {
                pstmt.setInt(1, asientoId);
                pstmt.executeUpdate();
                modeloTabla.removeRow(selectedRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método actualizarAsiento.
     * Actualiza el asiento seleccionado en la tabla en la base de datos.
     */
    private void actualizarAsiento() {
        int selectedRow = tablaAsientos.getSelectedRow();
        if (selectedRow != -1) {
            int asientoId = (int) modeloTabla.getValueAt(selectedRow, 0);
            String fila = (String) modeloTabla.getValueAt(selectedRow, 2);
            int numero = (int) modeloTabla.getValueAt(selectedRow, 3);
            boolean disponible = (boolean) modeloTabla.getValueAt(selectedRow, 4);
            try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
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

    /**
     * Método main.
     * Punto de entrada para la aplicación. Crea e inicia una instancia de la ventana de gestión de asientos.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionAsientos ventanaGestionAsientos = new GestionAsientos(null);
            ventanaGestionAsientos.setVisible(true);
        });
    }
}
