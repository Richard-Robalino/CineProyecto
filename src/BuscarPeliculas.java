import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BuscarPeliculas extends JFrame {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JButton btnCancelar;
    private JTable tblPeliculas;
    private DefaultTableModel modelo;

    public BuscarPeliculas() {
        super("Buscar Películas");
        setLayout(new BorderLayout());

        // Panel de búsqueda
        JPanel pnlBuscar = new JPanel();
        pnlBuscar.setLayout(new FlowLayout());
        pnlBuscar.setBackground(new Color(220, 220, 220)); // Color gris claro

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        txtBuscar.setForeground(new Color(50, 50, 50)); // Color de texto gris oscuro

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(34, 139, 34)); // Color verde oscuro
        btnBuscar.setForeground(Color.WHITE); // Texto blanco
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPeliculas();
            }
        });

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(255, 69, 0)); // Color rojo oscuro
        btnCancelar.setForeground(Color.WHITE); // Texto blanco
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volverAlMenuCliente();
            }
        });

        pnlBuscar.add(txtBuscar);
        pnlBuscar.add(btnBuscar);
        pnlBuscar.add(btnCancelar);
        add(pnlBuscar, BorderLayout.NORTH);

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        pnlResultados.setBackground(new Color(240, 240, 240)); // Color gris claro

        tblPeliculas = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Título");
        modelo.addColumn("Duración");
        modelo.addColumn("Clasificación");
        tblPeliculas.setModel(modelo);
        tblPeliculas.setFont(new Font("Arial", Font.PLAIN, 14));
        tblPeliculas.setForeground(new Color(50, 50, 50)); // Color de texto gris oscuro

        // Configurar el encabezado de la tabla
        JTableHeader header = tblPeliculas.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setForeground(new Color(34, 139, 34)); // Color verde oscuro

        pnlResultados.add(new JScrollPane(tblPeliculas), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buscarPeliculas() {
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un título de película para buscar");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM peliculas WHERE titulo LIKE '%" + buscar + "%'");

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString("titulo");
                fila[1] = rs.getInt("duracion");
                fila[2] = rs.getString("clasificacion");
                modelo.addRow(fila);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar películas: " + ex.getMessage());
        }
    }

    private void volverAlMenuCliente() {
        ClientWindow ventanaCliente = new ClientWindow(1);
        ventanaCliente.setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
            ventanaBuscarPeliculas.setVisible(true);
        });
    }
}
