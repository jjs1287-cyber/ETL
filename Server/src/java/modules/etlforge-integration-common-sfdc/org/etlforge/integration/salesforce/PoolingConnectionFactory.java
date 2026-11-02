package org.etlforge.integration.salesforce;

public interface PoolingConnectionFactory extends SalesforceConnectionFactory {
   void forgetPooledConnections();

   String getPooledConnectionsReport();
}
