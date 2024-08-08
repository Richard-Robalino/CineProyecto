import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JButton btnCancelar;
    private JButton btnVolverMenu;
    private int usuarioId;
    public ReservarAsiento(int usuarioId) {
        this.usuarioId = usuarioId;
        //super("Reservar Asiento");
        setLayout(new BorderLayout());

        // Establecer fondo de color
        getContentPane().setBackground(new Color(200, 220, 240));

        // Panel de selección de película y horario
        JPanel pnlSeleccion = new JPanel();
        pnlSeleccion.setLayout(new GridLayout(2, 1));
        pnlSeleccion.setBackground(new Color(200, 220, 240));

        JPanel pnlPelicula = new JPanel();
        pnlPelicula.setLayout(new FlowLayout());
        pnlPelicula.setBackground(new Color(200, 220, 240));
        JLabel lblPelicula = new JLabel("Seleccione una película:");
        lblPelicula.setFont(new Font("Arial", Font.PLAIN, 16));
        cmbPeliculas = new JComboBox<>();
        cargarPeliculas();
        cmbPeliculas.addActionListener(e -> seleccionarPelicula());
        pnlPelicula.add(lblPelicula);
        pnlPelicula.add(cmbPeliculas);
        pnlSeleccion.add(pnlPelicula);

        JPanel pnlHorario = new JPanel();
        pnlHorario.setLayout(new FlowLayout());
        pnlHorario.setBackground(new Color(200, 220, 240));
        JLabel lblHorario = new JLabel("Seleccione un horario:");
        lblHorario.setFont(new Font("Arial", Font.PLAIN, 16));
        cmbHorarios = new JComboBox<>();
        btnVerSala = new JButton("Ver Sala");
        btnVerSala.setBackground(new Color(70, 130, 180));
        btnVerSala.setForeground(Color.WHITE);
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
        pnlBotones.setBackground(new Color(200, 220, 240));
        btnReservar = new JButton("Reservar");
        btnReservar.setFont(new Font("Arial", Font.BOLD, 16));
        btnReservar.setBackground(new Color(70, 130, 180));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.addActionListener(e -> reservarAsiento());
        pnlBotones.add(btnReservar);

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
        pnlBotones.add(btnCancelar);

        add(pnlBotones, BorderLayout.SOUTH);

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarPeliculas() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, titulo FROM peliculas")) {

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
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement stmt = conn.prepareStatement("SELECT id, fecha, hora FROM horarios WHERE pelicula_id = ?")) {

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
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
                 PreparedStatement stmt = conn.prepareStatement("SELECT a.fila, a.numero, a.disponible FROM asientos a JOIN horarios h ON a.sala_id = h.sala WHERE h.id = ?")) {

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

        // Obteniendo el asiento_id basado en la fila y el número de asiento
        int asientoId = obtenerAsientoId(filaAsiento, numeroAsiento);

        // Utilizar el usuarioId pasado en el constructor
        String[] seleccionHorario = ((String) cmbHorarios.getSelectedItem()).split(" ");
        String fecha = seleccionHorario[0]; // Formato de fecha: "YYYY-MM-DD"
        String hora = seleccionHorario[1];  // Formato de hora: "HH:MM:SS"

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO reservas (cliente_id, horario_id, asiento_id) VALUES (?, ?, ?)")) {

            stmt.setInt(1, this.usuarioId); // Utilizar el ID del usuario
            stmt.setInt(2, horarioId);
            stmt.setInt(3, asientoId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                // Marcar el asiento como no disponible
                marcarAsientoNoDisponible(asientoId);
                JOptionPane.showMessageDialog(this, "Asiento reservado con éxito");
                verSala();
            } else {
                JOptionPane.showMessageDialog(this, "Error al reservar el asiento");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al reservar asiento: " + ex.getMessage());
        }
    }


    private int obtenerAsientoId(String fila, int numero) {
        int asientoId = -1;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement stmt = conn.prepareStatement("SELECT id FROM asientos WHERE fila = ? AND numero = ? AND sala_id = (SELECT sala FROM horarios WHERE id = ?)")) {

            stmt.setString(1, fila);
            stmt.setInt(2, numero);
            stmt.setInt(3, horarioId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                asientoId = rs.getInt("id");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener ID del asiento: " + ex.getMessage());
        }

        return asientoId;
    }

    private void marcarAsientoNoDisponible(int asientoId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement stmt = conn.prepareStatement("UPDATE asientos SET disponible = FALSE WHERE id = ?")) {
            stmt.setInt(1, asientoId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar disponibilidad del asiento: " + ex.getMessage());
        }
    }
    private void volverAlMenuCliente() {
        ClientWindow ventanaCliente = new ClientWindow(1); // Reemplaza 1 con el ID de usuario correspondiente
        ventanaCliente.setVisible(true);
        dispose();
    }
/*
    private void volverAlMenuPrincipal() {
        // Crear una nueva instancia de ClientWindow y mostrarla
        new ClientWindow(int usuarioId).setVisible(true);
        // Cerrar la ventana actual
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReservarAsiento().setVisible(true));
    }*/
}