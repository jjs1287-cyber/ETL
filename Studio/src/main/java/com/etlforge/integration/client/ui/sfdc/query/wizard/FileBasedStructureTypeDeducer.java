package com.etlforge.integration.client.ui.sfdc.query.wizard;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.integration.data.location.file.TargetFileName;

final class FileBasedStructureTypeDeducer {
   public static Optional<QueryTargetType> deduceType(Target var0, QueryTargetConfig var1) {
      DataLocation var2 = var0.getLocation();
      ImmutableSet var3 = var1.getStructureTypes(var2.getDataLocationType());
      if (var2 instanceof FileLocation) {
         TargetFileName var4 = ((FileLocation)var2).getTargetFileName();
         Optional var5 = fromFileName(var4);
         if (var5.isPresent() && var3.contains(var5.get())) {
            return var5;
         }
      } else if (var2 instanceof HttpLocation) {
      }

      return Optional.absent();
   }

   private static Optional<QueryTargetType> fromFileName(TargetFileName var0) {
      if (var0.hasTextExtension()) {
         return Optional.of(QueryTargetType.TEXT);
      } else if (var0.hasXmlExtension()) {
         return Optional.of(QueryTargetType.XML);
      } else {
         return var0.hasJsonExtension() ? Optional.of(QueryTargetType.JSON) : Optional.absent();
      }
   }

   private FileBasedStructureTypeDeducer() {
   }
}
