import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Clase GestionClientes proporciona una interfaz para gestionar los clientes en el sistema.
 * Permite agregar, editar, eliminar clientes y muestra una tabla con la información de los clientes existentes.
 */
public class GestionClientes extends JFrame {
    private JTable tableClientes;
    private JButton btnAgregarCliente;
    private JButton btnEditarCliente;
    private JButton btnEliminarCliente;
    private JButton btnCancelar;
    AdminWindow adminWindow; // Referencia al AdminWindow

    /**
     * Constructor de la clase GestionClientes.
     * Inicializa la interfaz de usuario para la gestión de clientes.
     *
     * @param adminWindow La instancia de la ventana AdminWindow desde la cual se invoca esta ventana.
     */
    public GestionClientes(AdminWindow adminWindow) {
        super("Gestionar Clientes");
        this.adminWindow = adminWindow; // Guardar la referencia
        setLayout(new BorderLayout());

        // Panel para la tabla de clientes
        JPanel panelTabla = new JPanel(new BorderLayout());
        tableClientes = new JTable();
        tableClientes.setFillsViewportHeight(true);
        tableClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actualizarTabla();
        panelTabla.add(new JScrollPane(tableClientes), BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        // Panel para los botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaciado

        // Botón Agregar Cliente
        btnAgregarCliente = new JButton("Agregar Cliente");
        btnAgregarCliente.setBackground(new Color(0, 123, 255));
        btnAgregarCliente.setForeground(Color.WHITE);
        btnAgregarCliente.setFocusPainted(false);
        btnAgregarCliente.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarCliente ventanaAgregarCliente = new AgregarCliente(GestionClientes.this);
                ventanaAgregarCliente.setVisible(true);
                actualizarTabla();
            }
        });
        panelBotones.add(btnAgregarCliente);

        // Botón Editar Cliente
        btnEditarCliente = new JButton("Editar Cliente");
        btnEditarCliente.setBackground(new Color(0, 123, 255));
        btnEditarCliente.setForeground(Color.WHITE);
        btnEditarCliente.setFocusPainted(false);
        btnEditarCliente.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableClientes.getSelectedRow();
                if (selectedRow != -1) {
                    int clienteId = (int) tableClientes.getValueAt(selectedRow, 0);
                    EditarCliente ventanaEditarCliente = new EditarCliente(GestionClientes.this, clienteId);
                    ventanaEditarCliente.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente para editar.");
                }
            }
        });
        panelBotones.add(btnEditarCliente);

        // Botón Eliminar Cliente
        btnEliminarCliente = new JButton("Eliminar Cliente");
        btnEliminarCliente.setBackground(new Color(220, 53, 69));
        btnEliminarCliente.setForeground(Color.WHITE);
        btnEliminarCliente.setFocusPainted(false);
        btnEliminarCliente.setFont(new Font("Arial", Font.BOLD, 14));
        btnEliminarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableClientes.getSelectedRow();
                if (selectedRow != -1) {
                    int clienteId = (int) tableClientes.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de eliminar este cliente?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarCliente(clienteId);
                        actualizarTabla();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente para eliminar.");
                }
            }
        });
        panelBotones.add(btnEliminarCliente);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.GRAY);
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

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método actualizarTabla.
     * Actualiza la tabla de clientes con la información obtenida de la base de datos.
     */
    private void actualizarTabla() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
            String query = "SELECT id, username, password, rol FROM usuarios WHERE rol='CLIENTE' OR rol='ADMINISTRADOR'";
            try (PreparedStatement pstmt = conn.prepareStatement(query);
                 ResultSet rs = pstmt.executeQuery()) {

                DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Username", "Password", "Rol"}, 0);
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("rol")
                    });
                }
                tableClientes.setModel(model);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Método eliminarCliente.
     * Elimina un cliente de la base de datos usando el ID proporcionado.
     *
     * @param clienteId El ID del cliente a eliminar.
     */
    private void eliminarCliente(int clienteId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
            String query = "DELETE FROM usuarios WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, clienteId);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
