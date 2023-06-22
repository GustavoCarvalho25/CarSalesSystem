import java.sql.*;

class CriaTabelas {
    public static void main(String[] args) {
        try {
            Class.forName("org.hsql.jdbcDriver");
            Connection con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE CARRO (CAR_ID INTEGER IDENTITY PRIMARY KEY, MARCA VARCHAR(30), MODELO VARCHAR(30), COR VARCHAR(30), PRECO FLOAT)");
            stmt.executeUpdate("CREATE TABLE CLIENTE (CLI_ID INTEGER, NOME VARCHAR(30), ENDERECO VARCHAR(30), TELEFONE VARCHAR(30), CPF VARCHAR(15))");
            stmt.executeUpdate("CREATE TABLE PEDIDO (ID INTEGER, CLI_ID INTEGER, CAR_ID INTEGER, DATA_DA_VENDA DATE, TAXA FLOAT)");
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
