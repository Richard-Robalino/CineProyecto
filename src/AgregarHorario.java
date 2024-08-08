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
        setLayout(new GridLayout(5, 2, 10, 10)); // Añadido espaciado entre componentes

        // Fondo de la ventana
        getContentPane().setBackground(new Color(240, 240, 240)); // Color gris claro

        // Estilo de los componentes
        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);
        Color buttonColor = new Color(34, 139, 34); // Color verde oscuro
        Color buttonTextColor = Color.WHITE; // Texto blanco

        // Etiqueta y campo ID de Película
        JLabel lblPeliculaId = new JLabel("ID de Película:");
        lblPeliculaId.setFont(labelFont);
        txtPeliculaId = new JTextField(20);
        txtPeliculaId.setFont(textFieldFont);
        add(lblPeliculaId);
        add(txtPeliculaId);

        // Etiqueta y campo Fecha
        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setFont(labelFont);
        txtFecha = new JTextField(20);
        txtFecha.setFont(textFieldFont);
        add(lblFecha);
        add(txtFecha);

        // Etiqueta y campo Hora
        JLabel lblHora = new JLabel("Hora (HH:MM:SS):");
        lblHora.setFont(labelFont);
        txtHora = new JTextField(20);
        txtHora.setFont(textFieldFont);
        add(lblHora);
        add(txtHora);

        // Etiqueta y campo Sala
        JLabel lblSala = new JLabel("Sala:");
        lblSala.setFont(labelFont);
        txtSala = new JTextField(20);
        txtSala.setFont(textFieldFont);
        add(lblSala);
        add(txtSala);

        // Botón Guardar
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(buttonColor);
        btnGuardar.setForeground(buttonTextColor);
        btnGuardar.setFont(textFieldFont.deriveFont(Font.BOLD));
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int peliculaId = Integer.parseInt(txtPeliculaId.getText());
                String fecha = txtFecha.getText();
                String hora = txtHora.getText();
                int sala = Integer.parseInt(txtSala.getText());

                try (Connection conn = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk")) {
                    String query = "INSERT INTO horarios (pelicula_id, fecha, hora, sala) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setInt(1, peliculaId);
                        pstmt.setDate(2, Date.valueOf(fecha));
                        pstmt.setTime(3, Time.valueOf(hora));
                        pstmt.setInt(4, sala);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Horario agregado exitosamente");
                        dispose();
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
        btnCancelar.setBackground(new Color(105, 105, 105)); // Color gris oscuro
        btnCancelar.setForeground(buttonTextColor);
        btnCancelar.setFont(textFieldFont.deriveFont(Font.BOLD));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                GestionHorarios ventanaGestionHorarios = new GestionHorarios(gestionHorarios.adminWindow);
                ventanaGestionHorarios.setVisible(true);
            }
        });
        add(btnCancelar);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}
