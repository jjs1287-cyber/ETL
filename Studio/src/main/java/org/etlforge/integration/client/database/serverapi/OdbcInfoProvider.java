package org.etlforge.integration.client.database.serverapi;

public interface OdbcInfoProvider extends DatabaseInfoProvider {
   void getDrivers(OdbcDriverListCallback var1);
}
