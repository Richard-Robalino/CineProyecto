import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GestionHorarios extends JFrame {
    private JTable tableHorarios;
    private JButton btnAgregarHorario;
    private JButton btnEditarHorario;
    private JButton btnEliminarHorario;
    private JButton btnVerHorarios;
    private JButton btnCancelar;
    AdminWindow adminWindow; // Referencia al AdminWindow

    public GestionHorarios(AdminWindow adminWindow) {
        super("Gestionar Horarios");
        this.adminWindow = adminWindow; // Guardar la referencia
        setLayout(new BorderLayout());

        // Panel para la tabla de horarios
        JPanel panelTabla = new JPanel(new BorderLayout());
        tableHorarios = new JTable();
        actualizarTabla();
        panelTabla.add(new JScrollPane(tableHorarios), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnAgregarHorario = new JButton("Agregar Horario");
        btnAgregarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarHorario ventanaAgregarHorario = new AgregarHorario(GestionHorarios.this);
                ventanaAgregarHorario.setVisible(true);
                dispose(); // Cierra GestionHorarios
            }
        });
        panelBotones.add(btnAgregarHorario);

        btnEditarHorario = new JButton("Editar Horario");
        btnEditarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableHorarios.getSelectedRow();
                if (selectedRow != -1) {
                    int horarioId = (int) tableHorarios.getValueAt(selectedRow, 0);
                    EditarHorario ventanaEditarHorario = new EditarHorario(GestionHorarios.this, horarioId);
                    ventanaEditarHorario.setVisible(true);
                    dispose(); // Cierra GestionHorarios
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un horario para editar.");
                }
            }
        });
        panelBotones.add(btnEditarHorario);

        btnEliminarHorario = new JButton("Eliminar Horario");
        btnEliminarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableHorarios.getSelectedRow();
                if (selectedRow != -1) {
                    int horarioId = (int) tableHorarios.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este horario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarHorario(horarioId);
                        actualizarTabla();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un horario para eliminar.");
                }
            }
        });
        panelBotones.add(btnEliminarHorario);



        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                AdminWindow ventanaAdmin = new AdminWindow();
                ventanaAdmin.setVisible(true); // Abre la ventana de AdminWindow
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(800, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private void actualizarTabla() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
            String query = "SELECT * FROM horarios";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Pelicula ID", "Fecha", "Hora"}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getInt("pelicula_id"),
                            rs.getDate("fecha"),
                            rs.getTime("hora"),
                            //rs.getInt("sala"),
                            //rs.getBoolean("disponible")
                    });
                }
                tableHorarios.setModel(model);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
            ex.printStackTrace();
        }
    }

    private void eliminarHorario(int horarioId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
            String query = "DELETE FROM horarios WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, horarioId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Horario eliminado exitosamente");
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
