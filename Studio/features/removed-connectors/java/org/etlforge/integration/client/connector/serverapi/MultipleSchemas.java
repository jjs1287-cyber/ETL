package org.etlforge.integration.client.connector.serverapi;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.etlforge.util.zip.CompressedAndBase64EncodedString;

public final class MultipleSchemas implements Schemas {
   private final String requestFileName;
   private final String requestRootName;
   private final String responseFileName;
   private final String responseRootName;
   private final ImmutableMap<String, CompressedAndBase64EncodedString> fileContent;

   public MultipleSchemas(String var1, String var2, String var3, String var4, Map<String, CompressedAndBase64EncodedString> var5) {
      this.requestFileName = Preconditions.checkNotNull(var1, "requestFileName");
      this.requestRootName = Preconditions.checkNotNull(var2, "requestRootName");
      this.responseFileName = Preconditions.checkNotNull(var3, "responseFileName");
      this.responseRootName = Preconditions.checkNotNull(var4, "responseRootName");
      this.fileContent = ImmutableMap.copyOf(Preconditions.checkNotNull(var5, "fileContent"));
   }

   public String getRequestFileName() {
      return this.requestFileName;
   }

   @Override
   public String getRequestRootName() {
      return this.requestRootName;
   }

   public String getResponseFileName() {
      return this.responseFileName;
   }

   @Override
   public String getResponseRootName() {
      return this.responseRootName;
   }

   public boolean contains(String var1) {
      return this.fileContent.containsKey(var1);
   }

   public CompressedAndBase64EncodedString getContent(String var1) {
      CompressedAndBase64EncodedString var2 = this.fileContent.get(var1);
      Preconditions.checkArgument(var2 != null, "Unknown filename: " + var1);
      return var2;
   }

   public ImmutableMap<String, CompressedAndBase64EncodedString> getFileContent() {
      return this.fileContent;
   }
}
