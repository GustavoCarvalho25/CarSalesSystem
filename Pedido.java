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
    public class PedidosPanelBusca extends JPanel {
        private JTextField idBuscaTextField;
        private JTextField idClienteTextField;
        private JTextField idCarroTextField;
        private JTextField dataVendaTextField;
        private JTextField taxaTextField;
        private JLabel imageLabel;

        public PedidosPanelBusca() {
            setLayout(new BorderLayout());

            // Painel principal
            JPanel mainPanel = new JPanel(new BorderLayout());

            // Painel para o formulário de pesquisa
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 0));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(5, 5, 5, 5);

            // Campo de busca por ID
            JLabel idBuscaLabel = new JLabel("ID de busca:");
            idBuscaLabel.setFont(idBuscaLabel.getFont().deriveFont(Font.BOLD, 20));
            idBuscaTextField = new JTextField(20);
            idBuscaTextField.setFont(idBuscaTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridx = 0;
            constraints.gridy = 0;
            formPanel.add(idBuscaLabel, constraints);
            constraints.gridy = 1;
            formPanel.add(idBuscaTextField, constraints);

            // Campos de texto para exibir os dados do pedido
            JLabel idClienteLabel = new JLabel("ID Cliente:");
            idClienteLabel.setFont(idClienteLabel.getFont().deriveFont(Font.BOLD, 20));
            idClienteTextField = new JTextField(20);
            idClienteTextField.setFont(idClienteTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridx = 0;
            constraints.gridy = 2;
            formPanel.add(idClienteLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(idClienteTextField, constraints);

            JLabel idCarroLabel = new JLabel("ID Carro:");
            idCarroLabel.setFont(idCarroLabel.getFont().deriveFont(Font.BOLD, 20));
            idCarroTextField = new JTextField(20);
            idCarroTextField.setFont(idCarroTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 4;
            formPanel.add(idCarroLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(idCarroTextField, constraints);

            JLabel dataVendaLabel = new JLabel("Data Venda:");
            dataVendaLabel.setFont(dataVendaLabel.getFont().deriveFont(Font.BOLD, 20));
            dataVendaTextField = new JTextField(20);
            dataVendaTextField.setFont(dataVendaTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 6;
            formPanel.add(dataVendaLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(dataVendaTextField, constraints);

            JLabel taxaLabel = new JLabel("Taxa:");
            taxaLabel.setFont(taxaLabel.getFont().deriveFont(Font.BOLD, 20));
            taxaTextField = new JTextField(20);
            taxaTextField.setFont(taxaTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 8;
            formPanel.add(taxaLabel, constraints);
            constraints.gridy = 9;
            formPanel.add(taxaTextField, constraints);

            // Painel para exibir a imagem
            JPanel imagePanel = new JPanel(new BorderLayout());
            imagePanel.setBorder(BorderFactory.createEmptyBorder(5, 100, 5, 20));
            imageLabel = new JLabel();
            imagePanel.add(imageLabel, BorderLayout.CENTER);

            // Adicionar painéis ao painel principal
            mainPanel.add(formPanel, BorderLayout.WEST);
            mainPanel.add(imagePanel, BorderLayout.CENTER);

            add(mainPanel, BorderLayout.CENTER);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Carregar a imagem da pasta "imgs" e definir no JLabel
            ImageIcon imageIcon = new ImageIcon("./imgs/busca_Pedido.png");
            Image image = imageIcon.getImage().getScaledInstance(512, 512, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(image);
            imageLabel.setIcon(imageIcon);

            // Botões "Excluir", "Alterar" e "Buscar"
            JButton excluirButton = new JButton("Excluir");
            excluirButton.setFont(excluirButton.getFont().deriveFont(Font.BOLD, 20));
            JButton alterarButton = new JButton("Alterar");
            alterarButton.setFont(alterarButton.getFont().deriveFont(Font.BOLD, 20));
            JButton buscarButton = new JButton("Buscar");
            buscarButton.setFont(buscarButton.getFont().deriveFont(Font.BOLD, 20));

            // Painel para os botões
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
            buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

            // Adiciona um espaçamento entre os botões
            buttonsPanel.add(Box.createHorizontalStrut(10));
            buttonsPanel.add(excluirButton);
            buttonsPanel.add(Box.createHorizontalStrut(10));
            buttonsPanel.add(alterarButton);
            buttonsPanel.add(Box.createHorizontalStrut(10));
            buttonsPanel.add(buscarButton);

            constraints.gridx = 0;
            constraints.gridy = 10;
            constraints.gridwidth = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            constraints.insets = new Insets(20, 0, 0, 0);
            formPanel.add(buttonsPanel, constraints);

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

            int pedidoId = Integer.parseInt(idBusca);

            try {
                // Realizar a busca no banco de dados
                ResultSet rs = stmt.executeQuery("SELECT * FROM PEDIDO WHERE PED_ID = " + pedidoId);

                if (rs.next()) {
                    // Preencher os campos de texto com os dados do pedido
                    int clienteId = rs.getInt("CLI_ID");
                    int carroId = rs.getInt("CAR_ID");
                    java.sql.Date dataVenda = rs.getDate("DATAVENDA");
                    double taxa = rs.getDouble("TAXA");

                    idClienteTextField.setText(String.valueOf(clienteId));
                    idCarroTextField.setText(String.valueOf(carroId));

                    // Formatação da data para exibição no formato DD/MM/YYYY
                    DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDataVenda = outputFormat.format(dataVenda);
                    dataVendaTextField.setText(formattedDataVenda);

                    taxaTextField.setText(String.valueOf(taxa));
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum pedido encontrado com o ID informado.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                    limparCampos();
                }

                rs.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ocorreu em BuscaPedidoPanel: " + ex, "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void excluirButtonActionPerformed(ActionEvent e) {
            int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o pedido?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
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
                int pedidoId = Integer.parseInt(idBusca);

                try {
                    // Realize a exclusão do pedido no banco de dados
                    Class.forName("org.hsql.jdbcDriver");

                    // Execute o comando SQL para excluir o pedido
                    String sql = "DELETE FROM PEDIDO WHERE PED_ID = " + pedidoId;
                    int rowsAffected = stmt.executeUpdate(sql);

                    if (rowsAffected > 0) {
                        limparCampos();

                        JOptionPane.showMessageDialog(this, "Pedido excluído com sucesso!", "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nenhum pedido encontrado com o ID informado.", "Aviso",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o pedido: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void alterarButtonActionPerformed(ActionEvent e) {
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
            int pedidoId = Integer.parseInt(idBusca);

            int clienteId = Integer.parseInt(idClienteTextField.getText().trim());
            int carroId = Integer.parseInt(idCarroTextField.getText().trim());
            String dataVenda = dataVendaTextField.getText().trim();
            double taxa = Double.parseDouble(taxaTextField.getText().trim());

            try {
                // Conversão da data para o formato "YYYY-MM-DD"
                DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                java.util.Date utilDate = inputFormat.parse(dataVenda);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                // Realize a alteração do pedido no banco de dados
                Class.forName("org.hsql.jdbcDriver");

                // Execute o comando SQL para alterar o pedido
                String sql = "UPDATE PEDIDO SET CLI_ID = " + clienteId + ", CAR_ID = " + carroId +
                        ", DATAVENDA = '" + sqlDate + "', TAXA = " + taxa +
                        " WHERE PED_ID = " + pedidoId;
                int rowsAffected = stmt.executeUpdate(sql);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Pedido alterado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum pedido encontrado com o ID informado.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao alterar o pedido: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        private void limparCampos() {
            idBuscaTextField.setText("");
            idClienteTextField.setText("");
            idCarroTextField.setText("");
            dataVendaTextField.setText("");
            taxaTextField.setText("");
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
