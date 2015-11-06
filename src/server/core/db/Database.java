package server.core.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import braynstorm.commonlib.Logger;
import braynstorm.commonlib.math.Vector3f;
import server.core.Config;
import server.core.Main;
import server.game.Account;
import server.game.GameCharacter;
import server.game.entities.EntityLiving;
import server.game.items.ItemStack;

public class Database {
	
    private Connection connection;
    
    private PreparedStatement account_fetch;
    private PreparedStatement account_characters_fetch;
    private PreparedStatement character_fetch_inventory;

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
        
        character_fetch_inventory = connection.prepareStatement("SELECT * FROM character_inventory WHERE characterID=? AND slotID<=? AND slotID>=?");
    }
    
    public boolean accountExists(String email) throws SQLException{
        account_fetch.clearParameters();
        account_fetch.setString(1, email);
        ResultSet rs = account_fetch.executeQuery();
        
        return rs.first();
    }
    
    public List<ShellCharacter> fetchCharactersList(int accountID) throws SQLException{
        account_characters_fetch.clearParameters();
        account_characters_fetch.setInt(1, accountID);
        ResultSet rs = account_characters_fetch.executeQuery();
        
        
        List<ShellCharacter> characters = new ArrayList<ShellCharacter>(rs.getFetchSize());
        Map<Integer, ItemStack> equipment;
        
        while(rs.next()){
        	equipment = new HashMap<Integer, ItemStack>(EntityLiving.EQUIPMENT_SLOTS_COUNT);
        	character_fetch_inventory.setInt(1, rs.getInt(1)); // Character ID
        	character_fetch_inventory.setInt(2, 0); // From slotID (inclusive)
        	character_fetch_inventory.setInt(3, EntityLiving.EQUIPMENT_SLOTS_COUNT); // To slotID (inclusive)
        	
        	
        	ResultSet items = character_fetch_inventory.executeQuery();
        	while(items.next()){
        		/*
        		 * SQL: DB ItemStack,
        		 * 		itemid,
        		 * 		amount,
        		 * 		enchantmentID_perm
        		 */
        		ItemStack itemStack = new ItemStack(items.getInt("itemID"));
        		equipment.put(items.getInt("slotID"), itemStack);
        	}
        	
            characters.add(
                    new ShellCharacter(
                            rs.getString("name"),
                            Main.getWorld().getZone(rs.getInt("currentZone")).toString(),
                            rs.getInt("raceData"),
                            equipment
                    )
            );
        }
        
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
