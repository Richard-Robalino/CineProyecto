import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class GestionClientes extends JFrame {
    private JTable tableClientes;
    private JButton btnAgregarCliente;
    private JButton btnEditarCliente;
    private JButton btnEliminarCliente;
    private JButton btnCancelar;
    AdminWindow adminWindow; // Referencia al AdminWindow

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

        btnAgregarCliente = new JButton("Agregar Cliente");
        btnAgregarCliente.setBackground(new Color(0, 123, 255));
        btnAgregarCliente.setForeground(Color.WHITE);
        btnAgregarCliente.setFocusPainted(false);
        btnAgregarCliente.setFont(new Font("Arial", Font.BOLD, 14));
        btnAgregarCliente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarCliente ventanaAgregarCliente = new AgregarCliente(GestionClientes.this);
                ventanaAgregarCliente.setVisible(true);
                dispose(); // Cierra la ventana actual
            }
        });
        panelBotones.add(btnAgregarCliente);

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
                    dispose(); // Cierra la ventana actual
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un cliente para editar.");
                }
            }
        });
        panelBotones.add(btnEditarCliente);

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

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(Color.GRAY);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                adminWindow.setVisible(true); // Abre la ventana de AdminWindow
            }
        });
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        setSize(800, 600); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private void actualizarTabla() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
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

    private void eliminarCliente(int clienteId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
