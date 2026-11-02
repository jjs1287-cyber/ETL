package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.integration.data.entity.sfdc.ScriptModificationWarning;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;

final class ExternalIdMappingChange implements RequestMappingsModifier {
   private final String name;
   private Logger logger = Logger.getAnonymousLogger();

   public ExternalIdMappingChange(String var1) {
      this.name = var1;
   }

   @Override
   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Override
   public void modifyMapList(List<String> var1) {
      this.removeExistingMapping(var1);
      this.addNewMapping(var1);
   }

   private void removeExistingMapping(List<String> var1) {
      for (String var3 : var1) {
         Mapping var4 = MapStringFormat.parse(var3);
         if (var4.getTargetPath().toString().equals("root$transaction.body$upsert$externalIDFieldName$")) {
            this.logger.fine("Removing existing External ID Name mapping: " + var3);
            var1.remove(var3);
            return;
         }
      }

      this.logger.fine("There was no existing External ID Name mapping");
   }

   private void addNewMapping(List<String> var1) {
      String var2 = "\"" + this.name + "\"";
      String var3 = ScriptModificationWarning.forMapping() + var2;
      String var4 = MapStringFormat.format("root$transaction.body$upsert$externalIDFieldName$", ScriptConstants.wrapInTags(var3));
      this.logger.info("Adding new mapping: " + var4);
      var1.add(var4);
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + " --> " + this.name;
   }
}
