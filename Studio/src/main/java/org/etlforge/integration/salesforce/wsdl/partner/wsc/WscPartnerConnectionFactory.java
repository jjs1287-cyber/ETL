package org.etlforge.integration.salesforce.wsdl.partner.wsc;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.sforce.ws.ConnectorConfig;
import java.util.logging.Logger;
import org.etlforge.integration.salesforce.LoginCredentials;
import org.etlforge.integration.salesforce.OrgIdentifier;
import org.etlforge.integration.salesforce.SalesforceConnection;
import org.etlforge.integration.salesforce.SalesforceConnectionFactory;
import org.etlforge.util.net.ProxySettings;

public final class WscPartnerConnectionFactory implements SalesforceConnectionFactory {
   private Supplier<TraceSettings> traceSettingsSupplier;
   private Supplier<ProxySettings> proxySettingsSupplier;
   private Logger logger = Logger.getAnonymousLogger();

   public WscPartnerConnectionFactory() {
      this.setTraceSettings(TraceSettings.noTracing());
   }

   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1, "logger");
   }

   @Override
   public synchronized SalesforceConnection newConnection(OrgIdentifier var1) {
      ConnectorConfig var2 = this.createConnectorConfig(var1);
      return new WscPartnerConnectionWrapper(var1, var2);
   }

   private ConnectorConfig createConnectorConfig(OrgIdentifier var1) {
      ConnectorConfig var2 = new ConnectorConfig();
      var2.setManualLogin(true); var2.setConnectionTimeout(15000); var2.setReadTimeout(30000);
      this.applyCredentials(var2, var1);
      this.applyEndpoint(var2, var1);
      this.applyProxySettings(var2);
      this.applyTraceSettings(var2); if (var1.getCredentials().isOAuth()) var2.setTraceMessage(false);
      var2.setPrettyPrintXml(true);
      return var2;
   }

   private void applyCredentials(ConnectorConfig var1, OrgIdentifier var2) {
      LoginCredentials var3 = var2.getCredentials();
      var1.setUsername(var3.username);
      var1.setPassword(var3.password + var3.securityToken);
   }

   private void applyEndpoint(ConnectorConfig var1, OrgIdentifier var2) {
      String var3 = var2.getLoginUrl();
      this.logger.info("Salesforce login URL: " + var3);
      var1.setAuthEndpoint(var3);
      var1.setServiceEndpoint(var3);
   }

   private void applyProxySettings(ConnectorConfig var1) {
      if (this.proxySettingsSupplier == null) {
         this.logger.info("No supplier of proxy settings available --> no proxy settings will be used.");
      } else {
         ProxySettings var2 = this.proxySettingsSupplier.get();
         this.logger.info("Proxy settings: " + var2);
         WscUtils.applyProxySettings(var1, var2);
      }
   }

   private void applyTraceSettings(ConnectorConfig var1) {
      TraceSettings var2 = this.traceSettingsSupplier.get();
      var2.applyTo(var1);
   }

   public final synchronized void setProxySettingsSupplier(Supplier<ProxySettings> var1) {
      this.proxySettingsSupplier = var1;
   }

   public final synchronized void setTraceSettings(TraceSettings var1) {
      this.traceSettingsSupplier = Suppliers.ofInstance(Preconditions.checkNotNull(var1));
   }

   public final synchronized void setTraceSettingsSupplier(Supplier<TraceSettings> var1) {
      this.traceSettingsSupplier = Preconditions.checkNotNull(var1);
   }
}
