import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase ClientWindow representa el panel principal del cliente.
 * Permite al usuario realizar diferentes acciones como buscar películas,
 * ver horarios, reservar asientos y ver el historial de reservas.
 */
public class ClientWindow extends JFrame {
    private JButton btnBuscarPeliculas;
    private JButton btnVerHorarios;
    private JButton btnReservarAsiento;
    private JButton btnVerHistorial;
    private int usuarioId;

    /**
     * Constructor de la clase ClientWindow.
     * Inicializa la interfaz de usuario con botones para las diferentes funcionalidades.
     *
     * @param usuarioId El ID del usuario que accede a la ventana del cliente.
     */
    public ClientWindow(int usuarioId) {
        super("Panel de Cliente");
        this.usuarioId = usuarioId;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Panel de Cliente", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        btnBuscarPeliculas = createButton("Buscar Películas");
        btnBuscarPeliculas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
                ventanaBuscarPeliculas.setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0;
        add(btnBuscarPeliculas, gbc);

        btnVerHorarios = createButton("Ver Horarios");
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VerHorarios ventanaVerHorarios = new VerHorarios();
                ventanaVerHorarios.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 2;
        add(btnVerHorarios, gbc);

        btnReservarAsiento = createButton("Reservar Asiento");
        btnReservarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReservarAsiento ventanaReservarAsiento = new ReservarAsiento(usuarioId);
                ventanaReservarAsiento.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 3;
        add(btnReservarAsiento, gbc);

        btnVerHistorial = createButton("Ver Historial de Reservas");
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreCliente = JOptionPane.showInputDialog("Ingrese el nombre del cliente:");
                if (nombreCliente != null && !nombreCliente.trim().isEmpty()) {
                    VerHistorialReservas ventanaVerHistorialReservas = new VerHistorialReservas(nombreCliente);
                    ventanaVerHistorialReservas.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Nombre de cliente no válido.");
                }
            }
        });
        gbc.gridy = 4;
        add(btnVerHistorial, gbc);

        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Método auxiliar para crear botones con un estilo uniforme.
     *
     * @param text El texto que aparecerá en el botón.
     * @return Un JButton configurado con el texto proporcionado.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientWindow ventanaCliente = new ClientWindow();
            ventanaCliente.setVisible(true);
        });
    }*/
}
