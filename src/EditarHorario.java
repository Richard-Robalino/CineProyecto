import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EditarHorario extends JFrame {
    private JTextField txtPeliculaId;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtSala;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionHorarios gestionHorarios;
    private int horarioId;

    public EditarHorario(GestionHorarios gestionHorarios, int horarioId) {
        super("Editar Horario");
        this.gestionHorarios = gestionHorarios;
        this.horarioId = horarioId;
        setLayout(new GridLayout(6, 2));
        setBackground(new Color(255, 255, 255)); // Fondo blanco

        // Etiquetas y campos de texto
        JLabel lblPeliculaId = new JLabel("ID de Película:");
        lblPeliculaId.setFont(new Font("Arial", Font.BOLD, 14));
        lblPeliculaId.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtPeliculaId = new JTextField(20);
        txtPeliculaId.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblPeliculaId);
        add(txtPeliculaId);

        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setFont(new Font("Arial", Font.BOLD, 14));
        lblFecha.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtFecha = new JTextField(20);
        txtFecha.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblFecha);
        add(txtFecha);

        JLabel lblHora = new JLabel("Hora (HH:MM:SS):");
        lblHora.setFont(new Font("Arial", Font.BOLD, 14));
        lblHora.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtHora = new JTextField(20);
        txtHora.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblHora);
        add(txtHora);

        JLabel lblSala = new JLabel("Sala:");
        lblSala.setFont(new Font("Arial", Font.BOLD, 14));
        lblSala.setForeground(new Color(50, 50, 50)); // Gris oscuro
        txtSala = new JTextField(20);
        txtSala.setFont(new Font("Arial", Font.PLAIN, 14));
        add(lblSala);
        add(txtSala);

        // Botón Guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.setBackground(new Color(34, 139, 34)); // Verde oscuro
        btnGuardar.setForeground(Color.WHITE); // Texto blanco
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int peliculaId = Integer.parseInt(txtPeliculaId.getText());
                String fecha = txtFecha.getText();
                String hora = txtHora.getText();
                int sala = Integer.parseInt(txtSala.getText());

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "UPDATE horarios SET pelicula_id=?, fecha=?, hora=?, sala=? WHERE id=?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, peliculaId);
                        pstmt.setDate(2, Date.valueOf(fecha));
                        pstmt.setTime(3, Time.valueOf(hora));
                        pstmt.setInt(4, sala);
                        pstmt.setInt(5, horarioId);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Horario actualizado exitosamente");
                        dispose(); // Cierra la ventana actual
                        GestionHorarios ventanaGestionHorarios = new GestionHorarios(gestionHorarios.adminWindow);
                        ventanaGestionHorarios.setVisible(true);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace();
                }
            }
        });
        add(btnGuardar);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.setBackground(new Color(255, 69, 58)); // Rojo claro
        btnCancelar.setForeground(Color.WHITE); // Texto blanco
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                GestionHorarios ventanaGestionHorarios = new GestionHorarios(gestionHorarios.adminWindow);
                ventanaGestionHorarios.setVisible(true);
            }
        });
        add(btnCancelar);

        cargarDatosHorario();

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarDatosHorario() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
            String query = "SELECT * FROM horarios WHERE id=?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, horarioId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        txtPeliculaId.setText(String.valueOf(rs.getInt("pelicula_id")));
                        txtFecha.setText(rs.getDate("fecha").toString());
                        txtHora.setText(rs.getTime("hora").toString());
                        txtSala.setText(String.valueOf(rs.getInt("sala")));
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
            ex.printStackTrace();
        }
    }
}
