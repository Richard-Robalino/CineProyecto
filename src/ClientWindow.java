import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ClientWindow extends JFrame {
    private JButton btnBuscarPeliculas;
    private JButton btnVerHorarios;
    private JButton btnReservarAsiento;
    private JButton btnVerHistorial;

    public ClientWindow() {
        super("Panel de Cliente");
        setLayout(new GridLayout(4, 1));

        btnBuscarPeliculas = new JButton("Buscar Películas");
        btnBuscarPeliculas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
                ventanaBuscarPeliculas.setVisible(true);
                dispose();
            }
        });
        add(btnBuscarPeliculas);

        btnVerHorarios = new JButton("Ver Horarios");
        btnVerHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                VerHorarios ventanaVerHorarios = new VerHorarios();
                ventanaVerHorarios.setVisible(true);
                dispose();
            }
        });
        add(btnVerHorarios);

        btnReservarAsiento = new JButton("Reservar Asiento");
        btnReservarAsiento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                ReservarAsiento ventanaReservarAsiento = new ReservarAsiento();
                ventanaReservarAsiento.setVisible(true);
            }
        });
        add(btnReservarAsiento);

        btnVerHistorial = new JButton("Ver Historial de Reservas");
        btnVerHistorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Solicitar clienteId al usuario
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
        add(btnVerHistorial);

        setSize(300, 300); // Ajusta el tamaño de la ventana
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