package org.etlforge.integration.client.mapping;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructureException;

public final class MappingStructureFactories implements MappingStructureFactory {
   private final ImmutableMap<Class<? extends DataStructure>, MappingStructureFactory> factories;

   public MappingStructureFactories(Iterable<MappingStructureFactory> var1, Iterable<MappingStructureFactory> var2) {
      Builder var3 = ImmutableMap.builder();
      populateMap(var1, var3);
      populateMap(var2, var3);
      this.factories = var3.build();
   }

   private static void populateMap(Iterable<MappingStructureFactory> var0, Builder<Class<? extends DataStructure>, MappingStructureFactory> var1) {
      for (MappingStructureFactory var3 : var0) {
         for (Class var5 : var3.getSupportedStructures()) {
            var1.put(var5, var3);
         }
      }
   }

   @Override
   public Iterable<? extends Class<? extends DataStructure>> getSupportedStructures() {
      return this.factories.keySet();
   }

   @Nullable
   @Override
   public MappingTreeStructure getInputStructure(Transformation var1) throws MappingTreeStructureException {
      MappingStructureFactory var2 = this.factories.get(var1.getInputStructure().getClass());
      return var2.getInputStructure(var1);
   }

   @Nullable
   @Override
   public MappingTreeStructure getOutputStructure(Transformation var1) throws MappingTreeStructureException {
      MappingStructureFactory var2 = this.factories.get(var1.getOutputStructure().getClass());
      return var2.getOutputStructure(var1);
   }

   public MappingStructureFactory getFactory(DataStructure var1) {
      Class var2 = var1.getClass();
      MappingStructureFactory var3 = this.factories.get(var2);
      if (var3 == null) {
         var3 = this.lookForSuperClass(var1);
      }

      if (var3 == null) {
         throw new RuntimeException("Cannot create a mapping structure for type " + var2.getName() + ".");
      } else {
         return var3;
      }
   }

   private MappingStructureFactory lookForSuperClass(DataStructure var1) {
      for (Entry var3 : this.factories.entrySet()) {
         if (((Class)var3.getKey()).isInstance(var1)) {
            return (MappingStructureFactory)var3.getValue();
         }
      }

      return null;
   }
}
