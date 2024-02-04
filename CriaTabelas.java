import java.sql.*;

public class CriaTabelas {

    public static void CriaTabelasSistema() {
        try {
            Class.forName("org.hsql.jdbcDriver");
            Connection con = DriverManager.getConnection("jdbc:HypersonicSQL:hsql://localhost:8080", "sa", "");
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE CARRO (CAR_ID INTEGER IDENTITY PRIMARY KEY, MARCA VARCHAR(30), MODELO VARCHAR(30), COR VARCHAR(30), PRECO FLOAT)");
            stmt.executeUpdate("CREATE TABLE CLIENTE (CLI_ID INTEGER IDENTITY PRIMARY KEY, NOME VARCHAR(30), ENDERECO VARCHAR(30), TELEFONE VARCHAR(30), CPF VARCHAR(15))");
            stmt.executeUpdate("CREATE TABLE PEDIDO (PED_ID INTEGER IDENTITY PRIMARY KEY, CLI_ID INTEGER, CAR_ID INTEGER, DATAVENDA DATE, TAXA FLOAT)");
            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println("Erro deu em CriaTabelas" + e);
        }
    }
}
