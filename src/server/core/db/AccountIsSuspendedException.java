package server.core.db;

import java.sql.Date;

public class AccountIsSuspendedException extends Exception {
    private static final long serialVersionUID = -2546732297664309611L;
    
    private Date suspendedUntil;
    
    public AccountIsSuspendedException(Date suspendedUntil) {
        this.suspendedUntil = suspendedUntil;
    }

    public Date getSuspendedUntil() {
        return suspendedUntil;
    }
    
}
