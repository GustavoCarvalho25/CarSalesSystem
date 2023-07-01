import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Carro {

    private Connection con;
    private Statement stmt;
    public CarrosPanelAdiciona carrosPanelAdiciona;
    public CarrosPanelBusca carrosPanelBusca;
    public CarrosPanelLista carrosPanelLista;

    public Carro(Connection _con, Statement _stmt) {
        con = _con;
        stmt = _stmt;
        carrosPanelAdiciona = new CarrosPanelAdiciona();
        carrosPanelBusca = new CarrosPanelBusca();
        carrosPanelLista = new CarrosPanelLista();
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
}