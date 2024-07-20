import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminWindow extends JFrame {
    private JButton btnAgregarPelicula;
    private JButton btnGestionarHorarios;
    private JButton btnVerEstadisticas;
    private JButton btnGestionarClientes;

    public AdminWindow() {
        super("Panel de Administrador");
        setLayout(new GridLayout(4, 1));

        btnAgregarPelicula = new JButton("Agregar Película");
        btnAgregarPelicula.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarPelicula ventanaAgregarPelicula = new AgregarPelicula(AdminWindow.this);
                ventanaAgregarPelicula.setVisible(true);
                dispose();
            }
        });
        add(btnAgregarPelicula);

        btnGestionarHorarios = new JButton("Gestionar Horarios");
        btnGestionarHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para abrir la ventana de gestión de horarios
                // Ejemplo:
                // GestionHorarios ventanaGestionHorarios = new GestionHorarios();
                // ventanaGestionHorarios.setVisible(true);
            }
        });
        add(btnGestionarHorarios);

        btnVerEstadisticas = new JButton("Ver Estadísticas");
        btnVerEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para ver estadísticas de ocupación y ventas
                // Ejemplo:
                // VerEstadisticas ventanaVerEstadisticas = new VerEstadisticas();
                // ventanaVerEstadisticas.setVisible(true);
            }
        });
        add(btnVerEstadisticas);

        btnGestionarClientes = new JButton("Gestionar Clientes");
        btnGestionarClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Lógica para abrir la ventana de gestión de clientes
                // Ejemplo:
                // GestionClientes ventanaGestionClientes = new GestionClientes();
                // ventanaGestionClientes.setVisible(true);
            }
        });
        add(btnGestionarClientes);

        setSize(300, 300); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
