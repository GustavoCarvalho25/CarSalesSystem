import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MDI extends JFrame implements ActionListener {
    
    Connection con;
    Statement stmt;
    
    public MDI() {
        iniciaBD();
        CriaTabelas.CriaTabelas();
             
    }
    
    void iniciaBD() {
        try {
            Class.forName("org.hsql.jdbcDriver");
            con = DriverManager.getConnection("jdbc:HypersonicSQL:http://127.0.0.1", "sa", "");
            stmt = con.createStatement();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "O driver do banco de dados não foi encontrado.\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na iniciação do acesso ao banco de dados\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } 
    }
}

class InsereDados extends JInternalFrame{
    PreparedStatement pStmt;
    JDesktopPane desktop;
    JButton bt1;
    JTextField tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9, tf10, tf11, tf12, tf13, tf14, tf15;

    public InsereDados(JDesktopPane desktopPane, Connection con){
        super("Cadastro de dados", false, true, false, true);
        desktop = desktopPane;
         try {
                String sqlCarro = "INSERT INTO CARRO   (CAR_ID, MARCA, MODELO, COR, PRECO)       VALUES (?, ?, ?, ?, ?)"; 
                String sqlCli   = "INSERT INTO CLIENTE (CLI_ID, NOME, ENDERECO, TELEFONE, CPF)   VALUES (?, ?, ?, ?, ?)";
                String sqlPed   = "INSERT INTO PEDIDO  (ID, CLI_ID, CAR_ID, DATA_DA_VENDA, TAXA) VALUES (?, ?, ?, ?, ?)";

                pStmtCarro = con.prepareStatement(sqlCarro);
                pStmtCli   = con.prepareStatement(sqlCli);
                pStmtPed   = con.prepareStatement(sqlPed);

                //Tabela Carro
                setLayout(new GridLayout(15, 5));
                add (new JLabel("ID do Carro: "));
                add(tf1 = new JTextField(30));
                add (new JLabel("Marca: "));
                add(tf2 = new JTextField(30));
                add (new JLabel("Modelo: "));
                add(tf3 = new JTextField(30));
                add (new JLabel("Cor: "));
                add(tf4 = new JTextField(30));
                add (new JLabel("Preço"));
                add(tf5 = new JTextField(30));
                //Tabela Cliente 
                add (new JLabel("ID do Cliente: "));
                add(tf6 = new JTextField(30));
                add (new JLabel("Nome: "));
                add(tf7 = new JTextField(30));
                add (new JLabel("Endereco: "));
                add(tf8 = new JTextField(30));
                add (new JLabel("Telefone: "));
                add(tf9 = new JTextField(30));
                add (new JLabel("CPF:"));
                add(tf10 = new JTextField(30));
                //Tabela Pedido
                add (new JLabel("ID do Pedido: "));
                add(tf6 = new JTextField(30));
                add (new JLabel("ID do Cliente: "));
                add(tf7 = new JTextField(30));
                add (new JLabel("ID do Carro: "));
                add(tf8 = new JTextField(30));
                add (new JLabel("Data da Venda: "));
                add(tf9 = new JTextField(30));
                add (new JLabel("Taxa:"));
                add(tf10 = new JTextField(30));
                pack();
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
            desktop.add(this);

            bt1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        pStmtCarro.setString(1, tf1.getText());
                        pStmt.setInt(2, Integer.parseInt(tf2.getText()));
                        tf1.setText("");
                        tf2.setText("");
                        pStmt.executeUpdate();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(desktop, "Problema interno.\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(desktop, "Problema interno.\n" + ex, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    
}

