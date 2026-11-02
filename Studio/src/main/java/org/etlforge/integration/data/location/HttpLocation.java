package org.etlforge.integration.data.location;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.etlforge.integration.data.ExportableProperty;
import org.etlforge.integration.data.ExportableStringProperty;
import org.etlforge.integration.data.Password;
import org.etlforge.integration.data.PasswordUtils;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.file.IgnoredLines;
import org.etlforge.integration.data.location.http.ExportableAdvancedProperties;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.http.HttpConnector;
import org.etlforge.integration.http.HttpProperties;
import org.etlforge.integration.http.HttpPropertiesSupport;
import org.etlforge.integration.http.HttpVerb;
import org.etlforge.integration.http.SslVersion;
import org.etlforge.system.EnumWithIdLookup;
import org.etlforge.util.id.KongaID;

public final class HttpLocation extends DataLocation implements HttpConnector, LoginProtectedResource, LineBasedSourceData {
   public static final String LOGIN = "login";
   private static final String PASSWORD = "password";
   public static final String URL = "httpurl";
   public static final String CERTIFICATE = "certificatename";
   public static final String HTTP_VERB = "http_verb";
   public static final String RESPONSE_TARGET_ID = "target_target_id";
   public static final String HTTP_PROPERTIES = "httpProperties";
   public static final String CONTENT_TYPE = "httpContentType";
   public static final String LINES_TO_IGNORE = "lines_to_ignore";
   public static final String USE_BASIC_HTTP_AUTHENTICATION = "use_basic_http_authentication";

   public static String[] getPropertyKeys() {
      return new String[]{
         "login",
         "password",
         "httpurl",
         "certificatename",
         "http_verb",
         "target_target_id",
         "httpProperties",
         "httpContentType",
         "use_basic_http_authentication",
         "enable_chunked_transfer_encoding",
         "enable_content_encoding",
         "send_expect_100_continue",
         "http_request_headers",
         "http_ssl_version"
      };
   }

   public HttpLocation() {
      super(DataLocationType.HTTP);
   }

   @Override
   public String getLogin() {
      return this.getProperty("login");
   }

   public void setLogin(String var1) {
      this.setStringProperty("login", var1);
   }

   @Override
   public Optional<Password> getPassword() {
      return PasswordUtils.getPassword(this, "password");
   }

   public void setPassword(Optional<Password> var1) {
      PasswordUtils.setPassword(this, "password", var1);
   }

   @Override
   public String getUrl() {
      return this.getProperty("httpurl");
   }

   @Override
   public void setUrl(String var1) {
      this.setStringProperty("httpurl", var1);
   }

   public String getCertificate() {
      return this.getProperty("certificatename");
   }

   public void setCertificate(String var1) {
      this.setStringProperty("certificatename", var1);
   }

   @Override
   public HttpVerb getHttpVerb() {
      int var1 = this.getIntProperty("http_verb", -1);
      return EnumWithIdLookup.tryLookup(HttpVerb.class, var1).orNull();
   }

   @Override
   public void setHttpVerb(HttpVerb var1) {
      this.setIntProperty("http_verb", var1.getId());
   }

   @Override
   public SslVersion getSslVersion() {
      SslVersion var1 = SslVersion.defaultVersion();
      int var2 = this.getIntProperty("http_ssl_version", var1.getId());
      return EnumWithIdLookup.lookupOr(var2, var1);
   }

   @Override
   public void setSslVersion(SslVersion var1) {
      this.setIntProperty("http_ssl_version", var1.getId());
   }

   @Override
   public HttpProperties getHttpProperties() {
      synchronized (this.getDataLock()) {
         return new HttpPropertiesSupport(this.getData()).createProperties();
      }
   }

   @Override
   public void setHttpProperties(HttpProperties var1) {
      HttpProperties var2;
      synchronized (this.getDataLock()) {
         var2 = this.getHttpProperties();
         if (!var1.equals(this.getHttpProperties())) {
            new HttpPropertiesSupport(this.getData()).applyProperties(var1);
         }
      }

      this.firePropertyChange("httpProperties", var2, var1);
   }

