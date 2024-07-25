import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BuscarPeliculas extends JFrame {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblPeliculas;
    private DefaultTableModel modelo;

    public BuscarPeliculas() {
        super("Buscar Películas");
        setLayout(new BorderLayout());

        // Panel de búsqueda
        JPanel pnlBuscar = new JPanel();
        pnlBuscar.setLayout(new FlowLayout());
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buscarPeliculas();
            }
        });
        pnlBuscar.add(txtBuscar);
        pnlBuscar.add(btnBuscar);
        add(pnlBuscar, BorderLayout.NORTH);

        // Panel de resultados
        JPanel pnlResultados = new JPanel();
        pnlResultados.setLayout(new BorderLayout());
        tblPeliculas = new JTable();
        modelo = new DefaultTableModel();
        modelo.addColumn("Título");
        modelo.addColumn("Duración");
        modelo.addColumn("Clasificación");
        tblPeliculas.setModel(modelo);
        pnlResultados.add(new JScrollPane(tblPeliculas), BorderLayout.CENTER);
        add(pnlResultados, BorderLayout.CENTER);

        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void buscarPeliculas() {
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un título de película para buscar");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine_reservas", "root", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM peliculas WHERE titulo LIKE '%" + buscar + "%'");

            modelo.setRowCount(0);
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getString("titulo");
                fila[1] = rs.getInt("duracion");
                fila[2] = rs.getString("clasificacion");
                modelo.addRow(fila);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar películas: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BuscarPeliculas ventanaBuscarPeliculas = new BuscarPeliculas();
            ventanaBuscarPeliculas.setVisible(true);
        });
    }
}