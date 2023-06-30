import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Index extends JFrame implements ActionListener {

    public Connection con;
    public Statement stmt;
    JPanel desktop;
    JMenuItem carrosMenuItemAdiciona;
    JMenuItem carrosMenuItemLista;
    JMenuItem carrosMenuItemBusca;
    JMenuItem clientesMenuItemAdiciona;
    JMenuItem clientesMenuItemLista;
    JMenuItem clientesMenuItemBusca;
    JMenuItem pedidosMenuItemAdiciona;
    JMenuItem pedidosMenuItemLista;
    JMenuItem pedidosMenuItemBusca;

    JPanel currentPanel;

    public static void main(String[] args) {
        new Index();
    }

    public Index() {
        super("Car System Sales");
        setBounds(0, 0, 700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);
        desktop = new JPanel(new BorderLayout());
        add(desktop);
        System.out.println("Entrou no Index");
        setJMenuBar(criaMenu());
        iniciaBD();
        CriaTabelas.CriaTabelasSistema(con, stmt);
        setLocationRelativeTo(null);

        setVisible(true);
        HomePanel homePanel = new HomePanel();
        trocarPainel(homePanel);
    }

    private JMenuBar criaMenu() {
        JMenuBar menuBar = new JMenuBar();
        Color black = new Color(0, 0, 0);
        Color orange = new Color(239, 165, 82);
        Color subOrange = new Color(250, 225, 199);
        UIManager.put("MenuBar.border", BorderFactory.createLineBorder(black, 1));
        UIManager.put("MenuBar.background", orange);
        UIManager.put("MenuItem.background", subOrange);
        UIManager.put("Menu.foreground", black);
        UIManager.put("MenuItem.foreground", black);
        Font menuFont = new Font("Arial", Font.PLAIN, 20);
        UIManager.put("Menu.font", menuFont);

        Font menuItemFont = new Font("Arial", Font.PLAIN, 20);
        UIManager.put("MenuItem.font", menuItemFont);

        JMenu homePage = new JMenu("Home");
        JMenu carros = new JMenu("Carros");
        JMenu clientes = new JMenu("Clientes");
        JMenu pedidos = new JMenu("Pedidos");

        homePage.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                trocarPainel(new HomePanel());
            }
        });

        menuBar.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        menuBar.add(homePage, constraints);

        constraints.gridx = 1;
        menuBar.add(carros, constraints);

        constraints.gridx = 2;
        menuBar.add(clientes, constraints);

        constraints.gridx = 3;
        menuBar.add(pedidos, constraints);

        carrosMenuItemAdiciona = new JMenuItem("Adicionar");
        carrosMenuItemBusca = new JMenuItem("Buscar");
        carrosMenuItemLista = new JMenuItem("Listar");

        carrosMenuItemAdiciona.addActionListener(this);
        carrosMenuItemBusca.addActionListener(this);
        carrosMenuItemLista.addActionListener(this);

        carros.add(carrosMenuItemAdiciona);
        carros.add(carrosMenuItemBusca);
        carros.add(carrosMenuItemLista);

        clientesMenuItemAdiciona = new JMenuItem("Adicionar");
        clientesMenuItemBusca = new JMenuItem("Buscar");
        clientesMenuItemLista = new JMenuItem("Listar");
        clientesMenuItemAdiciona.addActionListener(this);
        clientesMenuItemBusca.addActionListener(this);
        clientesMenuItemLista.addActionListener(this);
        clientes.add(clientesMenuItemAdiciona);
        clientes.add(clientesMenuItemBusca);
        clientes.add(clientesMenuItemLista);

        pedidosMenuItemAdiciona = new JMenuItem("Adicionar");
        pedidosMenuItemBusca = new JMenuItem("Buscar");
        pedidosMenuItemLista = new JMenuItem("Listar");
        pedidosMenuItemAdiciona.addActionListener(this);
        pedidosMenuItemBusca.addActionListener(this);
        pedidosMenuItemLista.addActionListener(this);
        pedidos.add(pedidosMenuItemAdiciona);
        pedidos.add(pedidosMenuItemBusca);
        pedidos.add(pedidosMenuItemLista);

        return menuBar;
    }

    public void iniciaBD() {
        try {
            Class.forName("org.hsql.jdbcDriver");
            con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            stmt = con.createStatement();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null,
                    "Erro deu em Index" + "O driver do banco de dados não foi encontrado.\n" + ex, "Erro",
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
        if (e.getSource() == carrosMenuItemAdiciona) {
            trocarPainel(new CarrosPanelAdiciona());
        } else if (e.getSource() == carrosMenuItemBusca) {
            trocarPainel(new CarrosPanelBusca());
        } else if (e.getSource() == carrosMenuItemLista) {
            trocarPainel(new CarrosPanelLista());
        } else if (e.getSource() == clientesMenuItemAdiciona) {
            trocarPainel(new ClientesPanelAdiciona());
        } else if (e.getSource() == clientesMenuItemBusca) {
            trocarPainel(new ClientesPanelBusca());
        } else if (e.getSource() == clientesMenuItemLista) {
            trocarPainel(new ClientesPanelLista());
        } else if (e.getSource() == pedidosMenuItemAdiciona) {
            trocarPainel(new PedidosPanelAdiciona());
        } else if (e.getSource() == pedidosMenuItemBusca) {
            trocarPainel(new PedidosPanelBusca());
        } else if (e.getSource() == pedidosMenuItemLista) {
            trocarPainel(new PedidosPanelLista());
        }
    }

    private void trocarPainel(JPanel panel) {
        if (currentPanel != null) {
            desktop.remove(currentPanel);
        }

        currentPanel = panel;
        desktop.add(currentPanel, BorderLayout.CENTER);
        currentPanel.setVisible(true);

        revalidate();
        repaint();
    }

    // Classe para adicionar carros
    class CarrosPanelAdiciona extends JPanel {
        public CarrosPanelAdiciona() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de adicionar carros aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Adicionar Carros"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para buscar carros
    class CarrosPanelBusca extends JPanel {
        public CarrosPanelBusca() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de buscar carros aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Buscar Carros"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para listar carros
    class CarrosPanelLista extends JPanel {
        public CarrosPanelLista() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de listar carros aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Listar Carros"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para adicionar clientes
    class ClientesPanelAdiciona extends JPanel {
        public ClientesPanelAdiciona() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de adicionar clientes aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Adicionar Clientes"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para buscar clientes
    class ClientesPanelBusca extends JPanel {
        public ClientesPanelBusca() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de buscar clientes aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Buscar Clientes"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para listar clientes
    class ClientesPanelLista extends JPanel {
        public ClientesPanelLista() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de listar clientes aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Listar Clientes"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para adicionar pedidos
    class PedidosPanelAdiciona extends JPanel {
        public PedidosPanelAdiciona() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de adicionar pedidos aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Adicionar Pedidos"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para buscar pedidos
    class PedidosPanelBusca extends JPanel {
        public PedidosPanelBusca() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de buscar pedidos aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Buscar Pedidos"));
            add(panel, BorderLayout.CENTER);
        }
    }

    // Classe para listar pedidos
    class PedidosPanelLista extends JPanel {
        public PedidosPanelLista() {
            setLayout(new BorderLayout());
            setSize(300, 200);

            // Adicione os componentes da tela de listar pedidos aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Listar Pedidos"));
            add(panel, BorderLayout.CENTER);
        }
    }

    class HomePanel extends JPanel {
        public HomePanel() {
            setLayout(new BorderLayout());

            ImageIcon imageIcon = new ImageIcon("../imgs/car.png");
            JLabel imageLabel = new JLabel(imageIcon);
            add(imageLabel, BorderLayout.WEST);

            JLabel welcomeLabel = new JLabel("Bem-vindo ao sistema de carros!");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            add(welcomeLabel, BorderLayout.CENTER);
        }
    }
}
