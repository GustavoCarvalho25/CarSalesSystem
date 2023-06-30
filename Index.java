import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Index extends JFrame implements ActionListener {

    Connection con;
    Statement stmt;
    JDesktopPane desktop;
    JMenuItem carrosMenuItem;
    JMenuItem clientesMenuItem;
    JMenuItem pedidosMenuItem;
    JInternalFrame currentFrame;

    public static void main(String[] args) {
        new Index();
    }

    public Index() {
        super("Car System Sales");
        setBounds(50, 50, 700, 500);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktop = new JDesktopPane();
        add(desktop);

        setJMenuBar(criaMenu());
        iniciaBD();
        CriaTabelas.CriaTabelas(con, stmt);

        setVisible(true);
    }

    private JMenuBar criaMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Menu");
        menuBar.add(menu);

        carrosMenuItem = new JMenuItem("Carros");
        carrosMenuItem.addActionListener(this);
        menu.add(carrosMenuItem);

        clientesMenuItem = new JMenuItem("Clientes");
        clientesMenuItem.addActionListener(this);
        menu.add(clientesMenuItem);

        pedidosMenuItem = new JMenuItem("Pedidos");
        pedidosMenuItem.addActionListener(this);
        menu.add(pedidosMenuItem);

        return menuBar;
    }

    private void iniciaBD() {
        try {
            Class.forName("org.hsql.jdbcDriver");
            con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            stmt = con.createStatement();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Erro deu em Index" + "O driver do banco de dados não foi encontrado.\n" + ex, "Erro",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na iniciação do acesso ao banco de dados\n" + ex, "Erro",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == carrosMenuItem) {
            trocarPainel(new CarrosFrame());
        } else if (e.getSource() == clientesMenuItem) {
            trocarPainel(new ClientesFrame());
        } else if (e.getSource() == pedidosMenuItem) {
            trocarPainel(new PedidosFrame());
        }
    }

    private void trocarPainel(JInternalFrame frame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }

        currentFrame = frame;
        desktop.add(currentFrame);
        currentFrame.setVisible(true);
    }

    // Classe para a tela de Carros
    class CarrosFrame extends JInternalFrame {
        public CarrosFrame() {
            super("Carros", true, true, true, true);
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Adicione os componentes da tela de carros aqui
            // Exemplo:
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Carros"));
            add(panel);
        }
    }

    // Classe para a tela de Clientes
    class ClientesFrame extends JInternalFrame {
        public ClientesFrame() {
            super("Clientes", true, true, true, true);
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Adicione os componentes da tela de clientes aqui
            // Exemplo:
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Clientes"));
            add(panel);
        }
    }

    // Classe para a tela de Pedidos
    class PedidosFrame extends JInternalFrame {
        public PedidosFrame() {
            super("Pedidos", true, true, true, true);
            setSize(300, 200);
            setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            // Adicione os componentes da tela de pedidos aqui
            // Exemplo:
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Pedidos"));
            add(panel);
        }
    }
}