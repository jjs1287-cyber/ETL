package org.etlforge.integration.server.db.infoprovider;

import java.sql.Connection;
import java.sql.SQLException;
import org.etlforge.integration.server.accesscontrol.ServerAccessException;
import org.etlforge.integration.server.db.ServerDbException;

public interface ConnectionFactory {
   Connection newConnection() throws ServerDbException, ServerAccessException, SQLException;
}
