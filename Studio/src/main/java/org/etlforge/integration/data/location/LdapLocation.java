package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.id.SourceId;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.ldap.ExportablePortProperty;
import org.etlforge.integration.data.location.ldap.ExportableSecurityLevelProperty;
import org.etlforge.integration.data.location.ldap.LdapLocationValidator;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.ldap.LdapConnectionParameters;
import org.etlforge.integration.ldap.LdapSecurityLevel;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.PortAccessible;

public final class LdapLocation extends DataLocation implements LoginProtectedResource, PortAccessible {
   public static final String SERVER_PROPERTY = "locator";
   public static final String BASE_NAME_PROPERTY = "path";
   public static final String USER_PROPERTY = "login";
   private static final String PASSWORD_PROPERTY = "password";
   public static final String PORT_PROPERTY = "port";
   public static final String SECURITY_PROPERTY = "secured";
   public static final String SOURCE_ID_PROPERTY = "source_id";
   public static final String TARGET_ID_PROPERTY = "target_id";
   private transient PortAccessibleSupport portSupport = new PortAccessibleSupport(this, "port");

   public static String[] getPropertyKeys() {
      return new String[]{"locator", "path", "login", "password", "port", "secured", "source_id", "target_id"};
   }

   public LdapLocation() {
      super(DataLocationType.LDAP);
   }

   public String getServer() {
      return this.getProperty("locator", "");
   }

   public void setServer(String var1) {
      this.setStringProperty("locator", var1);
   }

   public String getBaseName() {
      return this.getProperty("path", "");
   }

   public void setBaseName(String var1) {
      this.setStringProperty("path", var1);
   }

   @Override
   public Port getPort() {
      synchronized (this.getDataLock()) {
         return this.portSupport.getPort();
      }
   }

   @Override
   public void setPort(Port var1) {
      Port var2;
      synchronized (this.getDataLock()) {
         var2 = this.portSupport.setPort(var1);
      }

      this.firePropertyChange("port", var2, var1);
   }

   @Override
   public String getLogin() {
      return this.getProperty("login", "");
   }

   public void setUser(String var1) {
      this.setStringProperty("login", var1);
   }

   @Override
   public Optional<Password> getPassword() {
      return PasswordUtils.getPassword(this, "password");
   }

   public void setPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "password", var1);
   }

   public void setSecurityLevel(LdapSecurityLevel var1) {
      this.setIntProperty("secured", var1.ordinal());
   }

   public LdapSecurityLevel getSecurityLevel() {
      LdapSecurityLevel var1 = LdapSecurityLevel.NON_SECURE;
      int var2 = this.getIntProperty("secured", var1.getId());
      return EnumWithIdLookup.lookupOr(var2, var1);
   }

   public SourceId getSourceId() {
      String var1 = this.getProperty("source_id");
      if (var1 != null) {
         try {
            return new SourceId(var1);
         } catch (IllegalArgumentException var3) {
         }
      }

      return null;
   }

   public void setSourceId(SourceId var1) {
      SourceId var2;
      synchronized (this.getDataLock()) {
         var2 = this.getSourceId();
         this.setProperty("source_id", var1);
         if (var1 != null) {
            this.setProperty("target_id", (String)null);
         }
      }

      this.firePropertyChange("source_id", var2, var1);
   }

   public TargetId getTargetId() {
      String var1 = this.getProperty("target_id");
      if (var1 != null) {
         try {
            return new TargetId(var1);
         } catch (IllegalArgumentException var3) {
         }
      }

      return null;
   }

   public void setTargetId(TargetId var1) {
      TargetId var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTargetId();
         this.setProperty("target_id", var1);
         if (var1 != null) {
            this.setProperty("source_id", (String)null);
         }
      }

      this.firePropertyChange("target_id", var2, var1);
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      LdapLocationValidator var2 = new LdapLocationValidator(this, var1);
      var2.validate();
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableStringProperty(this, "locator", LocationMessages.getString("Property.Server"), true),
         new ExportablePortProperty(this),
         new ExportableStringProperty(this, "login", LocationMessages.getString("Property.Login"), true),
         new ExportableStringProperty(this, "password", LocationMessages.getString("Property.Password"), true),
         new ExportableStringProperty(this, "path", LocationMessages.getString("LdapLocation.Property.BaseName"), true),
         new ExportableSecurityLevelProperty(this)
      );
   }

   @Override
   public DataLocation getCopyForSourceOrTarget(SourceTarget var1) {
      Preconditions.checkNotNull(var1);
      return this.clone();
   }

   public LdapLocation clone() {
      synchronized (this.getDataLock()) {
         LdapLocation var2 = (LdapLocation)super.clone();
         var2.portSupport = new PortAccessibleSupport(var2, "port");
         return var2;
      }
   }

   @Override
   public String getPasswordPropertyName() {
      return "password";
   }

   public LdapConnectionParameters getConnectionParameters() {
      synchronized (this.getDataLock()) {
         return new LdapConnectionParameters(
            this.getServer(), this.getPort(), this.getLogin(), PasswordUtils.getClearTextPassword(this), this.getSecurityLevel()
         );
      }
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("locator", "path", "login");
   }
}
