package org.etlforge.integration.server.engine.connector.endpoint;

import com.google.common.base.Strings;
import org.etlforge.integration.server.engine.connector.autodesk.ConnectorEndpointAutoDesk;
import org.etlforge.integration.server.engine.connector.ebs.ConnectorEndpointEbs;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerAccessRuntimeException;
import org.etlforge.integration.server.engine.connector.exception.ConnectorServerNotSupportedRuntimeException;
import org.etlforge.integration.server.engine.connector.framework.ConnectorEndpoint;
import org.etlforge.integration.server.engine.connector.msax.ConnectorEndpointMsAX;
import org.etlforge.integration.server.engine.connector.mscrm.ConnectorEndpointMsCrm;
import org.etlforge.integration.server.engine.connector.msgp.ConnectorEndpointMsGP;
import org.etlforge.integration.server.engine.connector.netsuite.ConnectorEndpointNetSuite;
import org.etlforge.integration.server.engine.connector.sap.ConnectorEndpointSap;

public class ConnectorEndpointType {
   final String endpointTypeName;
   final String endpointClassName;

   public ConnectorEndpointType(String var1, String var2) {
      this.endpointTypeName = var1;
      this.endpointClassName = var2 == null ? "" : var2.trim();
   }

   private boolean isInternalClass() {
      return Strings.isNullOrEmpty(this.endpointClassName);
   }

   boolean isValidEndpointType() {
      if (this.isInternalClass()) {
         return true;
      }

      try {
         this.newConnectorEndpoint();
         return true;
      } catch (Exception var2) {
         return false;
      }
   }

   ConnectorEndpoint newConnectorEndpoint() {
      String var1 = null;
      if (this.isInternalClass()) {
         if (ConnectorEndpointTypeDef.isSapSystem(this.endpointTypeName)) {
            return new ConnectorEndpointSap();
         }

         if (ConnectorEndpointTypeDef.isMSCrmSystem(this.endpointTypeName)) {
            return new ConnectorEndpointMsCrm();
         }

         if (ConnectorEndpointTypeDef.isMSGPSystem(this.endpointTypeName)) {
            return new ConnectorEndpointMsGP();
         }

         if (ConnectorEndpointTypeDef.isAutoDeskSystem(this.endpointTypeName)) {
            return new ConnectorEndpointAutoDesk();
         }

         if (ConnectorEndpointTypeDef.isMSAXSystem(this.endpointTypeName)) {
            return new ConnectorEndpointMsAX();
         }

         if (ConnectorEndpointTypeDef.isEBSSystem(this.endpointTypeName)) {
            return new ConnectorEndpointEbs();
         }

         if (ConnectorEndpointTypeDef.isNetSuiteSystem(this.endpointTypeName)) {
            return new ConnectorEndpointNetSuite();
         }

         var1 = "connector endpoint " + this.endpointTypeName + " is not supported! ";
         throw new ConnectorServerNotSupportedRuntimeException(var1);
      } else {
         try {
            Class var2 = Class.forName(this.endpointClassName);
            return (ConnectorEndpoint)var2.newInstance();
         } catch (InstantiationException var3) {
            var1 = "Connector Server cannot instantiate the connector "
               + this.endpointTypeName
               + " with class "
               + this.endpointClassName
               + " : "
               + var3.getMessage();
         } catch (IllegalAccessException var4) {
            var1 = "Connector Server cannot access class" + this.endpointClassName + " for the connector " + this.endpointTypeName + " : " + var4.getMessage();
         } catch (ClassNotFoundException var5) {
            var1 = "Connector Server cannot find class " + this.endpointClassName + " for the connector " + this.endpointTypeName + " : " + var5.getMessage();
         }

         throw new ConnectorServerAccessRuntimeException(var1);
      }
   }
}
