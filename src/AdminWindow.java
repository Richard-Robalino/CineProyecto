import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AdminWindow extends JFrame {
    private JButton btnAgregarPelicula;
    private JButton btnGestionarHorarios;
    private JButton btnVerEstadisticas;
    private JButton btnGestionarClientes;
    private JButton btnGestionarAsientos;

    public AdminWindow() {
        super("Panel de Administrador");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Panel de Administrador", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLUE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;

        btnAgregarPelicula = createButton("Agregar Película", "agregar_pelicula.png");
        btnAgregarPelicula.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarPelicula ventanaAgregarPelicula = new AgregarPelicula(AdminWindow.this);
                ventanaAgregarPelicula.setVisible(true);
                dispose();
            }
        });
        gbc.gridx = 0;
        add(btnAgregarPelicula, gbc);

        btnGestionarHorarios = createButton("Gestionar Horarios", "gestionar_horarios.png");
        btnGestionarHorarios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GestionHorarios ventanaGestionHorarios = new GestionHorarios(AdminWindow.this);
                ventanaGestionHorarios.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 2;
        add(btnGestionarHorarios, gbc);

        btnVerEstadisticas = createButton("Ver Estadísticas", "ver_estadisticas.png");
        btnVerEstadisticas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VerEstadisticas ventanaVerEstadisticas = new VerEstadisticas(AdminWindow.this);
                ventanaVerEstadisticas.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 3;
        add(btnVerEstadisticas, gbc);

        btnGestionarClientes = createButton("Gestionar Clientes", "gestionar_clientes.png");
        btnGestionarClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GestionClientes ventanaGestionClientes = new GestionClientes(AdminWindow.this);
                ventanaGestionClientes.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 4;
        add(btnGestionarClientes, gbc);

        btnGestionarAsientos = createButton("Gestionar Asientos", "gestionar_asientos.png");
        btnGestionarAsientos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GestionAsientos ventanaGestionAsientos = new GestionAsientos(AdminWindow.this);
                ventanaGestionAsientos.setVisible(true);
                dispose();
            }
        });
        gbc.gridy = 5;
        add(btnGestionarAsientos, gbc);
        ;

        setSize(400, 500); // Ajusta el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
    }

    private JButton createButton(String text, String imagePath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setIcon(new ImageIcon(imagePath));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminWindow ventanaAdmin = new AdminWindow();
            ventanaAdmin.setVisible(true);
        });
    }
}
