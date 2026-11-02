package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.salesforce.DateConversionParams;
import org.etlforge.integration.structure.mapping.MapStringFormat;
import org.etlforge.integration.structure.mapping.Mapping;
import org.etlforge.integration.structure.mapping.NodePath;

final class DateOptionsChange implements RequestMappingsModifier {
   private final Optional<DateConversionParams> oldParams;
   private final Optional<DateConversionParams> newParams;
   private final ImmutableList<DateFieldsRetriever> fieldsRetrievers;
   private Logger logger = Logger.getAnonymousLogger();

   public DateOptionsChange(Optional<DateConversionParams> var1, Optional<DateConversionParams> var2, List<DateFieldsRetriever> var3) {
      Preconditions.checkArgument(!var1.equals(var2), "There is no change!");
      this.oldParams = var1;
      this.newParams = var2;
      this.fieldsRetrievers = ImmutableList.copyOf(var3);
   }

   @Override
   public void setLogger(Logger var1) {
      this.logger = Preconditions.checkNotNull(var1);
   }

   @Override
   public void modifyMapList(List<String> var1) {
      DateFields var2 = this.getDateFields();
      if (var2.isEmpty()) {
         this.logger.info("There are no date or dateTime fields to update.");
      } else {
         DateOptionsChange.MappingsReplacer var3 = new DateOptionsChange.MappingsReplacer(var2);
         var3.replace(var1);
      }
   }

   private DateFields getDateFields() {
      for (DateFieldsRetriever var2 : this.fieldsRetrievers) {
         this.logger.info("Asking " + var2.getClass().getName() + " for a list of date and dateTime fields");

         try {
            Optional var3 = var2.getDateFields();
            if (var3.isPresent()) {
               DateFields var4 = (DateFields)var3.get();
               var4.reportTo(this.logger);
               return var4;
            }
         } catch (Exception var5) {
            this.logger.throwing(this.getClass().getName(), "getDateFields", var5);
            ErrorLog.log(this, null, var5);
         }
      }

      this.logger.severe("None of the DateFieldsRetrievers could provide a list of date fields.");
      return DateFields.empty();
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName() + " --> " + (this.newParams.isPresent() ? this.newParams.get() : "No conversion needed");
   }

   private class MappingsReplacer {
      private final DateFields dateFields;
      private final List<DateMapping> userEditedMappings = Lists.newArrayList();
      private final List<DateMapping> failedReplacements = Lists.newArrayList();
      private final List<Mapping> newMappings = Lists.newArrayList();
      private int numberOfDateMappings;

      public MappingsReplacer(DateFields var2) {
         this.dateFields = var2;
      }

      public void replace(List<String> var1) {
         Iterator var2 = var1.iterator();

         while (var2.hasNext()) {
            String var3 = (String)var2.next();
            Mapping var4 = MapStringFormat.parse(var3);
            DateMapping var5 = this.getDateMappingToReplace(var4);
            if (var5 != null) {
               this.numberOfDateMappings++;
               if (var5.isUserEdited()) {
                  this.recordUserEditedMapping(var4, var5);
               } else {
                  Mapping var6 = var5.generateNewMapping();
                  if (var6 == null) {
                     this.recordFailedReplacement(var4, var5);
                  } else {
                     this.replaceMapping(var2, var3, var6);
                  }
               }
            }
         }

         this.applyResult(var1);
      }

      private DateMapping getDateMappingToReplace(Mapping var1) {
         NodePath var2 = var1.getTargetPath();
         if (this.dateFields.isDateField(var2)) {
            return DateMapping.forDate(var1, DateOptionsChange.this.oldParams, DateOptionsChange.this.newParams);
         } else {
            return this.dateFields.isDateTimeField(var2)
               ? DateMapping.forDateTime(var1, DateOptionsChange.this.oldParams, DateOptionsChange.this.newParams)
               : null;
         }
      }

      private void recordUserEditedMapping(Mapping var1, DateMapping var2) {
         DateOptionsChange.this.logger.info("The mapping of target node " + var1.getTargetPath() + " has been edited by the user and cannot be replaced.");
         this.userEditedMappings.add(var2);
      }

      private void recordFailedReplacement(Mapping var1, DateMapping var2) {
         DateOptionsChange.this.logger.info("The mapping of target node " + var1.getTargetPath() + " could not be parsed, and cannot be replaced.");
         this.failedReplacements.add(var2);
      }

      private void replaceMapping(Iterator<String> var1, String var2, Mapping var3) {
         DateOptionsChange.this.logger.fine("Removing existing mapping: " + var2);
         this.newMappings.add(var3);
         var1.remove();
      }

      private void applyResult(List<String> var1) {
         if (!this.userEditedMappings.isEmpty() || !this.failedReplacements.isEmpty()) {
            this.showStaleMappingsWarning();
         }

         if (this.numberOfDateMappings == 0) {
            DateOptionsChange.this.logger.info("There were no date mappings to replace.");
         } else {
            this.addBackNewMappings(var1);
         }
      }

      private void showStaleMappingsWarning() {
         StaleDateMappingsWarning var1 = new StaleDateMappingsWarning(this.userEditedMappings, this.failedReplacements);
         var1.show();
      }

      private void addBackNewMappings(List<String> var1) {
         List var2 = KongaListUtils.transform(this.newMappings, MapStringFormat.MAPPING_TO_STRING);
         DateOptionsChange.this.logger.info("Adding new mappings: " + Joiner.on('\n').join(var2));
         var1.addAll(var2);
      }
   }
}
