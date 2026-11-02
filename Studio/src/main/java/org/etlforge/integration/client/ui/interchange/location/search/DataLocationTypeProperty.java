package org.etlforge.integration.client.ui.interchange.location.search;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.client.entity.search.NestedProperty;
import org.etlforge.integration.client.entity.search.SearchableEnumProperty;
import org.etlforge.integration.client.entity.search.SearchableProperty;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.metadata.DataLocationMetaData;

public final class DataLocationTypeProperty implements SearchableEnumProperty<DataLocationType>, NestedProperty<DataLocationType> {
   private final SourceTarget st;

   public DataLocationTypeProperty(SourceTarget var1) {
      this.st = var1;
   }

   @Override
   public Set<DataLocationType> getSupportedValues() {
      DataLocationMetaData var1 = DataLocationMetaData.getInstance();
      DataLocationType[] var2;
      switch (this.st) {
         case Source:
            var2 = var1.getSupportedSourceTypes();
            break;
         case Target:
            var2 = var1.getSupportedTargetTypes();
            break;
         default:
            throw new RuntimeException("What sorcery is this? SourceTarget." + this.st + "?!");
      }

      return Sets.newLinkedHashSet(Arrays.asList(var2));
   }

   @Override
   public String getName() {
      return "Type";
   }

   @Override
   public Class<DataLocationType> getValueType() {
      return DataLocationType.class;
   }

   public DataLocationType getValue(IntegrationEntity var1) {
      DataLocation var2 = ((Locatable)var1).getLocation();
      return var2 != null ? var2.getDataLocationType() : null;
   }

   public List<SearchableProperty> getAssociatedProperties(DataLocationType var1) {
      return LocationProperties.getSupportedProperties(var1, this.st);
   }
}
