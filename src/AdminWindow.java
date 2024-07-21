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
                GestionHorarios ventanaGestionHorarios = new GestionHorarios(AdminWindow.this);
                ventanaGestionHorarios.setVisible(true);
                dispose();
            }
        });
        add(btnGestionarHorarios);

        btnVerEstadisticas = new JButton("Ver Estadísticas");
        btnVerEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                VerEstadisticas ventanaVerEstadisticas = new VerEstadisticas(AdminWindow.this);
                ventanaVerEstadisticas.setVisible(true);
                dispose();
            }
        });
        add(btnVerEstadisticas);

        btnGestionarClientes = new JButton("Gestionar Clientes");
        btnGestionarClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                GestionClientes ventanaGestionClientes = new GestionClientes(AdminWindow.this);
                ventanaGestionClientes.setVisible(true);
                dispose();
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
