package persistence;

import java.sql.Connection;
import java.util.List;
import static persistence.DBConnection.getConnectionFromInstance;
public class EntityRepository {

    protected Connection conn;
    protected EntityRepository(){
        conn = getConnectionFromInstance();
    }

}
