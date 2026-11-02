package org.etlforge.integration.client.database.serverapi;

public interface JdbcInfoProvider extends DatabaseInfoProvider {
   void getDrivers(JdbcDriverListCallback var1);
}
