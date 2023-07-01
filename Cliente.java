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

}
