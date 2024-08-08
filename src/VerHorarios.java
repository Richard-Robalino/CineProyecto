import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerHorarios extends JFrame {
    private JComboBox<String> cmbPeliculas;
    private JButton btnVerHorarios;
    private JButton btnCancelar;
    private JTable tblHorarios;
    private DefaultTableModel modelo;

    public VerHorarios() {
        super("Ver Horarios");
        setLayout(new BorderLayout());

        // Panel de selección de película
        JPanel pnlPelicula = new JPanel();
        pnlPelicula.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnlPelicula.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaciado

        JLabel lblPelicula = new JLabel("Seleccione una película:");
        lblPelicula.setFont(new Font("Arial", Font.BOLD, 14));

        cmbPeliculas = new JComboBox<>();
        cmbPeliculas.setFont(new Font("Arial", Font.PLAIN, 14));
        cargarPeliculas();

        btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.setBackground(new Color(0, 123, 255));
        btnVerHorarios.setForeground(Color.WHITE);
        btnVerHorarios.setFocusPainted(false);
        btnVerHorarios.setFont(new Font("Arial", Font.BOLD, 14));
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verHorarios();
            }
        });

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(255, 69, 0)); // Color rojo oscuro
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                volverAlMenuCliente();
            }
        });

        pnlPelicula.add(lblPelicula);
        pnlPelicula.add(cmbPeliculas);
        pnlPelicula.add(btnVerHorarios);
        pnlPelicula.add(btnCancelar);
        add(pnlPelicula, BorderLayout.NORTH);

        // Panel de resultados
        JPanel pnlResultados = new JPanel(new BorderLayout());
        pnlResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaciado

        tblHorarios = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Sala");
        modelo.addColumn("Disponible");
        tblHorarios.setModel(modelo);
        tblHorarios.setRowHeight(30);
        tblHorarios.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(tblHorarios);
        pnlResultados.add(scrollPane, BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarPeliculas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM peliculas")) {

            while (rs.next()) {
                cmbPeliculas.addItem(rs.getString("titulo"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHorarios() {
        int peliculaId = getPeliculaId((String) cmbPeliculas.getSelectedItem());
        if (peliculaId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película válida", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM horarios WHERE pelicula_id = " + peliculaId)) {

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getDate("fecha");
                fila[1] = rs.getTime("hora");
                fila[2] = rs.getInt("sala");
                fila[3] = rs.getBoolean("disponible") ? "Sí" : "No";
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al ver horarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getPeliculaId(String peliculaTitulo) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id FROM peliculas WHERE titulo = '" + peliculaTitulo + "'")) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID de película: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return -1;
    }
    private void volverAlMenuCliente() {
        ClientWindow ventanaCliente = new ClientWindow(1); // Reemplaza 1 con el ID de usuario correspondiente
        ventanaCliente.setVisible(true);
        dispose();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerHorarios ventanaVerHorarios = new VerHorarios();
            ventanaVerHorarios.setVisible(true);
        });
    }
}
