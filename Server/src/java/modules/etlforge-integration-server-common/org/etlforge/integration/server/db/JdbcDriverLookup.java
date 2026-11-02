package org.etlforge.integration.server.db;

import java.io.File;
import java.util.List;

public interface JdbcDriverLookup {
   List<File> getJarFiles();

   boolean isKnownDriver(String var1);

   String getDriverClassName(String var1) throws JdbcDriverInfoException;
}