   public void setTargetForResponse(TargetId var1) {
      TargetId var2;
      synchronized (this.getDataLock()) {
         var2 = this.getTargetIdForResponse();
         this.setProperty("target_target_id", var1);
      }

      if (!KongaID.areEqual(var1, var2)) {
         this.firePropertyChange("target_target_id", var2, var1);
      }
   }

   public TargetId getTargetIdForResponse() {
      String var1 = this.getProperty("target_target_id");
      if (var1 != null && var1.length() > 0) {
         try {
            return new TargetId(var1);
         } catch (IllegalArgumentException var3) {
         }
      }

      return null;
   }

   @Override
   public void collectValidationMessages(ValidationMessageCollector var1) {
      super.collectValidationMessages(var1);
      new HttpLocationValidator(this, var1).validate();
   }

   @Override
   public ImmutableList<? extends ExportableProperty> getExportableProperties() {
      return ImmutableList.of(
         new ExportableStringProperty(this, "httpurl", LocationMessages.getString("HttpLocation.Property.URL"), true),
         new ExportableStringProperty(this, "login", LocationMessages.getString("Property.Login"), true),
         new ExportableStringProperty(this, "password", LocationMessages.getString("Property.Password"), true),
         new ExportableAdvancedProperties(this)
      );
   }

   @Override
   public DataLocation getCopyForSourceOrTarget(SourceTarget var1) {
      Preconditions.checkNotNull(var1);
      HttpLocation var2 = (HttpLocation)this.clone();
      if (var1 == SourceTarget.Source) {
         var2.setProperty("httpContentType", (String)null);
         var2.setProperty("target_target_id", (String)null);
      } else {
         var2.setProperty("ignore_first", (String)null);
         var2.setProperty("ignore_last", (String)null);
      }

      return var2;
   }

   public static HttpVerb getDefaultVerb(SourceTarget var0) {
      switch (var0) {
         case Source:
            return HttpVerb.GET;
         case Target:
            return HttpVerb.POST;
         default:
            throw new IllegalArgumentException("Invalid SourceTarget: " + var0);
      }
   }

   public static ImmutableSet<HttpVerb> getSupportedVerbs() {
      return Sets.immutableEnumSet(HttpVerb.GET, HttpVerb.POST, HttpVerb.PUT, HttpVerb.DELETE);
   }

   @Override
   public final IgnoredLines getLinesToIgnore() {
      synchronized (this.getDataLock()) {
         return new IgnoredLines(this.getIntProperty("ignore_first", 0), this.getIntProperty("ignore_last", 0));
      }
   }

   @Override
   public final void setLinesToIgnore(IgnoredLines var1) {
      IgnoredLines var2;
      synchronized (this.getDataLock()) {
         var2 = this.getLinesToIgnore();
         this.setProperty("ignore_first", var1.getFirst());
         this.setProperty("ignore_last", var1.getLast());
      }

      if (!var2.equals(var1)) {
         this.firePropertyChange("lines_to_ignore", var2, var1);
      }
   }

   public String getContentType() {
      return this.getProperty("httpContentType", null);
   }

   public void setBasicHttpAuthentication(boolean var1) {
      this.setBooleanProperty("use_basic_http_authentication", var1);
   }

   public boolean getBasicHttpAuthentication() {
      return this.getBooleanProperty("use_basic_http_authentication", false);
   }

   public void setContentType(String var1) {
      this.setStringProperty("httpContentType", var1);
   }

   @Override
   public String getRequestHeaders() {
      return this.getProperty("http_request_headers");
   }

   @Override
   public void setRequestHeaders(String var1) {
      this.setStringProperty("http_request_headers", var1);
   }

   @Override
   public String getPasswordPropertyName() {
      return "password";
   }

   @Override
   public ImmutableSet<String> getNamesOfPropertiesToCache() {
      return ImmutableSet.of("httpurl", "login");
   }
}
