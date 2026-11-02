package org.etlforge.integration.data.location;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.ExportableIntProperty;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.ftp.FtpLocationValidator;
import org.etlforge.integration.data.location.ftp.FtpMode;
import org.etlforge.integration.data.location.ftp.FtpSecurityOption;
import org.etlforge.integration.data.location.ftp.FtpTransferType;
import org.etlforge.integration.data.location.ftp.FtpsAuthenticationMode;
import org.etlforge.integration.data.location.ftp.FtpsExplicitness;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.lang.Arrays2;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.PortAccessible;

public final class FtpLocation extends FileLocation implements PortAccessible, LoginProtectedResource {
   public static final int DEFAULT_PORT = 21;
   public static final FtpTransferType DEFAULT_TRANSFER_TYPE = FtpTransferType.Binary;
   public static final boolean DEFAULT_PASSIVE_MODE = true;
   public static final String PROPERTY_HOST = "locator";
   public static final String PROPERTY_PORT = "port";
   public static final String PROPERTY_HOST_PATH = "path";
   public static final String PROPERTY_DSN = "dsn";
   public static final String PROPERTY_TRANSFER_TYPE = "transfer_type";
   public static final String PROPERTY_PASSIVE_MODE = "passive_mode";
   public static final String PROPERTY_NLST = "ftp.nlst";
   public static final String FTPS_EXPLICIT = "ftps_explicit";
   public static final String FTPS_AUTHENTICATION = "ftps_auth";
   public static final String PROPERTY_TRANSFER_TIMEOUT = "transfer_timeout";
   public static final int DEFAULT_TRANSFER_TIMEOUT = 14400;
   private transient PortAccessibleSupport portSupport = new PortAccessibleSupport(this, "port");

   public static String[] getPropertyKeys() {
      return Arrays2.append(
         FileLocation.getPropertyKeys(), new String[]{"locator", "port", "path", "dsn", "transfer_type", "passive_mode", "ftp.nlst", "transfer_timeout", "sftp_key_id", "sftp_host_sha256"}
      );
   }

   public FtpLocation() {
      super(DataLocationType.FTP);
   }

   public String getHost() {
      return this.getProperty("locator");
   }

   public void setHost(String var1) {
      this.setStringProperty("locator", var1);
   }

   @Override
   public Port getPort() {
      synchronized (this.getDataLock()) {
         Port var2 = this.portSupport.getPort();
         return var2 != null && var2.getPort() == 21 ? Port.DEFAULT : var2;
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

   public FtpSecurityOption getSecurityOption() {
      int var1 = this.getIntProperty("encrypt_choice", -1);
      return EnumWithIdLookup.lookupOr(var1, FtpSecurityOption.defaultOption());
   }

   public void setSecurityOption(FtpSecurityOption var1) {
      this.setIntProperty("encrypt_choice", var1.getId());
   }

   public String getHostPath() {
      return this.getProperty("path");
   }

   public void setHostPath(String var1) {
      this.setStringProperty("path", var1);
   }

   public FtpTransferType getTransferType() {
      synchronized (this.getDataLock()) {
         int var2 = this.getIntProperty("transfer_type", -1);
         return var2 >= 0 ? FtpTransferType.getFromId(var2) : DEFAULT_TRANSFER_TYPE;
      }
   }

   public void setTransferType(FtpTransferType var1) {
      FtpTransferType var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTransferType();
         this.setProperty("transfer_type", String.valueOf(var1.getId()));
      }

      if (var1 != var2) {
         this.firePropertyChange("transfer_type", var2, var1);
      }
   }

   public boolean getPassiveMode() {
      synchronized (this.getDataLock()) {
         return this.getBooleanProperty("passive_mode", true);
      }
   }

   public void setPassiveMode(boolean var1) {
      this.setBooleanProperty("passive_mode", var1);
   }

   public void setTransferTimeout(int var1) {
      this.setIntProperty("transfer_timeout", var1);
   }

   public int getTransferTimeout() {
      synchronized (this.getDataLock()) {
         return this.getIntProperty("transfer_timeout", 14400);
      }
   }

   public boolean isFtpRenameEnabled() {
      int var1 = this.getIntProperty("secured", 0);
      return var1 == 0;
   }

   public void setFtpRenameEnabled(boolean var1) {
      int var2 = var1 ? 0 : 1;
      this.setIntProperty("secured", var2);
   }

   public void setNLSTEnabled(boolean var1) {
      this.setBooleanProperty("ftp.nlst", var1);
   }

   public boolean isNLSTEnabled() {
      return this.getBooleanProperty("ftp.nlst", false);
   }

   public FtpMode getFtpMode() {
      FtpSecurityOption var1 = this.getSecurityOption();
      switch (var1) {
         case NONE:
            return FtpMode.UNSECURE;
         case SFTP:
            return FtpMode.SFTP;
         case REQUIRE_SSL_FOR_ALL:
            return this.isExplicitFtps() ? FtpMode.EXPLICIT_FTPS : FtpMode.IMPLICIT_FTPS;
         default:
            return FtpMode.EXPLICIT_FTPS;
      }
   }

   public FtpsExplicitness getFtpsExplicitness() {
      int var1 = this.getIntProperty("ftps_explicit", FtpsExplicitness.defaultValue().getId());
      return EnumWithIdLookup.lookupOr(var1, FtpsExplicitness.defaultValue());
   }

   public void setFtpsExplicitness(FtpsExplicitness var1) {
      this.setIntProperty("ftps_explicit", var1.getId());
   }

   private boolean isExplicitFtps() {
      return this.getFtpsExplicitness() == FtpsExplicitness.EXPLICIT;
   }

   public void setFtpsAuthenticationMode(FtpsAuthenticationMode var1) {
      this.setIntProperty("ftps_auth", var1.getId());
   }

   public FtpsAuthenticationMode getFtpsAuthenticationMode() {
      int var1 = this.getIntProperty("ftps_auth", FtpsAuthenticationMode.defaultValue().getId());
      return EnumWithIdLookup.lookupOr(var1, FtpsAuthenticationMode.defaultValue());
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      FtpLocationValidator var2 = new FtpLocationValidator(this, var1);
      var2.validate();
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableStringProperty(this, "locator", LocationMessages.getString("FtpLocation.Property.Host"), true),
         new ExportableIntProperty(this, "port", LocationMessages.getString("FtpLocation.Property.Port"), true, 21),
         new ExportableStringProperty(this, "path", LocationMessages.getString("FtpLocation.Property.Path"), true),
         new ExportableStringProperty(this, "file_name", LocationMessages.getString("FileLocation.Property.File"), false),
         new ExportableStringProperty(this, "login", LocationMessages.getString("Property.Login"), true),
         new ExportableStringProperty(this, "password", LocationMessages.getString("Property.Password"), true)
      );
   }

   public FtpLocation getCopyForSourceOrTarget(SourceTarget var1) {
      FtpLocation var2 = (FtpLocation)super.getCopyForSourceOrTarget(var1);
      if (var1 == SourceTarget.Source) {
         this.setProperty("secured", (String)null);
      } else {
         this.setProperty("ftp.nlst", (String)null);
      }

      return var2;
   }

   public FtpLocation clone() {
      synchronized (this.getDataLock()) {
         FtpLocation var2 = (FtpLocation)super.clone();
         var2.portSupport = new PortAccessibleSupport(var2, "port");
         return var2;
      }
   }

   @Override
   public String getLogin() {
      return this.getProperty("login");
   }

   @Override
   public String getPasswordPropertyName() {
      return "password";
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("locator", "path", "login");
   }
}
