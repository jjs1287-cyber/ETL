package com.etlforge.integration.data.download.entity;

import com.google.common.collect.Sets;
import com.etlforge.integration.data.download.InvalidDownloadedDataException;
import com.etlforge.integration.data.download.location.DataLocationFactory;
import java.util.Set;
import org.etlforge.collections.StringKeyValuePairs;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Source;

public class SourceFactory extends CustomEntityFactory<Source> {
   public SourceFactory() {
      super(EntityType.Source, Source.class);
   }

   @Override
   protected String getIdKey() {
      return "source_id";
   }

   @Override
   protected String getNameKey() {
      return "source_name";
   }

   protected void customizeItem(Source var1, StringKeyValuePairs var2) throws InvalidDownloadedDataException {
      super.customizeItem(var1, var2);
      DataLocationFactory var3 = DataLocationFactory.getFactory(var1);
      var1.setLocation(var3.createLocation(var2));
      if (var2.containsKey("salesforce_wizard_object")) {
         var1.setProperty("salesforce_wizard_object", var2.get("salesforce_wizard_object"));
      }
   }

   @Override
   protected Set<String> getRequiredPropertyKeys() {
      return Sets.newHashSet("salesforce_wizard_object");
   }
}
