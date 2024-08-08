import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * Clase ActualizarAsiento es una ventana de JFrame que permite actualizar la información
 * de un asiento en la base de datos, como el ID de sala, fila, número y disponibilidad.
 */
public class ActualizarAsiento extends JFrame {
    private JTextField txtSalaId;
    private JTextField txtFila;
    private JTextField txtNumero;
    private JCheckBox chkDisponible;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private int asientoId;

    /**
     * Constructor de la clase ActualizarAsiento.
     *
     * @param parent    El JFrame padre de la ventana actual.
     * @param asientoId El ID del asiento que se va a actualizar.
     */
    public ActualizarAsiento(JFrame parent, int asientoId) {
        super("Actualizar Asiento");
        this.asientoId = asientoId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Configuración de los componentes visuales y su disposición
        JLabel lblSalaId = new JLabel("ID de Sala:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblSalaId, gbc);

        txtSalaId = new JTextField(20);
        gbc.gridx = 1;
        add(txtSalaId, gbc);

        JLabel lblFila = new JLabel("Fila:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lblFila, gbc);

        txtFila = new JTextField(20);
        gbc.gridx = 1;
        add(txtFila, gbc);

        JLabel lblNumero = new JLabel("Número:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(lblNumero, gbc);

        txtNumero = new JTextField(20);
        gbc.gridx = 1;
        add(txtNumero, gbc);

        JLabel lblDisponible = new JLabel("Disponible:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(lblDisponible, gbc);

        chkDisponible = new JCheckBox();
        gbc.gridx = 1;
        add(chkDisponible, gbc);

        // Botón para actualizar los datos del asiento
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarAsiento();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        add(btnActualizar, gbc);

        // Botón para cancelar la operación y cerrar la ventana
        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 5;
        add(btnCancelar, gbc);

        // Cargar datos del asiento seleccionado
        cargarDatosAsiento();

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método para cargar los datos del asiento desde la base de datos.
     * Llena los campos del formulario con la información del asiento seleccionado.
     */
    private void cargarDatosAsiento() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement pstmt = con.prepareStatement("SELECT * FROM asientos WHERE id = ?")) {
            pstmt.setInt(1, asientoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    txtSalaId.setText(String.valueOf(rs.getInt("sala_id")));
                    txtFila.setText(rs.getString("fila"));
                    txtNumero.setText(String.valueOf(rs.getInt("numero")));
                    chkDisponible.setSelected(rs.getBoolean("disponible"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para actualizar los datos del asiento en la base de datos.
     * Verifica que todos los campos estén llenos y realiza la actualización.
     */
    private void actualizarAsiento() {
        String salaIdStr = txtSalaId.getText();
        String fila = txtFila.getText();
        String numeroStr = txtNumero.getText();
        boolean disponible = chkDisponible.isSelected();

        // Validación de campos vacíos
        if (salaIdStr.isEmpty() || fila.isEmpty() || numeroStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int salaId;
        int numero;

        // Validación de tipos de datos
        try {
            salaId = Integer.parseInt(salaIdStr);
            numero = Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de sala y número deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Actualización de los datos del asiento en la base de datos
        try (Connection con = DriverManager.getConnection("jdbc:mysql://ubizbip0ntk5uopb:vFULnkL51YQfK531npMk@b8shaoo2h7ajp78hvm5k-mysql.services.clever-cloud.com:3306/b8shaoo2h7ajp78hvm5k", "ubizbip0ntk5uopb", "vFULnkL51YQfK531npMk");
             PreparedStatement pstmt = con.prepareStatement("UPDATE asientos SET sala_id = ?, fila = ?, numero = ?, disponible = ? WHERE id = ?")) {
            pstmt.setInt(1, salaId);
            pstmt.setString(2, fila);
            pstmt.setInt(3, numero);
            pstmt.setBoolean(4, disponible);
            pstmt.setInt(5, asientoId);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Asiento actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar el asiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método principal que lanza la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Aquí debes pasar el ID del asiento a actualizar
            ActualizarAsiento ventanaActualizarAsiento = new ActualizarAsiento(null, 1);
            ventanaActualizarAsiento.setVisible(true);
        });
    }
}
