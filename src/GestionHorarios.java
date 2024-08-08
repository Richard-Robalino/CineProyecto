import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Clase GestionHorarios proporciona una interfaz para gestionar los horarios de películas en el sistema.
 * Permite agregar, editar, eliminar horarios y muestra una tabla con la información de los horarios existentes.
 */
public class GestionHorarios extends JFrame {
    private JTable tableHorarios;
    private JButton btnAgregarHorario;
    private JButton btnEditarHorario;
    private JButton btnEliminarHorario;
    private JButton btnCancelar;
    private AdminWindow adminWindow; // Referencia al AdminWindow

    /**
     * Constructor de la clase GestionHorarios.
     * Inicializa la interfaz de usuario para la gestión de horarios.
     *
     * @param adminWindow La instancia de la ventana AdminWindow desde la cual se invoca esta ventana.
     */
    public GestionHorarios(AdminWindow adminWindow) {
        super("Gestionar Horarios");
        this.adminWindow = adminWindow;
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
                dispose();
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
                    dispose();
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
                dispose();
                AdminWindow ventanaAdmin = new AdminWindow();
                ventanaAdmin.setVisible(true);
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método actualizarTabla.
     * Actualiza la tabla de horarios con la información obtenida de la base de datos.
     */
    private void actualizarTabla() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
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

    /**
     * Método eliminarHorario.
     * Elimina un horario de la base de datos usando el ID proporcionado.
     *
     * @param horarioId El ID del horario a eliminar.
     */
    private void eliminarHorario(int horarioId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
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

    /**
     * Método main.
     * Punto de entrada para la aplicación. Crea e inicia una instancia de la ventana AdminWindow.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
