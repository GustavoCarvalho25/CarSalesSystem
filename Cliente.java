import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Cliente {

    private Connection con;
    private Statement stmt;
    public ClientesPanelAdiciona clientesPanelAdiciona;
    public ClientesPanelBusca clientesPanelBusca;
    public ClientesPanelLista clientesPanelLista;

    public Cliente(Connection _con, Statement _stmt) {
        con = _con;
        stmt = _stmt;
        clientesPanelAdiciona = new ClientesPanelAdiciona();
        clientesPanelBusca = new ClientesPanelBusca();
        clientesPanelLista = new ClientesPanelLista();
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
    public class ClientesPanelBusca extends JPanel {
        private JTextField idBuscaTextField;
        private JTextField nomeTextField;
        private JTextField enderecoTextField;
        private JTextField telefoneTextField;
        private JTextField cpfTextField;
        private JLabel imageLabel;

        public ClientesPanelBusca() {
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

            // Campos de texto para exibir os dados do cliente
            JLabel nomeLabel = new JLabel("Nome:");
            nomeLabel.setFont(nomeLabel.getFont().deriveFont(Font.BOLD, 20));
            nomeTextField = new JTextField(20);
            nomeTextField.setFont(nomeTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridx = 0;
            constraints.gridy = 2;
            formPanel.add(nomeLabel, constraints);
            constraints.gridy = 3;
            formPanel.add(nomeTextField, constraints);

            JLabel enderecoLabel = new JLabel("Endereço:");
            enderecoLabel.setFont(enderecoLabel.getFont().deriveFont(Font.BOLD, 20));
            enderecoTextField = new JTextField(20);
            enderecoTextField.setFont(enderecoTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 4;
            formPanel.add(enderecoLabel, constraints);
            constraints.gridy = 5;
            formPanel.add(enderecoTextField, constraints);

            JLabel telefoneLabel = new JLabel("Telefone:");
            telefoneLabel.setFont(telefoneLabel.getFont().deriveFont(Font.BOLD, 20));
            telefoneTextField = new JTextField(20);
            telefoneTextField.setFont(telefoneTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 6;
            formPanel.add(telefoneLabel, constraints);
            constraints.gridy = 7;
            formPanel.add(telefoneTextField, constraints);

            JLabel cpfLabel = new JLabel("CPF:");
            cpfLabel.setFont(cpfLabel.getFont().deriveFont(Font.BOLD, 20));
            cpfTextField = new JTextField(20);
            cpfTextField.setFont(cpfTextField.getFont().deriveFont(Font.PLAIN, 20));
            constraints.gridy = 8;
            formPanel.add(cpfLabel, constraints);
            constraints.gridy = 9;
            formPanel.add(cpfTextField, constraints);

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
            ImageIcon imageIcon = new ImageIcon("./imgs/busca_Cliente.png");
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
                    int clienteId = Integer.parseInt(idBusca);

                    // Realizar a busca no banco de dados
                    ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE WHERE CLI_ID = " + clienteId);

                    if (rs.next()) {
                        // Preencher os campos de texto com os dados do cliente
                        String nome = rs.getString("NOME");
                        String endereco = rs.getString("ENDERECO");
                        String telefone = rs.getString("TELEFONE");
                        String cpf = rs.getString("CPF");

                        nomeTextField.setText(nome);
                        enderecoTextField.setText(endereco);
                        telefoneTextField.setText(telefone);
                        cpfTextField.setText(cpf);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Nenhum cliente encontrado com o ID informado.",
                                "Aviso", JOptionPane.WARNING_MESSAGE);
                        nomeTextField.setText("");
                        enderecoTextField.setText("");
                        telefoneTextField.setText("");
                        cpfTextField.setText("");
                    }

                    rs.close();
                } catch (Exception ex) {
                    System.out.println("Erro ocorreu em CarrosPanelBusca: " + ex);
                }
            }
        }

        private void excluirButtonActionPerformed(ActionEvent e) {
            int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o cliente?",
                    "Confirmação",
                    JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // Obtenha o ID do cliente a ser excluído
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
                int clienteId = Integer.parseInt(idBusca);

                try {
                    // Realize a exclusão do cliente no banco de dados
                    Class.forName("org.hsql.jdbcDriver");

                    // Execute o comando SQL para excluir o cliente
                    String sql = "DELETE FROM CLIENTE WHERE CLI_ID = " + clienteId;
                    int rowsAffected = stmt.executeUpdate(sql);

                    if (rowsAffected > 0) {
                        // Limpe os campos de texto
                        nomeTextField.setText("");
                        enderecoTextField.setText("");
                        telefoneTextField.setText("");
                        cpfTextField.setText("");

                        JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!", "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado com o ID informado.", "Aviso",
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir o cliente: " + ex.getMessage(), "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void alterarButtonActionPerformed(ActionEvent e) {
            // Obtenha o ID do cliente a ser alterado
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
            int clienteId = Integer.parseInt(idBusca);

            // Obtenha os novos dados do cliente dos campos de texto
            String nome = nomeTextField.getText().trim();
            String endereco = enderecoTextField.getText().trim();
            String telefone = telefoneTextField.getText().trim();
            String cpf = cpfTextField.getText().trim();

            if (nome.isEmpty() || endereco.isEmpty() || telefone.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Realize a alteração do cliente no banco de dados
                Class.forName("org.hsql.jdbcDriver");

                // Execute o comando SQL para alterar o cliente
                String sql = "UPDATE CLIENTE SET NOME = '" + nome + "', ENDERECO = '" + endereco + "', TELEFONE = '"
                        + telefone
                        + "', CPF = '" + cpf + "' WHERE CLI_ID = " + clienteId;
                int rowsAffected = stmt.executeUpdate(sql);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Cliente alterado com sucesso!", "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Nenhum cliente encontrado com o ID informado.", "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao alterar o cliente: " + ex.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
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

}
