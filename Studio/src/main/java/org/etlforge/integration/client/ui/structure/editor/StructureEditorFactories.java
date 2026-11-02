package org.etlforge.integration.client.ui.structure.editor;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructureType;

public final class StructureEditorFactories {
   private final ImmutableMap<DataStructureType, StructureEditorFactory> factories;

   public StructureEditorFactories(Map<DataStructureType, StructureEditorFactory> var1, Map<DataStructureType, StructureEditorFactory> var2) {
      Builder var3 = ImmutableMap.builder();
      var3.putAll(var1);
      var3.putAll(var2);
      this.factories = var3.build();
   }

   public StructureEditorFactory getFactory(DataStructure var1) {
      return this.factories.get(var1.getStructureType());
   }

   @Override
   public String toString() {
      ToStringHelper var1 = Objects.toStringHelper(this);

      for (Entry var3 : this.factories.entrySet()) {
         var1.add(((DataStructureType)var3.getKey()).name(), ((StructureEditorFactory)var3.getValue()).getClass().getName());
      }

      return var1.toString();
   }
}
