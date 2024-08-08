import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clase AgregarAsiento representa la ventana de diálogo para agregar un nuevo asiento
 * en la base de datos de la aplicación. Permite al usuario ingresar los detalles
 * del asiento, como el ID de la sala, la fila, el número, y si está disponible.
 */
public class AgregarAsiento extends JFrame {
    private JTextField txtSalaId;
    private JTextField txtFila;
    private JTextField txtNumero;
    private JCheckBox chkDisponible;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionAsientos gestionAsientos;

    /**
     * Constructor de la clase AgregarAsiento.
     *
     * @param gestionAsientos Referencia a la ventana de gestión de asientos (GestionAsientos).
     *                        Se utiliza para volver a mostrarla cuando se cierra esta ventana.
     */
    public AgregarAsiento(GestionAsientos gestionAsientos) {
        super("Agregar Asiento");
        this.gestionAsientos = gestionAsientos; // Guardar referencia a GestionAsientos
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta y campo de texto para el ID de Sala
        JLabel lblSalaId = new JLabel("ID de Sala:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblSalaId, gbc);

        txtSalaId = new JTextField(20);
        gbc.gridx = 1;
        add(txtSalaId, gbc);

        // Etiqueta y campo de texto para la fila
        JLabel lblFila = new JLabel("Fila:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblFila, gbc);

        txtFila = new JTextField(20);
        gbc.gridx = 1;
        add(txtFila, gbc);

        // Etiqueta y campo de texto para el número de asiento
        JLabel lblNumero = new JLabel("Número:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblNumero, gbc);

        txtNumero = new JTextField(20);
        gbc.gridx = 1;
        add(txtNumero, gbc);

        // Etiqueta y checkbox para indicar si el asiento está disponible
        JLabel lblDisponible = new JLabel("Disponible:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblDisponible, gbc);

        chkDisponible = new JCheckBox();
        gbc.gridx = 1;
        add(chkDisponible, gbc);

        // Botón para guardar el nuevo asiento
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarAsiento();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(btnGuardar, gbc);

        // Botón para cancelar la operación y cerrar la ventana
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                if (gestionAsientos != null) {
                    gestionAsientos.setVisible(true); // Muestra la ventana de GestionAsientos
                }
            }
        });
        gbc.gridy = 5;
        add(btnCancelar, gbc);

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método que guarda el asiento en la base de datos.
     * Verifica que los campos estén llenos y que los valores ingresados sean válidos.
     * Luego inserta el nuevo asiento en la base de datos.
     */
    private void guardarAsiento() {
        String salaIdStr = txtSalaId.getText();
        String fila = txtFila.getText();
        String numeroStr = txtNumero.getText();
        boolean disponible = chkDisponible.isSelected();

        // Validar que todos los campos estén llenos
        if (salaIdStr.isEmpty() || fila.isEmpty() || numeroStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int salaId;
        int numero;

        // Intentar convertir los campos de ID de sala y número a enteros
        try {
            salaId = Integer.parseInt(salaIdStr);
            numero = Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de sala y número deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Guardar el asiento en la base de datos
        try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement pstmt = con.prepareStatement("INSERT INTO asientos (sala_id, fila, numero, disponible) VALUES (?, ?, ?, ?)")) {
            pstmt.setInt(1, salaId);
            pstmt.setString(2, fila);
            pstmt.setInt(3, numero);
            pstmt.setBoolean(4, disponible);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Asiento agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al agregar el asiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal que lanza la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgregarAsiento ventanaAgregarAsiento = new AgregarAsiento(null);
            ventanaAgregarAsiento.setVisible(true);
        });
    }
}
