import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReservarAsiento extends JFrame {
    private JComboBox<String> cmbPeliculas;
    private JComboBox<String> cmbHorarios;
    private JButton btnVerSala;
    private JButton btnReservar;
    private JTable tblAsientos;
    private DefaultTableModel modelo;
    private int peliculaId;
    private int horarioId;

    public ReservarAsiento() {
        super("Reservar Asiento");
        setLayout(new BorderLayout());

        // Panel de selección de película y horario
        JPanel pnlSeleccion = new JPanel();
        pnlSeleccion.setLayout(new GridLayout(2, 1));

        JPanel pnlPelicula = new JPanel();
        pnlPelicula.setLayout(new FlowLayout());
        JLabel lblPelicula = new JLabel("Seleccione una película:");
        cmbPeliculas = new JComboBox<>();
        cargarPeliculas();
        pnlPelicula.add(lblPelicula);
        pnlPelicula.add(cmbPeliculas);
        pnlSeleccion.add(pnlPelicula);

        JPanel pnlHorario = new JPanel();
        pnlHorario.setLayout(new FlowLayout());
        JLabel lblHorario = new JLabel("Seleccione un horario:");
        cmbHorarios = new JComboBox<>();
        btnVerSala = new JButton("Ver Sala");
        btnVerSala.addActionListener(e -> verSala());
        pnlHorario.add(lblHorario);
        pnlHorario.add(cmbHorarios);
        pnlHorario.add(btnVerSala);
        pnlSeleccion.add(pnlHorario);

        add(pnlSeleccion, BorderLayout.WEST);

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        tblAsientos = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Fila");
        modelo.addColumn("Asiento");
        modelo.addColumn("Disponible");
        tblAsientos.setModel(modelo);
        pnlResultados.add(new JScrollPane(tblAsientos), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        // Panel de botones
        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new FlowLayout());
        btnReservar = new JButton("Reservar");
        btnReservar.addActionListener(e -> reservarAsiento());
        pnlBotones.add(btnReservar);
        add(pnlBotones, BorderLayout.SOUTH);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarPeliculas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM peliculas")) {

            while (rs.next()) {
                cmbPeliculas.addItem(rs.getString("titulo"));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage());
        }
    }

    private void cargarHorarios() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM horarios WHERE pelicula_id = ?");
        ) {
            stmt.setInt(1, peliculaId);
            ResultSet rs = stmt.executeQuery();

            cmbHorarios.removeAllItems();
            while (rs.next()) {
                cmbHorarios.addItem(rs.getString("fecha") + " " + rs.getString("hora"));
                // Aquí deberías decidir cómo manejar múltiples horarios si es necesario
                horarioId = rs.getInt("id");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage());
        }
    }

    private void verSala() {
        cargarHorarios();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM asientos WHERE horario_id = ?")) {

            stmt.setInt(1, horarioId);
            ResultSet rs = stmt.executeQuery();

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString("fila");
                fila[1] = rs.getString("asiento");
                fila[2] = rs.getBoolean("disponible") ? "Sí" : "No";
                modelo.addRow(fila);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al ver sala: " + ex.getMessage());
        }
    }

    private void reservarAsiento() {
        int fila = tblAsientos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un asiento");
            return;
        }

        String asiento = (String) tblAsientos.getValueAt(fila, 1);
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement("UPDATE asientos SET disponible = 0 WHERE horario_id = ? AND fila = ? AND asiento = ?")) {

            stmt.setInt(1, horarioId);
            stmt.setString(2, asiento.substring(0, 1));
            stmt.setInt(3, Integer.parseInt(asiento.substring(1)));

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Asiento reservado con éxito");
            verSala();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al reservar asiento: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ReservarAsiento ventanaReservarAsiento = new ReservarAsiento();
            ventanaReservarAsiento.setVisible(true);
        });
    }
}
