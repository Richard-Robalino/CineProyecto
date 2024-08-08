import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ActualizarAsiento extends JFrame {
    private JTextField txtSalaId;
    private JTextField txtFila;
    private JTextField txtNumero;
    private JCheckBox chkDisponible;
    private JButton btnActualizar;
    private JButton btnCancelar;
    private int asientoId;

    public ActualizarAsiento(JFrame parent, int asientoId) {
        super("Actualizar Asiento");
        this.asientoId = asientoId;
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

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parent.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 5;
        add(btnCancelar, gbc);

        cargarDatosAsiento();

        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void cargarDatosAsiento() {
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cine_reservas", "root", "123456");
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

    private void actualizarAsiento() {
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

        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/cine_reservas", "root", "123456");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Aquí debes pasar el ID del asiento a actualizar
            ActualizarAsiento ventanaActualizarAsiento = new ActualizarAsiento(null, 1);
            ventanaActualizarAsiento.setVisible(true);
        });
    }
}
