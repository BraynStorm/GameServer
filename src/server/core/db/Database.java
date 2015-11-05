package server.core.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import braynstorm.commonlib.Logger;
import braynstorm.commonlib.math.Vector3f;
import server.core.Config;
import server.core.Main;
import server.game.Account;
import server.game.GameCharacter;

public class Database {
	
    private Connection connection;
    
    private PreparedStatement account_fetch;
    private PreparedStatement account_characters_fetch;
    //private PreparedStatement account_f

    public Database() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("serveruser");
        dataSource.setPassword("serveruser");
        dataSource.setServerName("localhost");
        dataSource.setDatabaseName(Config.getValueS("databaseName"));
        try {
            connection = dataSource.getConnection();
            
            initStatements();
        } catch (SQLException e) {
            Logger.logExceptionImpossibru(e);
        }
    }
    
    public void close(){
        try {
            account_fetch.close();
            
            connection.close();
        } catch (SQLException e) {
            Logger.logExceptionImpossibru(e);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    private void initStatements() throws SQLException{
        account_fetch = connection.prepareStatement("SELECT * FROM accounts WHERE email=?");
        account_fetch.setFetchSize(1);
        
        account_characters_fetch = connection.prepareStatement("SELECT * FROM characters WHERE accountID=?");
    }
    
    public boolean accountExists(String email) throws SQLException{
        account_fetch.clearParameters();
        account_fetch.setString(1, email);
        ResultSet rs = account_fetch.executeQuery();
        
        return rs.first();
    }
    
    public List<GameCharacter> fetchCharactersList(int accountID) throws SQLException{
        account_characters_fetch.clearParameters();
        account_characters_fetch.setInt(1, accountID);
        ResultSet rs = account_characters_fetch.executeQuery();
        
        
        List<GameCharacter> characters = new ArrayList<GameCharacter>(rs.getFetchSize());
        
        while(rs.next())
            characters.add(
                    new GameCharacter(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("raceData"),
                            Main.getWorld().getZone(rs.getInt("currentZone")),
                            new Vector3f(
                                    rs.getFloat("currentPositionX"),
                                    rs.getFloat("currentPositionY"),
                                    rs.getFloat("currentPositionZ")
                            )
                    )
            );
        
        
        return characters;
    }
    
    public Account fetchAccount(String email, String password) throws WrongPasswordException, AccountDoesntExistException, AccountIsSuspendedException, SQLException{
        account_fetch.setString(1, email);
        ResultSet rs = account_fetch.executeQuery();
        
        if(!rs.next())
            throw new AccountDoesntExistException(); 
        
        String passFromDB = rs.getString("password");
        
        if(!password.equals(passFromDB))
            throw new WrongPasswordException();
        
        Date suspendedUntil = rs.getDate("suspendedUntil");
        
        if(suspendedUntil.after(new Date(System.currentTimeMillis())))
            throw new AccountIsSuspendedException(suspendedUntil);
        
        int id = rs.getInt(1);
        Date registeredOn = rs.getDate("registeredOn");
        
        
        
        return new Account(id, fetchCharactersList(id), registeredOn, suspendedUntil);
    }
}
