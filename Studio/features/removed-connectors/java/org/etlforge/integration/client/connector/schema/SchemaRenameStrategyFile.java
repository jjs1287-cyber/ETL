package org.etlforge.integration.client.connector.schema;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.io.FilenameUtils;
import org.etlforge.integration.client.connector.serverapi.MultipleSchemas;
import org.etlforge.integration.data.connector.AbstractConnectorCall;
import org.etlforge.integration.data.connector.XsdNamingStrategy;
import org.etlforge.integration.design.util.SchemaRenameStrategy;

public class SchemaRenameStrategyFile implements SchemaRenameStrategy {
   private final Map<String, String> newReferences = Maps.newHashMap();
   private final MultipleSchemas schemas;
   private final MultipleSchemasProcessor.ProcessedFiles processedFiles;
   private final XsdNamingStrategy xsdNamer;
   private final AbstractConnectorCall function;
   private File file;

   public SchemaRenameStrategyFile(AbstractConnectorCall var1, XsdNamingStrategy var2, MultipleSchemas var3, MultipleSchemasProcessor.ProcessedFiles var4) {
      this.schemas = var3;
      this.processedFiles = var4;
      this.function = var1;
      this.xsdNamer = var2;
   }

   @Override
   public String newName(String var1) {
      return this.renameReference(var1);
   }

   private String renameReference(String var1) {
      if (this.processedFiles.isProcessed(var1)) {
         return this.processedFiles.getNewName(var1);
      }

      String var2 = this.hashOfFileName(var1);
      String var3 = this.xsdNamer.createXsdName(this.function, var2);
      this.newReferences.put(var1, var3);
      return var3;
   }

   void setFile(File var1) {
      this.file = var1;
   }

   @Override
   public void checkLocation(String var1, String var2) throws Exception {
      if (Strings.isNullOrEmpty(var2)) {
         throw new Exception("Missing schemaLocation attribute in " + var1 + " element in the file \"" + this.file.getName() + "\".");
      }

      if (!this.schemas.contains(var2)) {
         throw new Exception("The server did not return the referenced schema \"" + var2 + "\" referenced by the file \"" + this.file.getName() + "\".");
      }
   }

   private String hashOfFileName(String var1) {
      String var2 = FilenameUtils.getBaseName(var1);
      HashCode var3 = Hashing.md5().hashString(var2, Charsets.UTF_8);
      return var3.toString();
   }

   public Set<Entry<String, String>> newReferences() {
      return this.newReferences.entrySet();
   }
}
