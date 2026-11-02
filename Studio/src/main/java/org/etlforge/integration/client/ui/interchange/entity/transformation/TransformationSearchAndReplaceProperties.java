package org.etlforge.integration.client.ui.interchange.entity.transformation;

import org.etlforge.integration.client.entity.search.AbstractSearchableProperty;
import org.etlforge.integration.client.entity.search.SearchAndReplaceMetaData;
import org.etlforge.integration.client.entity.search.StringList;
import org.etlforge.integration.client.entity.search.StringListProperty;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.function.FunctionStructureGetter;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

final class TransformationSearchAndReplaceProperties extends SearchAndReplaceMetaData {
   public TransformationSearchAndReplaceProperties() {
      super(
         new TransformationSearchAndReplaceProperties.MappingsProperty(),
         new TransformationSearchAndReplaceProperties.StructureTypeProperty("Input Type", FunctionStructureGetter.inputGetter()),
         new TransformationSearchAndReplaceProperties.StructureTypeProperty("Output Type", FunctionStructureGetter.inputGetter())
      );
   }

   private static final class MappingsProperty extends StringListProperty {
      public MappingsProperty() {
         super("Mappings");
      }

      public StringList getValue(IntegrationEntity var1) {
         return new StringList(((Transformation)var1).getMapList());
      }
   }

   private static final class StructureTypeProperty extends AbstractSearchableProperty<DataStructureType> {
      private final FunctionStructureGetter<Transformation> getter;

      public StructureTypeProperty(String var1, FunctionStructureGetter<Transformation> var2) {
         super(var1, DataStructureType.class);
         this.getter = var2;
      }

      public final DataStructureType getValue(IntegrationEntity var1) {
         DataStructure var2 = this.getter.getStructure((Transformation)var1);
         return var2 != null ? var2.getStructureType() : null;
      }
   }
}
