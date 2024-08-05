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
    private JButton btnCancelar;
    AdminWindow adminWindow; // Referencia al AdminWindow

    public GestionHorarios(AdminWindow adminWindow) {
        super("Gestionar Horarios");
        this.adminWindow = adminWindow; // Guardar la referencia
        setLayout(new BorderLayout());

        // Fondo de la ventana
        getContentPane().setBackground(new Color(240, 240, 240)); // Color gris claro

        // Panel para la tabla de horarios
        JPanel panelTabla = new JPanel(new BorderLayout());
        tableHorarios = new JTable();
        actualizarTabla();
        JScrollPane scrollPane = new JScrollPane(tableHorarios);
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(240, 240, 240)); // Color gris claro

        // Botón Agregar Horario
        btnAgregarHorario = new JButton("Agregar Horario");
        btnAgregarHorario.setBackground(new Color(34, 139, 34)); // Color verde oscuro
        btnAgregarHorario.setForeground(Color.WHITE); // Texto blanco
        btnAgregarHorario.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregarHorario.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarHorario ventanaAgregarHorario = new AgregarHorario(GestionHorarios.this);
                ventanaAgregarHorario.setVisible(true);
                dispose(); // Cierra GestionHorarios
            }
        });
        panelBotones.add(btnAgregarHorario);

        // Botón Editar Horario
        btnEditarHorario = new JButton("Editar Horario");
        btnEditarHorario.setBackground(new Color(70, 130, 180)); // Color azul acero
        btnEditarHorario.setForeground(Color.WHITE); // Texto blanco
        btnEditarHorario.setFont(new Font("Arial", Font.BOLD, 14));
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

        // Botón Eliminar Horario
        btnEliminarHorario = new JButton("Eliminar Horario");
        btnEliminarHorario.setBackground(new Color(255, 69, 58)); // Color rojo
        btnEliminarHorario.setForeground(Color.WHITE); // Texto blanco
        btnEliminarHorario.setFont(new Font("Arial", Font.BOLD, 14));
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

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(105, 105, 105)); // Color gris oscuro
        btnCancelar.setForeground(Color.WHITE); // Texto blanco
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
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

                DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Pelicula ID", "Fecha", "Hora", "Sala", "Disponible"}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getInt("pelicula_id"),
                            rs.getDate("fecha"),
                            rs.getTime("hora"),
                            rs.getInt("sala"),
                            rs.getBoolean("disponible")
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
