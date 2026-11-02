package org.etlforge.integration.server.engine.connector;

import java.sql.SQLException;
import org.etlforge.integration.design.netsuite.definition.NetSuiteDefinitionDesign;
import org.etlforge.integration.server.authentication.AuthenticateUser;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerRuntimeException;

public class ConnectorNetSuiteDefinitionDesign {
   private static NetSuiteDefinitionDesign getNetSuiteDefinition(String var0, String var1) {
      try {
         AuthenticateUser.isValidUser(var0, var1);
         return null;
      } catch (SQLException var3) {
         throw new ConnectorServerRuntimeException(var3);
      }
   }
}
