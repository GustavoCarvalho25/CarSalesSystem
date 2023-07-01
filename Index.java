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
        //this.pedido = new Pedido(con, stmt);
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
            trocarPainel(new PedidosPanelAdiciona());
        }
        if (e.getSource() == pedidosMenuItemBusca) {
            trocarPainel(new PedidosPanelBusca());
        }
        if (e.getSource() == pedidosMenuItemLista) {
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

    // Classe para adicionar pedidos
    class PedidosPanelAdiciona extends JPanel {
        private JTextField cliIdTextField;
        private JTextField carIdTextField;
        private JTextField dataVendaTextField;
        private JTextField taxaTextField;

        public PedidosPanelAdiciona() {
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout()); // Painel principal
            JPanel leftPanel = new JPanel(new BorderLayout()); // Painel para a imagem à esquerda

            // Ícone à esquerda
            ImageIcon icon = new ImageIcon("./imgs/adiciona_Pedido.png");
            JLabel iconLabel = new JLabel(icon);
            leftPanel.add(iconLabel, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5); // Define as margens internas dos componentes

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Campo de texto para ID Cliente
            JLabel cliIdLabel = new JLabel("ID Cliente:");
            cliIdLabel.setFont(cliIdLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            cliIdTextField = new JTextField(20);
            cliIdTextField.setFont(cliIdTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(cliIdLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(cliIdTextField, constraints);

            // Campo de texto para ID Carro
            JLabel carIdLabel = new JLabel("ID Carro:");
            carIdLabel.setFont(carIdLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            carIdTextField = new JTextField(20);
            carIdTextField.setFont(carIdTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 2;
            formPanel.add(carIdLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(carIdTextField, constraints);

            // Campo de texto para Data de Venda
            JLabel dataVendaLabel = new JLabel("Data de Venda:");
            dataVendaLabel.setFont(dataVendaLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            dataVendaTextField = new JTextField(20);
            dataVendaTextField.setFont(dataVendaTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da
                                                                                                 // fonte
            constraints.gridy = 4;
            formPanel.add(dataVendaLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(dataVendaTextField, constraints);

            // Campo de texto para Taxa
            JLabel taxaLabel = new JLabel("Taxa:");
            taxaLabel.setFont(taxaLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            taxaTextField = new JTextField(20);
            taxaTextField.setFont(taxaTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 6;
            formPanel.add(taxaLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(taxaTextField, constraints);

            // Botão "Adicionar"
            JButton adicionarButton = new JButton("Adicionar");
            constraints.gridx = 0;
            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.anchor = GridBagConstraints.EAST;
            constraints.insets = new Insets(10, 0, 0, 5);
            formPanel.add(adicionarButton, constraints);

            adicionarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    adicionarButtonActionPerformed(e);
                }
            });

            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 0));
        }

        private void adicionarButtonActionPerformed(ActionEvent e) {
            String cliId = cliIdTextField.getText().trim();
            String carId = carIdTextField.getText().trim();
            String dataVenda = dataVendaTextField.getText().trim();
            String taxa = taxaTextField.getText().trim();

            // Verificações e condições
            if (cliId.isEmpty() || carId.isEmpty() || dataVenda.isEmpty() || taxa.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos os campos devem ser preenchidos.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!cliId.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "O ID do cliente deve conter apenas números.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!carId.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "O ID do carro deve conter apenas números.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Conversão da data para o formato "YYYY-MM-DD"
                    DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date utilDate = inputFormat.parse(dataVenda);
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                    // Inserir os dados do pedido no banco de dados
                    int cliIdInt = Integer.parseInt(cliId);
                    int carIdInt = Integer.parseInt(carId);
                    float taxaFloat = Float.parseFloat(taxa);

                    Class.forName("org.hsql.jdbcDriver");
                    stmt.executeUpdate("INSERT INTO PEDIDO(CLI_ID, CAR_ID, DATAVENDA, TAXA) VALUES ("
                            + cliIdInt + ", " + carIdInt + ", '" + sqlDate + "', " + taxaFloat + ")");
                    stmt.close();
                    con.close();

                    JOptionPane.showMessageDialog(this,
                            "Pedido adicionado com sucesso!\n",
                            "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                    // Limpar os campos
                    cliIdTextField.setText("");
                    carIdTextField.setText("");
                    dataVendaTextField.setText("");
                    taxaTextField.setText("");
                } catch (Exception ex) {
                    System.out.println("Erro ocorreu em PedidosPanelAdiciona: " + ex);
                }
            }
        }
    }

    // Classe para buscar pedidos
    class PedidosPanelBusca extends JPanel {
        public PedidosPanelBusca() {
            setLayout(new BorderLayout());

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

            // Adicione os componentes da tela de listar pedidos aqui
            JPanel panel = new JPanel();
            panel.add(new JLabel("Tela de Listar Pedidos"));
            add(panel, BorderLayout.CENTER);
        }
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
