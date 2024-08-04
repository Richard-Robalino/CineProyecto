import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientWindow extends JFrame {
    private JButton btnBuscarPeliculas;
    private JButton btnVerHorarios;
    private JButton btnReservarAsiento;
    private JButton btnVerHistorial;

    public ClientWindow() {
        super("Panel de Cliente");
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

        btnBuscarPeliculas = createButton("Buscar Películas", "buscar_peliculas.png");
        btnBuscarPeliculas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
                ventanaBuscarPeliculas.setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0;
        add(btnBuscarPeliculas, gbc);

        btnVerHorarios = createButton("Ver Horarios", "ver_horarios.png");
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VerHorarios ventanaVerHorarios = new VerHorarios();
                ventanaVerHorarios.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 2;
        add(btnVerHorarios, gbc);

        btnReservarAsiento = createButton("Reservar Asiento", "reservar_asiento.png");
        btnReservarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReservarAsiento ventanaReservarAsiento = new ReservarAsiento();
                ventanaReservarAsiento.setVisible(true);
            }
        });
        gbc.gridy = 3;
        add(btnReservarAsiento, gbc);

        btnVerHistorial = createButton("Ver Historial de Reservas", "ver_historial.png");
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog("Ingrese el ID del cliente:");
                int clienteId;
                try {
                    clienteId = Integer.parseInt(input);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID de cliente no válido. Usando ID predeterminado 3.");
                    clienteId = 3; // ID predeterminado en caso de entrada no válida
                }
                VerHistorialReservas ventanaVerHistorialReservas = new VerHistorialReservas(clienteId);
                ventanaVerHistorialReservas.setVisible(true);
            }
        });
        gbc.gridy = 4;
        add(btnVerHistorial, gbc);

        setSize(400, 500); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private JButton createButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setIcon(new ImageIcon(imagePath));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(250, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientWindow ventanaCliente = new ClientWindow();
            ventanaCliente.setVisible(true);
        });
    }
}
