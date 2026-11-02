package org.etlforge.integration.mscrm.metadata;

import com.google.common.collect.ImmutableMap;
import org.etlforge.util.name.Nameables;

public final class MSCrmObjectListLookup implements MSCrmObjectLookup {
   private final ImmutableMap<String, MSCrmObject> objects;

   public MSCrmObjectListLookup(Iterable<MSCrmObject> var1) {
      this.objects = Nameables.toImmutableMap(var1);
   }

   @Override
   public MSCrmObject describeObject(String var1) throws MSCrmMetaDataException {
      MSCrmObject var2 = this.objects.get(var1);
      if (var2 == null) {
         throw new MSCrmMetaDataException("Unknown object: " + var1);
      } else {
         return var2;
      }
   }
}
