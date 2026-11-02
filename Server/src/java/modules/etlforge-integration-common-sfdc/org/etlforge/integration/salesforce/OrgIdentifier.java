package org.etlforge.integration.salesforce;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class OrgIdentifier {
   private final ServerHost server;
   private final SalesforceVersion version;
   private final LoginCredentials credentials;

   public OrgIdentifier(ServerHost var1, SalesforceVersion var2, LoginCredentials var3) {
      this.server = Preconditions.checkNotNull(var1);
      this.version = Preconditions.checkNotNull(var2);
      this.credentials = var3;
   }

   public ServerHost getServer() {
      return this.server;
   }

   public SalesforceVersion getVersion() {
      return this.version;
   }

   public LoginCredentials getCredentials() {
      return this.credentials;
   }

   public String getUserName() {
      return this.isIncomplete() ? "" : this.credentials.username;
   }

   public String getPassword() {
      return this.isIncomplete() ? "" : this.credentials.password;
   }

   public boolean isIncomplete() {
      return this.credentials == null;
   }

   public String getLoginUrl() {
      boolean var1 = this.isIncomplete() ? false : this.credentials.sandbox;
      return var1 ? this.server.getSandboxLoginUrl(this.version) : this.server.getLoginUrl(this.version);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(this.server, this.version, this.credentials);
   }

   @Override
   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      }

      if (!(var1 instanceof OrgIdentifier)) {
         return false;
      }

      OrgIdentifier var2 = (OrgIdentifier)var1;
      return this.server.equals(var2.server) && this.version.equals(var2.version) && Objects.equal(this.credentials, var2.credentials);
   }

   @Override
   public String toString() {
      return Objects.toStringHelper(this)
         .add("Server", this.server)
         .add("Version", this.version)
         .add("User", this.getUserName())
         .add("Sandbox", this.isIncomplete() ? "false" : this.credentials.sandbox)
         .toString();
   }

   public OrgIdentifier forVersion(SalesforceVersion var1) {
      return var1.equals(this.version) ? this : new OrgIdentifier(this.server, var1, this.credentials);
   }

   public OrgIdentifier resolveDataElementDefaultValues() {
      return new OrgIdentifier(this.server.resolveDataElementDefaultValues(), this.version, this.credentials.resolveDataElementDefaultValues());
   }
}
