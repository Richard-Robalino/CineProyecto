import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerHorarios extends JFrame {
    private JComboBox cmbPeliculas;
    private JButton btnVerHorarios;
    private JTable tblHorarios;
    private DefaultTableModel modelo;

    public VerHorarios() {
        super("Ver Horarios");
        setLayout(new BorderLayout());

        // Panel de selección de película
        JPanel pnlPelicula = new JPanel();
        pnlPelicula.setLayout(new FlowLayout());
        JLabel lblPelicula = new JLabel("Seleccione una película:");
        cmbPeliculas = new JComboBox();
        cargarPeliculas();
        btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                verHorarios();
            }
        });
        pnlPelicula.add(lblPelicula);
        pnlPelicula.add(cmbPeliculas);
        pnlPelicula.add(btnVerHorarios);
        add(pnlPelicula, BorderLayout.NORTH);

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        tblHorarios = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Fecha");
        modelo.addColumn("Hora");
        modelo.addColumn("Sala");
        modelo.addColumn("Disponible");
        tblHorarios.setModel(modelo);
        pnlResultados.add(new JScrollPane(tblHorarios), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarPeliculas() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM peliculas");

            while (rs.next()) {
                cmbPeliculas.addItem(rs.getString("titulo"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage());
        }
    }

    private void verHorarios() {
        int peliculaId = getPeliculaId((String) cmbPeliculas.getSelectedItem());
        if (peliculaId == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una película válida");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM horarios WHERE pelicula_id = " + peliculaId);

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getDate("fecha");
                fila[1] = rs.getTime("hora");
                fila[2] = rs.getInt("sala");
                fila[3] = rs.getBoolean("disponible")? "Sí" : "No";
                modelo.addRow(fila);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al ver horarios: " + ex.getMessage());
        }
    }

    private int getPeliculaId(String peliculaTitulo) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM peliculas WHERE titulo = '" + peliculaTitulo + "'");

            if (rs.next()) {
                return rs.getInt("id");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID de película: " + ex.getMessage());
        }

        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerHorarios ventanaVerHorarios = new VerHorarios();
            ventanaVerHorarios.setVisible(true);
        });
    }
}