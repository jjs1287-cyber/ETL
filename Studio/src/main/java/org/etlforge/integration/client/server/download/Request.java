package org.etlforge.integration.client.server.download;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.nio.charset.Charset;
import org.etlforge.system.ArgCheck;

public final class Request {
   private final String category;
   private final String fileIdentifier;
   private final Credentials credentials;
   private final Charset encoding;

   public Request(String var1, String var2, Credentials var3) {
      this(var1, var2, var3, Charsets.UTF_8);
   }

   public Request(String var1, String var2, Credentials var3, Charset var4) {
      this.category = ArgCheck.checkNotBlank(var1);
      this.fileIdentifier = ArgCheck.checkNotBlank(var2);
      this.credentials = Preconditions.checkNotNull(var3);
      this.encoding = var4;
   }

   public String toUrl(String var1, boolean var2) {
      return String.format("%s://%s/etlforge_ws/download/%s/%s", var2 ? "https" : "http", var1, this.category, this.fileIdentifier);
   }

   public String getCategory() {
      return this.category;
   }

   public String getFileIdentifier() {
      return this.fileIdentifier;
   }

   public Credentials getCredentials() {
      return this.credentials;
   }

   public Charset getEncoding() {
      return this.encoding;
   }
}
