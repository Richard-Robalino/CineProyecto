import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class VerPeliculas extends JFrame {
    private JPanel panelMovies;

    public VerPeliculas() {
        super("Client Panel");

        // Establecer el color de fondo de la ventana
        getContentPane().setBackground(new Color(240, 248, 255)); // Color azul claro

        panelMovies = new JPanel();
        panelMovies.setLayout(new BoxLayout(panelMovies, BoxLayout.Y_AXIS));
        panelMovies.setBackground(new Color(240, 248, 255)); // Color azul claro

        JScrollPane scrollPane = new JScrollPane(panelMovies);
        add(scrollPane);

        loadMovies();

        setSize(600, 500); // Ajuste del tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void loadMovies() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM peliculas";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String movieTitle = rs.getString("titulo");
                int movieId = rs.getInt("id");
                JButton btnMovie = new JButton(movieTitle);
                btnMovie.setBackground(new Color(70, 130, 180)); // Color de fondo del botón
                btnMovie.setForeground(Color.WHITE); // Color del texto del botón
                btnMovie.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente del texto del botón
                btnMovie.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Espaciado interno del botón
                btnMovie.addActionListener(e -> showMovieDetails(movieId));
                panelMovies.add(btnMovie);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading movies: " + ex.getMessage());
        }
    }

    private void showMovieDetails(int movieId) {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM peliculas WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String details = "Título: " + rs.getString("titulo") +
                        "\nDuración: " + rs.getInt("duracion") + " minutos" +
                        "\nDescripción: " + rs.getString("descripcion") +
                        "\nClasificación: " + rs.getString("clasificacion");

                JOptionPane.showMessageDialog(this, details, "Detalles de la Película", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading movie details: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VerPeliculas ventanaVerPeliculas = new VerPeliculas();
            ventanaVerPeliculas.setVisible(true);
        });
    }
}
