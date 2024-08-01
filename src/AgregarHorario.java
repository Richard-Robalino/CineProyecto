import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgregarHorario extends JFrame {
    private JTextField txtPeliculaId;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtSala;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionHorarios gestionHorarios;

    public AgregarHorario(GestionHorarios gestionHorarios) {
        super("Agregar Horario");
        this.gestionHorarios = gestionHorarios;
        setLayout(new GridLayout(5, 2));

        JLabel lblPeliculaId = new JLabel("ID de Película:");
        txtPeliculaId = new JTextField(20);
        add(lblPeliculaId);
        add(txtPeliculaId);

        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        txtFecha = new JTextField(20);
        add(lblFecha);
        add(txtFecha);

        JLabel lblHora = new JLabel("Hora (HH:MM:SS):");
        txtHora = new JTextField(20);
        add(lblHora);
        add(txtHora);

        JLabel lblSala = new JLabel("Sala:");
        txtSala = new JTextField(20);
        add(lblSala);
        add(txtSala);

        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int peliculaId = Integer.parseInt(txtPeliculaId.getText());
                String fecha = txtFecha.getText();
                String hora = txtHora.getText();
                int sala = Integer.parseInt(txtSala.getText());

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456")) {
                    String query = "INSERT INTO horarios (pelicula_id, fecha, hora, sala) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, peliculaId);
                        pstmt.setDate(2, Date.valueOf(fecha));
                        pstmt.setTime(3, Time.valueOf(hora));
                        pstmt.setInt(4, sala);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Horario agregado exitosamente");
                        dispose(); // Cierra la ventana actual
                        GestionHorarios ventanaGestionHorarios = new GestionHorarios(gestionHorarios.adminWindow);
                        ventanaGestionHorarios.setVisible(true); // Abre la ventana de GestionHorarios
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
                    ex.printStackTrace();
                }
            }
        });
        add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
                GestionHorarios ventanaGestionHorarios = new GestionHorarios(gestionHorarios.adminWindow);
                ventanaGestionHorarios.setVisible(true); // Abre la ventana de GestionHorarios
            }
        });
        add(btnCancelar);

        setSize(400, 300); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }
}
