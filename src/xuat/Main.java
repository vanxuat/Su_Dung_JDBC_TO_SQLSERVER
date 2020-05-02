package xuat;

import com.microsoft.sqlserver.jdbc.SQLServerConnection;

import java.sql.*;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SudungJDBC_OutputPram();

    }

    //cach 1
    static  void ConnectToServer(){
        Statement statement = null;
        Connection con = null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=test;user=SA;password=Password789";
        try {
            con = DriverManager.getConnection(connectionUrl);

            String sql= "INSERT dbo.HocSinh(Ten,CMND)VALUES(N'Tran Van Xuat',234567)";

            statement = con.createStatement();
            statement.executeQuery(sql);
        }catch (Exception e){}
        finally {
            try {
                statement.close();
                con.close();
            }catch (Exception ex){}
        }
    }

    //cach 2

    static  void ConnectServer2(){
        Connection connection =null;
        PreparedStatement preparedStatement = null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=test;user=SA;password=Password789";
        try{
            connection = DriverManager.getConnection(connectionUrl);
            String sql= "INSERT dbo.HocSinh(Ten,CMND)VALUES(?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,"Tran van Moc "); //truyen tham so bat bat bang 1
            preparedStatement.setInt(2,678899);

            preparedStatement.execute();
        }catch (Exception ex){}
        finally {

        }
    }

    //Lay du lieu tu SQl server len

    static  void LoadDataFromSql(){
        Connection connection= null;
        Statement statement = null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=test;user=SA;password=Password789";
        try{
            connection = DriverManager.getConnection(connectionUrl); // tao ket noi toi co so du lieu

            String sql = "SELECT * from HocSinh"; //cau lenh truy van toi Sql
            //tao cau lenh
            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){ // kiem tra xem co next duoc dong tiep theo
                System.out.println(resultSet.getInt("id") + " Ho Ten: " +
                        resultSet.getString("Ten") + " So CMND " + resultSet.getInt("CMND"));
            }


        }catch (Exception ex){}
        finally {
            try {
                connection.close();
                statement.close();
            }catch (Exception ex){

            }

        }

    }

    // su dung jdbc de goi store lay du lieu tu Sql len
    static  void SudungStoreJDBC(){
            CallableStatement cstmt = null;
            double averageWeight = 0;
            ResultSet rs = null;
            Connection connection= null;
            String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                    "databaseName=test;user=SA;password=Password789";
            try {

                connection = DriverManager.getConnection(connectionUrl); // tao ket noi

                cstmt = connection.prepareCall("{call GetHocSinh}"); // tao cau truy van dua ten store vao
                cstmt.execute();
                rs = cstmt.getResultSet(); //lay ket qua tra ve tu store

                while (rs.next()) {
                    System.out.println(rs.getInt("id") + " Ten La " + rs.getString("Ten"));
                }

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
    }


    static  void SudungJDBC_OutputPram(){
        CallableStatement cstmt = null;
        double averageWeight = 0;
        ResultSet rs = null;
        Connection connection= null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=test;user=SA;password=Password789";

        try{
            connection = DriverManager.getConnection(connectionUrl); // tao ket noi
            cstmt = connection.prepareCall("{call dbo.GetSoLuongHocSinh(?)}");
            cstmt.registerOutParameter("@Soluong ", Types.INTEGER);
            cstmt.execute();

            int n = cstmt.getInt("@Soluong");
            System.out.println("So luong la : " + n);


        }catch (SQLException ex){

        }
        finally {

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                cstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    static  void SudungJDBC_HoiStore(){
        //su dung tham so truyen vao
        CallableStatement cstmt = null;
        double averageWeight = 0;
        ResultSet rs = null;
        Connection connection= null;
        String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
                "databaseName=test;user=SA;password=Password789";
        try {
            connection = DriverManager.getConnection(connectionUrl);

            cstmt = connection.prepareCall("{call dbo.ThemHocSinh(?,?)",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            cstmt.setString("Ten", "Xuat");
            cstmt.setInt("CMND",89);
            boolean results = cstmt.execute();
            int rowsAffected = 0;
            while (results || rowsAffected != -1) {
                if (results) {
                    rs = cstmt.getResultSet();
                    break;
                } else {
                    rowsAffected = cstmt.getUpdateCount();
                }
                results = cstmt.getMoreResults();
            }
        }catch (Exception ex){}
        finally {

        }
    }


}
