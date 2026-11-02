package org.etlforge.integration.server.conf;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.driver.JdbcDriverDescriptor;
import org.etlforge.integration.database.driver.JdbcDriverDesriptorBuilder;
import org.etlforge.integration.server.exception.ServerConfigurationException;
import org.etlforge.xml.ElementVisitor;
import org.etlforge.xml.Elements;
import org.etlforge.xml.KongaXmlUtils;
import org.etlforge.xml.dom.DomParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class JdbcDriversConfig {
   private static final String CONFIG_FILE = "JdbcDrivers.conf";
   private static JdbcDriversConfig conf;
   private final Map<String, JdbcDriverDescriptor> drivers;

   static JdbcDriversConfig fromXml(String var0) throws Exception {
      return new JdbcDriversConfig(DomParser.parseXmlString(var0));
   }

   public JdbcDriversConfig(File var1) throws ServerConfigurationException {
      this(parseFile(var1));
   }

   private static Document parseFile(File var0) throws ServerConfigurationException {
      try {
         return DomParser.parseFile(var0);
      } catch (Exception var2) {
         throw new ServerConfigurationException("The file " + var0.getAbsolutePath() + " does not contain a valid JDBC driver configuration.", var2);
      }
   }

   private JdbcDriversConfig(Document var1) throws ServerConfigurationException {
      this.drivers = this.loadDrivers(var1);
   }

   private Map<String, JdbcDriverDescriptor> loadDrivers(Document var1) throws ServerConfigurationException {
      JdbcDriversConfig.DriverFactory var2 = new JdbcDriversConfig.DriverFactory();
      Elements.matching("JdbcConfig/Drivers/*", var1).visit(var2);
      return var2.getDrivers();
   }

   public static synchronized JdbcDriversConfig getConfig() throws ServerConfigurationException {
      if (conf == null) {
         conf = load();
      }

      return conf;
   }

   private static JdbcDriversConfig load() throws ServerConfigurationException {
      JdbcDriversConfig var0 = loadFromFile();
      if (var0 == null) {
         var0 = loadFromClassPath();
      }

      if (var0 == null) {
         throw new ServerConfigurationException(
            "Could not locate the JdbcDrivers.conf file. Looked for it in the server home directory and also on the classpath."
         );
      } else {
         return var0;
      }
   }

   private static JdbcDriversConfig loadFromFile() throws ServerConfigurationException {
      if (ServerConfig.isServerHomeDefined()) {
         File var0 = new File(ServerConfig.getServerHome(), "JdbcDrivers.conf");
         if (var0.exists() && var0.canRead()) {
            return new JdbcDriversConfig(var0);
         }
      }

      return null;
   }

   private static JdbcDriversConfig loadFromClassPath() throws ServerConfigurationException {
      try {
         Document var0 = DomParser.parseResource("JdbcDrivers.conf");
         return new JdbcDriversConfig(var0);
      } catch (Exception var1) {
         throw new ServerConfigurationException("Failed to parse the jdbc.conf file: " + var1.getMessage(), var1);
      }
   }

   public List<JdbcDriverDescriptor> getRegisteredDrivers() throws ServerConfigurationException {
      return Lists.newArrayList(this.drivers.values());
   }

   public JdbcDriverDescriptor getDriverDescriptor(String var1) throws ServerConfigurationException {
      JdbcDriverDescriptor var2 = this.drivers.get(var1);
      if (var2 == null) {
         throw new ServerConfigurationException("The JDBC driver \"" + var1 + "\" has not been registered on this server.");
      } else {
         return var2;
      }
   }

   private static class DriverFactory implements ElementVisitor {
      private final JdbcDriverDesriptorBuilder builder = new JdbcDriverDesriptorBuilder();
      private final Map<String, JdbcDriverDescriptor> drivers = Collections.synchronizedMap(new HashMap<>());
      private ServerConfigurationException error;

      private DriverFactory() {
      }

      public Map<String, JdbcDriverDescriptor> getDrivers() throws ServerConfigurationException {
         if (this.error != null) {
            throw this.error;
         } else {
            return this.drivers;
         }
      }

      @Override
      public void visit(Element var1) {
         if (this.error == null) {
            try {
               this.builder.reset();
               String var2 = null;
               String var3 = null;

               for (Element var5 : Elements.under(var1)) {
                  String var6 = var5.getNodeName();
                  String var7 = KongaXmlUtils.getElementValue(var5);
                  if ("Name".equals(var6)) {
                     this.builder.name(var7);
                  } else if ("Class".equals(var6)) {
                     this.builder.driverClass(var7);
                  } else if ("Subprotocol".equals(var6)) {
                     this.builder.subprotocol(var7);
                  } else if ("BeginQuote".equals(var6)) {
                     var2 = var7 == null ? "" : var7;
                  } else if ("EndQuote".equals(var6)) {
                     var3 = var7 == null ? "" : var7;
                  }
               }

               this.builder.quotes(BeginEndQuote.fromStrings(var2, var3));
               JdbcDriverDescriptor var9 = this.builder.build();
               this.drivers.put(var9.getName(), var9);
            } catch (Exception var8) {
               this.error = new ServerConfigurationException("The Drivers section in jdbc.conf contains invalid data.", var8);
            }
         }
      }
   }
}
