import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Index extends JFrame implements ActionListener {

    public Connection con;
    public Statement stmt;
    JPanel desktop;
    JPanel currentPanel;
    JMenuItem carrosMenuItemAdiciona;
    JMenuItem carrosMenuItemLista;
    JMenuItem carrosMenuItemBusca;
    JMenuItem clientesMenuItemAdiciona;
    JMenuItem clientesMenuItemLista;
    JMenuItem clientesMenuItemBusca;
    JMenuItem pedidosMenuItemAdiciona;
    JMenuItem pedidosMenuItemLista;
    JMenuItem pedidosMenuItemBusca;
    private Carro carro;
    private Cliente cliente;
    private Pedido pedido;

    public static void main(String[] args) {
        new Index();
    }

    public Index() {
        super("Car Sales System");
        setPreferredSize(new Dimension(1200, 700));
        setMaximumSize(new Dimension(1200, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);

        desktop = new JPanel(new BorderLayout());
        add(desktop);
        setJMenuBar(criaMenu());
        iniciaBD();
        this.carro = new Carro(con, stmt);
        this.cliente = new Cliente(con, stmt);
        this.pedido = new Pedido(con, stmt);
        CriaTabelas.CriaTabelasSistema(con, stmt);
        pack();
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
        Font menuFont = new Font("Arial", Font.PLAIN, 20);
        Font menuItemFont = new Font("Arial", Font.PLAIN, 20);

        UIManager.put("MenuBar.border", BorderFactory.createLineBorder(black, 1));
        UIManager.put("MenuBar.background", orange);
        UIManager.put("MenuItem.background", subOrange);
        UIManager.put("Menu.foreground", black);
        UIManager.put("MenuItem.foreground", black);
        UIManager.put("Menu.font", menuFont);
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
            trocarPainel(carro.carrosPanelAdiciona);
        }
        if (e.getSource() == carrosMenuItemBusca) {
            trocarPainel(carro.carrosPanelBusca);
        }
        if (e.getSource() == carrosMenuItemLista) {
            trocarPainel(carro.carrosPanelLista);
        }
        if (e.getSource() == clientesMenuItemAdiciona) {
            trocarPainel(cliente.clientesPanelAdiciona);
        }
        if (e.getSource() == clientesMenuItemBusca) {
            trocarPainel(cliente.clientesPanelBusca);
        }
        if (e.getSource() == clientesMenuItemLista) {
            trocarPainel(cliente.clientesPanelLista);
        }
        if (e.getSource() == pedidosMenuItemAdiciona) {
            trocarPainel(pedido.pedidosPanelAdiciona);
        }
        if (e.getSource() == pedidosMenuItemBusca) {
            trocarPainel(pedido.pedidosPanelBusca);
        }
        if (e.getSource() == pedidosMenuItemLista) {
            trocarPainel(pedido.pedidosPanelLista);
        }
    }

    private void trocarPainel(JPanel panel) {
        if (currentPanel != null) {
            desktop.remove(currentPanel);
        }

        currentPanel = panel;
        desktop.add(currentPanel, BorderLayout.CENTER);
        setBackground(Color.WHITE);
        currentPanel.setVisible(true);

        revalidate();
        repaint();
    }

    public class HomePanel extends JPanel {
        public HomePanel() {
            setLayout(new BorderLayout());

            ImageIcon imageIcon = new ImageIcon("./imgs/home_Carro.png");
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setBorder(new EmptyBorder(0, 60, 0, 0)); // Define a margem interna esquerda
            add(imageLabel, BorderLayout.WEST);

            JLabel welcomeLabel = new JLabel("Bem-vindo ao CSS!");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
            welcomeLabel.setBorder(new EmptyBorder(0, 35, 0, 0));
            add(welcomeLabel, BorderLayout.CENTER);
        }
    }
}
