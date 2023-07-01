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
            trocarPainel(new CarrosPanelAdiciona());
        }
        if (e.getSource() == carrosMenuItemBusca) {
            trocarPainel(new CarrosPanelBusca());
        }
        if (e.getSource() == carrosMenuItemLista) {
            trocarPainel(new CarrosPanelLista());
        }
        if (e.getSource() == clientesMenuItemAdiciona) {
            trocarPainel(new ClientesPanelAdiciona());
        }
        if (e.getSource() == clientesMenuItemBusca) {
            trocarPainel(new ClientesPanelBusca());
        }
        if (e.getSource() == clientesMenuItemLista) {
            trocarPainel(new ClientesPanelLista());
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

    // Classe para adicionar carros
    class CarrosPanelAdiciona extends JPanel {
        private JTextField marcaTextField;
        private JTextField modeloTextField;
        private JTextField corTextField;
        private JTextField precoTextField;

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
            marcaTextField = new JTextField(20);
            marcaTextField.setFont(marcaTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(marcaLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(marcaTextField, constraints);

            // Campo de texto para Modelo
            JLabel modeloLabel = new JLabel("Modelo:");
            modeloLabel.setFont(modeloLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            modeloTextField = new JTextField(20);
            modeloTextField.setFont(modeloTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 2;
            formPanel.add(modeloLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(modeloTextField, constraints);

            // Campo de texto para Cor
            JLabel corLabel = new JLabel("Cor:");
            corLabel.setFont(corLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            corTextField = new JTextField(20);
            corTextField.setFont(corTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridy = 4;
            formPanel.add(corLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(corTextField, constraints);

            // Campo de texto para Preço
            JLabel precoLabel = new JLabel("Preço:");
            precoLabel.setFont(precoLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            precoTextField = new JTextField(20);
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
                    adicionarButtonActionPerformed(e);
                }
            });

            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 0));
        }

        private void adicionarButtonActionPerformed(ActionEvent e) {
            String marca = marcaTextField.getText().trim();
            String modelo = modeloTextField.getText().trim();
            String cor = corTextField.getText().trim();
            String preco = precoTextField.getText().trim();

            // Verificações e condições
            if (marca.length() > 30 || modelo.length() > 30 || cor.length() > 30) {
                JOptionPane.showMessageDialog(this,
                        "A marca, modelo e cor devem ter no máximo 30 caracteres.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!preco.matches("\\d+(.\\d{1,2})?")) {
                JOptionPane.showMessageDialog(this,
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
                JOptionPane.showMessageDialog(this,
                        "Carro adicionado com sucesso!\n",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Limpar os campos
                marcaTextField.setText("");
                modeloTextField.setText("");
                corTextField.setText("");
                precoTextField.setText("");
            }
        }
    }

    // Classe para buscar carros
    public class CarrosPanelBusca extends JPanel {
        private JTextField idBuscaTextField;
        private JTextField marcaTextField;
        private JTextField modeloTextField;
        private JTextField precoTextField;
        private JTextField corTextField;

        public CarrosPanelBusca() {
            setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout());
            JPanel formPanel = new JPanel(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5);

            // Campo de busca por ID
            JLabel idBuscaLabel = new JLabel("ID de busca:");
            idBuscaLabel.setFont(idBuscaLabel.getFont().deriveFont(Font.BOLD, 16));
            idBuscaTextField = new JTextField(10);
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(idBuscaLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(idBuscaTextField, constraints);

            // Botão "Buscar"
            JButton buscarButton = new JButton("Buscar");
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.insets = new Insets(5, 10, 5, 5);
            formPanel.add(buscarButton, constraints);

            // Campos de texto para exibir os dados do carro
            JLabel marcaLabel = new JLabel("Marca:");
            marcaLabel.setFont(marcaLabel.getFont().deriveFont(Font.BOLD, 16));
            marcaTextField = new JTextField(20);
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.insets = new Insets(10, 5, 5, 5);
            formPanel.add(marcaLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(marcaTextField, constraints);

            JLabel modeloLabel = new JLabel("Modelo:");
            modeloLabel.setFont(modeloLabel.getFont().deriveFont(Font.BOLD, 16));
            modeloTextField = new JTextField(20);
            constraints.gridy = 4;
            formPanel.add(modeloLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(modeloTextField, constraints);

            JLabel precoLabel = new JLabel("Preço:");
            precoLabel.setFont(precoLabel.getFont().deriveFont(Font.BOLD, 16));
            precoTextField = new JTextField(20);
            constraints.gridy = 6;
            formPanel.add(precoLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(precoTextField, constraints);

            JLabel corLabel = new JLabel("Cor:");
            corLabel.setFont(corLabel.getFont().deriveFont(Font.BOLD, 16));
            corTextField = new JTextField(20);
            constraints.gridy = 8;
            formPanel.add(corLabel, constraints);
            constraints.gridy = 9;
            formPanel.add(corTextField, constraints);

            // Botões "Excluir" e "Alterar"
            JButton excluirButton = new JButton("Excluir");
            JButton alterarButton = new JButton("Alterar");
            constraints.gridx = 0;
            constraints.gridy = 10;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(20, 0, 0, 0);
            formPanel.add(excluirButton, constraints);
            constraints.gridy = 11;
            formPanel.add(alterarButton, constraints);

            mainPanel.add(formPanel, BorderLayout.CENTER);
            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 100));

            buscarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buscarButtonActionPerformed(e);
                }
            });

            excluirButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    excluirButtonActionPerformed(e);
                }
            });

            alterarButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alterarButtonActionPerformed(e);
                }
            });
        }

        private void buscarButtonActionPerformed(ActionEvent e) {
            String idBusca = idBuscaTextField.getText().trim();

            // Verificação e condição
            if (idBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "O campo de busca por ID está vazio.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!idBusca.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "O ID de busca deve conter apenas números.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    int carId = Integer.parseInt(idBusca);

                    // Realizar a busca no banco de dados
                    ResultSet rs = stmt.executeQuery("SELECT * FROM CARRO WHERE CAR_ID = " + carId);

                    if (rs.next()) {
                        // Preencher os campos de texto com os dados do carro
                        String marca = rs.getString("MARCA");
                        String modelo = rs.getString("MODELO");
                        float preco = rs.getFloat("PRECO");
                        String cor = rs.getString("COR");

                        marcaTextField.setText(marca);
                        modeloTextField.setText(modelo);
                        precoTextField.setText(String.valueOf(preco));
                        corTextField.setText(cor);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Nenhum carro encontrado com o ID informado.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        marcaTextField.setText("");
                        modeloTextField.setText("");
                        precoTextField.setText("");
                        corTextField.setText("");
                    }

                    rs.close();
                } catch (Exception ex) {
                    System.out.println("Erro ocorreu em CarrosPanelBusca: " + ex);
                }
            }
        }

        private void excluirButtonActionPerformed(ActionEvent e) {
            int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o carro?", "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Obtenha o ID do carro a ser excluído
                String idBusca = idBuscaTextField.getText().trim();
                if (idBusca.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "O campo de busca por ID está vazio.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!idBusca.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "O ID de busca deve conter apenas números.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int carId = Integer.parseInt(idBusca);

                try {
                    // Realize a exclusão do carro no banco de dados
                    Class.forName("org.hsql.jdbcDriver");

                    // Execute o comando SQL para excluir o carro
                    String sql = "DELETE FROM CARRO WHERE CAR_ID = " + carId;
                    int rowsAffected = stmt.executeUpdate(sql);

                    if (rowsAffected > 0) {
                        // Limpe os campos de texto
                        marcaTextField.setText("");
                        modeloTextField.setText("");
                        precoTextField.setText("");
                        corTextField.setText("");

                        JOptionPane.showMessageDialog(this, "Carro excluído com sucesso!", "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nenhum carro encontrado com o ID informado.", "Aviso",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o carro: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void alterarButtonActionPerformed(ActionEvent e) {
            // Obtenha o ID do carro a ser alterado
            String idBusca = idBuscaTextField.getText().trim();
            if (idBusca.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O campo de busca por ID está vazio.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!idBusca.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "O ID de busca deve conter apenas números.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int carId = Integer.parseInt(idBusca);

            // Obtenha os novos dados do carro dos campos de texto
            String marca = marcaTextField.getText().trim();
            String modelo = modeloTextField.getText().trim();
            String precoStr = precoTextField.getText().trim();
            String cor = corTextField.getText().trim();

            if (marca.isEmpty() || modelo.isEmpty() || precoStr.isEmpty() || cor.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                float preco = Float.parseFloat(precoStr);

                // Realize a alteração do carro no banco de dados
                Class.forName("org.hsql.jdbcDriver");

                // Execute o comando SQL para alterar o carro
                String sql = "UPDATE CARRO SET MARCA = '" + marca + "', MODELO = '" + modelo + "', PRECO = " + preco
                        + ", COR = '" + cor + "' WHERE CAR_ID = " + carId;
                int rowsAffected = stmt.executeUpdate(sql);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Carro alterado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum carro encontrado com o ID informado.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "O preço deve ser um valor numérico.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao alterar o carro: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
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
        private JTextField nomeTextField;
        private JTextField enderecoTextField;
        private JTextField telefoneTextField;
        private JTextField cpfTextField;

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
            nomeTextField = new JTextField(20);
            nomeTextField.setFont(nomeTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da fonte
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(nomeLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(nomeTextField, constraints);

            // Campo de texto para Endereço
            JLabel enderecoLabel = new JLabel("Endereço:");
            enderecoLabel.setFont(enderecoLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            enderecoTextField = new JTextField(20);
            enderecoTextField.setFont(enderecoTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da
                                                                                               // fonte
            constraints.gridy = 2;
            formPanel.add(enderecoLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(enderecoTextField, constraints);

            // Campo de texto para Telefone
            JLabel telefoneLabel = new JLabel("Telefone:");
            telefoneLabel.setFont(telefoneLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            telefoneTextField = new JTextField(20);
            telefoneTextField.setFont(telefoneTextField.getFont().deriveFont(Font.PLAIN, 20)); // Ajusta o tamanho da
                                                                                               // fonte
            constraints.gridy = 4;
            formPanel.add(telefoneLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(telefoneTextField, constraints);

            // Campo de texto para CPF
            JLabel cpfLabel = new JLabel("CPF:");
            cpfLabel.setFont(cpfLabel.getFont().deriveFont(Font.BOLD, 20)); // Ajusta o tamanho da fonte
            cpfTextField = new JTextField(20);
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
                    adicionarButtonActionPerformed(e);
                }
            });

            mainPanel.add(leftPanel, BorderLayout.WEST);
            mainPanel.add(formPanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 100, 10, 0));
        }

        private void adicionarButtonActionPerformed(ActionEvent e) {
            String nome = nomeTextField.getText().trim();
            String endereco = enderecoTextField.getText().trim();
            String telefone = telefoneTextField.getText().trim();
            String cpf = cpfTextField.getText().trim();

            // Verificações e condições
            if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Todos os campos devem ser preenchidos.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!nome.matches("[A-Za-zÀ-ÖØ-öø-ÿ]+")) {
                JOptionPane.showMessageDialog(this,
                        "O nome deve conter apenas letras.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            } else if (!cpf.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
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

                JOptionPane.showMessageDialog(this,
                        "Cliente adicionado com sucesso!\n",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Limpar os campos
                nomeTextField.setText("");
                enderecoTextField.setText("");
                telefoneTextField.setText("");
                cpfTextField.setText("");
            }
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
