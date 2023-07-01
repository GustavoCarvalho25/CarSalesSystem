import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        super("Car Sales System");
        setPreferredSize(new Dimension(1200, 700));
        setMaximumSize(new Dimension(1200, 900));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.NORMAL);

        desktop = new JPanel(new BorderLayout());
        add(desktop);
        setJMenuBar(criaMenu());
        iniciaBD();
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

            JPanel mainPanel = new JPanel(new BorderLayout()); // Painel principal
            JPanel leftPanel = new JPanel(new BorderLayout()); // Painel para a imagem à esquerda

            // Ícone à esquerda
            ImageIcon icon = new ImageIcon("./imgs/adiciona_Carro.png");
            JLabel iconLabel = new JLabel(icon);
            leftPanel.add(iconLabel, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5); // Define as margens internas dos componentes

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Campo de texto para Marca
            JLabel marcaLabel = new JLabel("Marca:");
            marcaLabel.setFont(marcaLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField marcaTextField = new JTextField(20);
            marcaTextField.setFont(marcaTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(marcaLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(marcaTextField, constraints);

            // Campo de texto para Modelo
            JLabel modeloLabel = new JLabel("Modelo:");
            modeloLabel.setFont(modeloLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField modeloTextField = new JTextField(20);
            modeloTextField.setFont(modeloTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 2;
            formPanel.add(modeloLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(modeloTextField, constraints);

            // Campo de texto para Cor
            JLabel corLabel = new JLabel("Cor:");
            corLabel.setFont(corLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField corTextField = new JTextField(20);
            corTextField.setFont(corTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 4;
            formPanel.add(corLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(corTextField, constraints);

            // Campo de texto para Preço
            JLabel precoLabel = new JLabel("Preço:");
            precoLabel.setFont(precoLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField precoTextField = new JTextField(20);
            precoTextField.setFont(precoTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 6;
            formPanel.add(precoLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(precoTextField, constraints);

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
                    String marca = marcaTextField.getText().trim();
                    String modelo = modeloTextField.getText().trim();
                    String cor = corTextField.getText().trim();
                    String preco = precoTextField.getText().trim();

                    // Verificações e condições
                    if (marca.length() > 30 || modelo.length() > 30 || cor.length() > 30) {
                        JOptionPane.showMessageDialog(CarrosPanelAdiciona.this,
                                "A marca, modelo e cor devem ter no máximo 30 caracteres.",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (!preco.matches("\\d+(.\\d{1,2})?")) {
                        JOptionPane.showMessageDialog(CarrosPanelAdiciona.this,
                                "O preço deve ser um número válido no formato XX ou XX,XX.",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String insertCarro = "'" + marca + "'" + ", " + "'" + modelo + "'" + ", " + "'" + cor + "'"
                                + ", " + preco;
                        try {
                            Class.forName("org.hsql.jdbcDriver");
                            stmt.executeUpdate(
                                    "INSERT INTO CARRO(MARCA, MODELO, COR, PRECO) VALUES (" + insertCarro + ")");
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        JOptionPane.showMessageDialog(CarrosPanelAdiciona.this,
                                "Carro adicionado com sucesso!\n",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        // Limpar os campos
                        marcaTextField.setText("");
                        modeloTextField.setText("");
                        corTextField.setText("");
                        precoTextField.setText("");
                    }
                }
            });

            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 0));
        }
    }

    // Classe para buscar carros
    class CarrosPanelBusca extends JPanel {
        public CarrosPanelBusca() {
            setLayout(new BorderLayout());

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

            JPanel mainPanel = new JPanel(new BorderLayout()); // Painel principal
            JPanel leftPanel = new JPanel(new BorderLayout()); // Painel para a imagem à esquerda

            // Ícone à esquerda
            ImageIcon icon = new ImageIcon("./imgs/adiciona_Cliente.png");
            JLabel iconLabel = new JLabel(icon);
            leftPanel.add(iconLabel, BorderLayout.CENTER);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5); // Define as margens internas dos componentes

            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Campo de texto para Nome
            JLabel nomeLabel = new JLabel("Nome:");
            nomeLabel.setFont(nomeLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField nomeTextField = new JTextField(20);
            nomeTextField.setFont(nomeTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(nomeLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(nomeTextField, constraints);

            // Campo de texto para Endereço
            JLabel enderecoLabel = new JLabel("Endereço:");
            enderecoLabel.setFont(enderecoLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField enderecoTextField = new JTextField(20);
            enderecoTextField.setFont(enderecoTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da
                                                                                               // fonte
            constraints.gridy = 2;
            formPanel.add(enderecoLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(enderecoTextField, constraints);

            // Campo de texto para Telefone
            JLabel telefoneLabel = new JLabel("Telefone:");
            telefoneLabel.setFont(telefoneLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField telefoneTextField = new JTextField(20);
            telefoneTextField.setFont(telefoneTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da
                                                                                               // fonte
            constraints.gridy = 4;
            formPanel.add(telefoneLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(telefoneTextField, constraints);

            // Campo de texto para CPF
            JLabel cpfLabel = new JLabel("CPF:");
            cpfLabel.setFont(cpfLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            JTextField cpfTextField = new JTextField(20);
            cpfTextField.setFont(cpfTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 6;
            formPanel.add(cpfLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(cpfTextField, constraints);

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
                    String nome = nomeTextField.getText().trim();
                    String endereco = enderecoTextField.getText().trim();
                    String telefone = telefoneTextField.getText().trim();
                    String cpf = cpfTextField.getText().trim();

                    // Verificações e condições
                    if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty() || cpf.isEmpty()) {
                        JOptionPane.showMessageDialog(ClientesPanelAdiciona.this,
                                "Todos os campos devem ser preenchidos.",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (!nome.matches("[A-Za-zÀ-ÖØ-öø-ÿ]+")) {
                        JOptionPane.showMessageDialog(ClientesPanelAdiciona.this,
                                "O nome deve conter apenas letras.",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } else if (!cpf.matches("\\d+")) {
                        JOptionPane.showMessageDialog(ClientesPanelAdiciona.this,
                                "O CPF deve conter apenas números.",
                                "Erro", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Inserir os dados do cliente no banco de dados
                        try {
                            Class.forName("org.hsql.jdbcDriver");
                            stmt.executeUpdate("INSERT INTO CLIENTE(NOME, ENDERECO, TELEFONE, CPF) VALUES ('" + nome
                                    + "', '" + endereco + "', '" + telefone + "', '" + cpf + "')");
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }

                        JOptionPane.showMessageDialog(ClientesPanelAdiciona.this,
                                "Cliente adicionado com sucesso!\n",
                                "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                        // Limpar os campos
                        nomeTextField.setText("");
                        enderecoTextField.setText("");
                        telefoneTextField.setText("");
                        cpfTextField.setText("");
                    }
                }
            });

            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 0));
        }
    }

    // Classe para buscar clientes
    class ClientesPanelBusca extends JPanel {
        public ClientesPanelBusca() {
            setLayout(new BorderLayout());

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
