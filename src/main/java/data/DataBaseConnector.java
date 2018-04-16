package data;

import java.sql.*;

public class DataBaseConnector {

    private String dbURL = "jdbc:sqlite:./DataBase/BacteriaBase";
    private String user = "user";
    private String password = "paslo";
    private Connection conect = null;

    public ResultSet getSorterFlagella() throws SQLException {
        Statement stmt = conect.createStatement();


        String sql = "SELECT * FROM main.FLAGELLA ORDER BY ALPHA";
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }

    public void startTransaction() throws SQLException {
        conect.setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        conect.commit();
        conect.setAutoCommit(true);
    }

    public void rollbackTransaction() throws SQLException {
        conect.rollback();
        conect.setAutoCommit(true);
    }

    public ResultSet getSorterToughness() throws SQLException {
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.TOUGHNESS ORDER BY BETA";
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }

    public DataBaseConnector(String path, String login, String password){
        try {
            Class.forName("org.sqlite.JDBC");
            conect = DriverManager.getConnection(path,login,password);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(" Connected Fail ");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println(" Connected Fail ");
        } finally {
            System.out.println(" Connected ");
        }
    }

    public void addExaminedBacteria(Bacteria bacteria) throws SQLException {
        System.out.println("Inserting records into the table...");

        String sql;
        PreparedStatement stmt;

        sql = "INSERT OR REPLACE INTO main.EXAMINED(CLASS, GENOTYPE) " +
                "VALUES (  ? ,  ?  ) ";
        stmt = conect.prepareStatement(sql);
        stmt.setString(1, bacteria.getgClass());
        stmt.setString(2, bacteria.getGenotype());
        stmt.executeUpdate();

        sql = "INSERT OR REPLACE INTO main.FLAGELLA " +
                "VALUES ( ?, ?, ?, ?) ";
        stmt = conect.prepareStatement(sql);
        stmt.setString(1, bacteria.getGenotype());
        stmt.setString(2, bacteria.getNumber()+"" );
        stmt.setString(3, String.valueOf(bacteria.getAlpha()));
        stmt.setString(4, String.valueOf(bacteria.getBeta()));
        stmt.executeUpdate();

        sql = "INSERT OR REPLACE INTO main.TOUGHNESS " +
                "VALUES ( ?, ?, ?, ?) ";
        stmt = conect.prepareStatement(sql);
        stmt.setString(1, bacteria.getGenotype());
        stmt.setString(2, bacteria.getRank()+"" );
        stmt.setString(3, String.valueOf(bacteria.getBeta()));
        stmt.setString(4, String.valueOf(bacteria.getGamma()));
        stmt.executeUpdate();

    }

    public String getFlagellaNumber(int ID) throws SQLException {
        Statement stmt = conect.createStatement();

        String sql = "SELECT NUMBER FROM main.FLAGELLA WHERE Bav";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getString("NUMBER");
    }

    public void addExaminedBacteria(String cGlass, String genotype) throws SQLException {
        System.out.println("Inserting records into the table...");
        Statement stmt = conect.createStatement();

        String sql = "INSERT INTO main.EXAMINED " +
                "VALUES ( \'"+ cGlass +"\' ,\'"+ genotype +"\')";

        stmt.executeUpdate(sql);
    }

    public ResultSet getExamined() throws SQLException {
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.EXAMINED";
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }

    public ResultSet getToughness() throws SQLException {
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.TOUGHNESS";
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }

    public ResultSet getFlagella() throws SQLException {
        System.out.println("Selecting records from the table EXAMINED");
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.FLAGELLA";
        ResultSet rs = stmt.executeQuery(sql);
        return  rs;
    }
    public void showExamined() throws SQLException {
        System.out.println("Selecting records from the table EXAMINED");
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.EXAMINED";
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            //Retrieve by column name
            String gClass  = rs.getString("CLASS");
            String genotype = rs.getString("GENOTYPE");

            //Display values
            System.out.print("class: " + gClass);
            System.out.print(", genotype: " + genotype);
            System.out.print("\n");
        }
    }

    public void showFlagella() throws SQLException {
        System.out.println("Selecting records from the table FLAGELLA");
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.FLAGELLA";
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            //Retrieve by column name
            String number  = rs.getString("NUMBER");
            String alpha = rs.getString("ALPHA");
            String beta = rs.getString("BETA");

            //Display values
            System.out.print("number: " + number);
            System.out.print(", alpha: " + alpha);
            System.out.print(", beta: " + beta);
            System.out.print("\n");
        }
    }

    public void shoTougchness() throws SQLException {
        System.out.println("Selecting records from the table TOUGHNESS");
        Statement stmt = conect.createStatement();

        String sql = "SELECT * FROM main.TOUGHNESS";
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            //Retrieve by column name
            String rank  = rs.getString("RANK");
            String beta = rs.getString("BETA");
            String gamma = rs.getString("GAMMA");

            //Display values
            System.out.print("rank: " + rank);
            System.out.print(", beta: " + beta);
            System.out.print(", gamma: " + gamma);
            System.out.print("\n");
        }
    }

    public void showAll() throws SQLException {
        this.showExamined();
        this.shoTougchness();
        this.showFlagella();
    }

    public void close(){
        try {
            conect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomBase(int size){

        int tmpGen;
        int tmpClass;
        char tmpChar;
        Bacteria tmpBacteria;

        for (int i = 0 ; i < size ; i++){

            tmpGen = (int)(Math.random() * 899999 ) + 100000 ;
            tmpClass = (int)(Math.random() * 25) +  97;
            tmpChar = (char)tmpClass;
            tmpClass = (int)(Math.random() * 9);

            tmpBacteria = new Bacteria(String.valueOf(tmpGen), String.valueOf(tmpClass)+tmpChar);
            try {
                this.addExaminedBacteria(tmpBacteria);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}