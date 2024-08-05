import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientWindow extends JFrame {
    private JButton btnBuscarPeliculas;
    private JButton btnVerHorarios;
    private JButton btnReservarAsiento;
    private JButton btnVerHistorial;

    public ClientWindow() {
        super("Panel de Cliente");
        setLayout(new GridLayout(5, 1, 10, 10)); // Ajuste de espaciado en el GridLayout

        // Panel de botones
        JPanel pnlBotones = new JPanel();
        pnlBotones.setLayout(new GridLayout(4, 1, 10, 10));
        pnlBotones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        pnlBotones.setBackground(new Color(200, 220, 240));

        btnBuscarPeliculas = new JButton("Buscar Películas");
        btnBuscarPeliculas.setFont(new Font("Arial", Font.BOLD, 16));
        btnBuscarPeliculas.setBackground(new Color(70, 130, 180));
        btnBuscarPeliculas.setForeground(Color.WHITE);
        btnBuscarPeliculas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
                ventanaBuscarPeliculas.setVisible(true);
                dispose();
            }
        });
        pnlBotones.add(btnBuscarPeliculas);

        btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.setFont(new Font("Arial", Font.BOLD, 16));
        btnVerHorarios.setBackground(new Color(70, 130, 180));
        btnVerHorarios.setForeground(Color.WHITE);
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VerHorarios ventanaVerHorarios = new VerHorarios();
                ventanaVerHorarios.setVisible(true);
                dispose();
            }
        });
        pnlBotones.add(btnVerHorarios);

        btnReservarAsiento = new JButton("Reservar Asiento");
        btnReservarAsiento.setFont(new Font("Arial", Font.BOLD, 16));
        btnReservarAsiento.setBackground(new Color(70, 130, 180));
        btnReservarAsiento.setForeground(Color.WHITE);
        btnReservarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReservarAsiento ventanaReservarAsiento = new ReservarAsiento();
                ventanaReservarAsiento.setVisible(true);
                dispose();
            }
        });
        pnlBotones.add(btnReservarAsiento);

        btnVerHistorial = new JButton("Ver Historial de Reservas");
        btnVerHistorial.setFont(new Font("Arial", Font.BOLD, 16));
        btnVerHistorial.setBackground(new Color(70, 130, 180));
        btnVerHistorial.setForeground(Color.WHITE);
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
        pnlBotones.add(btnVerHistorial);

        add(pnlBotones);

        setSize(400, 400); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientWindow ventanaCliente = new ClientWindow();
            ventanaCliente.setVisible(true);
        });
    }
}
