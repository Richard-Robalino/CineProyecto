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
        cmbPeliculas.addActionListener(e -> seleccionarPelicula());
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

        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarPeliculas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, titulo FROM Peliculas")) {

            cmbPeliculas.addItem("Seleccione una película");
            while (rs.next()) {
                cmbPeliculas.addItem(rs.getString("titulo") + " (" + rs.getInt("id") + ")");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar películas: " + ex.getMessage());
        }
    }

    private void seleccionarPelicula() {
        String seleccion = (String) cmbPeliculas.getSelectedItem();
        if (seleccion != null && !seleccion.equals("Seleccione una película")) {
            peliculaId = Integer.parseInt(seleccion.split("\\(")[1].replace(")", ""));
            cargarHorarios();
        }
    }

    private void cargarHorarios() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement("SELECT id, fecha, hora FROM Horarios WHERE pelicula_id = ?")) {

            stmt.setInt(1, peliculaId);
            ResultSet rs = stmt.executeQuery();

            cmbHorarios.removeAllItems();
            cmbHorarios.addItem("Seleccione un horario");
            while (rs.next()) {
                cmbHorarios.addItem(rs.getString("fecha") + " " + rs.getString("hora") + " (" + rs.getInt("id") + ")");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar horarios: " + ex.getMessage());
        }
    }

    private void verSala() {
        String seleccion = (String) cmbHorarios.getSelectedItem();
        if (seleccion != null && !seleccion.equals("Seleccione un horario")) {
            horarioId = Integer.parseInt(seleccion.split("\\(")[1].replace(")", ""));
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
                 PreparedStatement stmt = conn.prepareStatement("SELECT a.fila, a.numero, a.disponible FROM Asientos a JOIN Horarios h ON a.sala_id = h.sala_id WHERE h.id = ?")) {

                stmt.setInt(1, horarioId);
                ResultSet rs = stmt.executeQuery();

                modelo.setRowCount(0);
                while (rs.next()) {
                    Object[] fila = new Object[3];
                    fila[0] = rs.getString("fila");
                    fila[1] = rs.getInt("numero");
                    fila[2] = rs.getBoolean("disponible") ? "Sí" : "No";
                    modelo.addRow(fila);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al ver sala: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un horario primero");
        }
    }





    private void reservarAsiento() {
        int fila = tblAsientos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un asiento");
            return;
        }

        String filaAsiento = (String) tblAsientos.getValueAt(fila, 0);
        int numeroAsiento = (int) tblAsientos.getValueAt(fila, 1);

        // Asumiendo que el usuario está autenticado y tenemos su ID
        int usuarioId = 1; // Debes obtener el usuario_id de forma segura y apropiada
        String[] seleccionHorario = ((String) cmbHorarios.getSelectedItem()).split(" ");
        String fecha = seleccionHorario[0]; // Formato de fecha: "YYYY-MM-DD"
        String hora = seleccionHorario[1];  // Formato de hora: "HH:MM:SS"

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Reservas (pelicula_id, sala_id, asiento_id, fecha, hora, usuario_id) VALUES (?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, peliculaId);
            stmt.setInt(2, horarioId); // Asumiendo que sala_id está relacionado con el horario
            stmt.setInt(3, numeroAsiento);
            stmt.setDate(4, java.sql.Date.valueOf(fecha));
            stmt.setTime(5, java.sql.Time.valueOf(hora));
            stmt.setInt(6, usuarioId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Asiento reservado con éxito");
                verSala();
            } else {
                JOptionPane.showMessageDialog(this, "Error al reservar el asiento");
            }

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
