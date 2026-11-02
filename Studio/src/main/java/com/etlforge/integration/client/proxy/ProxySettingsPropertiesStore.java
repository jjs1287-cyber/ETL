package com.etlforge.integration.client.proxy;

import com.google.common.base.Optional;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.etlforge.lang.Objects;
import org.etlforge.util.net.Port;
import org.etlforge.util.net.ProxySettings;
import org.etlforge.util.properties.PropertiesReader;
import org.etlforge.util.properties.PropertiesWriter;

final class ProxySettingsPropertiesStore implements ProxySettingsStore {
   private static final String CERTS = "certs";
   private static final String PORT = "port";
   private static final String PASSWORD = "password";
   private static final String NTLM = "ntlm";
   private static final String USER = "user";
   private static final String HOST = "host";
   private final Properties props;
   private Optional<PasswordCrypto> pwdCrypto = Optional.absent();

   public ProxySettingsPropertiesStore() {
      this(new Properties());
   }

   public ProxySettingsPropertiesStore(Properties var1) {
      this.props = var1;
   }

   public void setPwdCrypto(PasswordCrypto var1) {
      this.pwdCrypto = Optional.of(var1);
   }

   @Override
   public ProxySettings get() {
      Port var1 = this.readPort();
      String var2 = this.decryptPassword();
      ProxySettings.CertificatePolicy var3 = this.readCertPolicy();
      return new ProxySettings(this.props.getProperty("host"), var1, this.props.getProperty("user"), var2, this.props.getProperty("ntlm"), var3);
   }

   private String decryptPassword() {
      String var1 = this.props.getProperty("password", "");
      return this.needsDecryption(var1) ? this.pwdCrypto.get().decrypt(var1) : var1;
   }

   private boolean needsDecryption(String var1) {
      return var1.length() > 0 && this.pwdCrypto.isPresent();
   }

   private ProxySettings.CertificatePolicy readCertPolicy() {
      String var1 = this.props.getProperty("certs", "");
      if (!var1.isEmpty()) {
         try {
            return ProxySettings.CertificatePolicy.valueOf(var1);
         } catch (Exception var3) {
            var3.printStackTrace();
         }
      }

      return ProxySettings.CertificatePolicy.STRICT;
   }

   private Port readPort() {
      try {
         String var1 = this.props.getProperty("port", "0");
         return Port.valueOf(Integer.parseInt(var1));
      } catch (NumberFormatException var2) {
         var2.printStackTrace();
         return Port.DEFAULT;
      }
   }

   @Override
   public boolean put(ProxySettings var1) {
      boolean var2 = false;
      if (this.set("host", var1.getHost())) {
         var2 = true;
      }

      if (this.set("port", "" + var1.getPort().getPort())) {
         var2 = true;
      }

      if (this.set("user", var1.getUser())) {
         var2 = true;
      }

      String var3 = this.encrypt(var1.getPassword());
      if (this.set("password", var3)) {
         var2 = true;
      }

      if (this.set("ntlm", var1.getNtlmDomain())) {
         var2 = true;
      }

      if (this.set("certs", var1.getCertPolicy().name())) {
         var2 = true;
      }

      return var2;
   }

   private boolean set(String var1, String var2) {
      Object var3 = this.props.setProperty(var1, var2);
      return !Objects.equal(var3, var2);
   }

   private String encrypt(String var1) {
      return this.pwdCrypto.isPresent() ? this.pwdCrypto.get().encrypt(var1) : var1;
   }

   public void writeToFile(File var1) throws IOException {
      PropertiesWriter.write(this.props, var1);
   }

   public void readFromFile(File var1) throws IOException {
      Properties var2 = PropertiesReader.read(var1);
      this.props.putAll(var2);
   }
}
