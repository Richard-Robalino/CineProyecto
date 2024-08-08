import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AgregarAsiento extends JFrame {
    private JTextField txtSalaId;
    private JTextField txtFila;
    private JTextField txtNumero;
    private JCheckBox chkDisponible;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private GestionAsientos gestionAsientos;

    public AgregarAsiento(GestionAsientos gestionAsientos) {
        super("Agregar Asiento");
        this.gestionAsientos = gestionAsientos; // Guardar referencia a GestionAsientos
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

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

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Cierra la ventana actual
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

    private void guardarAsiento() {
        String salaIdStr = txtSalaId.getText();
        String fila = txtFila.getText();
        String numeroStr = txtNumero.getText();
        boolean disponible = chkDisponible.isSelected();

        if (salaIdStr.isEmpty() || fila.isEmpty() || numeroStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos deben ser llenados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int salaId;
        int numero;

        try {
            salaId = Integer.parseInt(salaIdStr);
            numero = Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de sala y número deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AgregarAsiento ventanaAgregarAsiento = new AgregarAsiento(null);
            ventanaAgregarAsiento.setVisible(true);
        });
    }
}
