import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Pedido {

    private Connection con;
    private Statement stmt;
    public PedidosPanelAdiciona pedidosPanelAdiciona;
    public PedidosPanelBusca pedidosPanelBusca;
    public PedidosPanelLista pedidosPanelLista;

    public Pedido(Connection _con, Statement _stmt) {
        con = _con;
        stmt = _stmt;
        pedidosPanelAdiciona = new PedidosPanelAdiciona();
        pedidosPanelBusca = new PedidosPanelBusca();
        pedidosPanelLista = new PedidosPanelLista();
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

}
