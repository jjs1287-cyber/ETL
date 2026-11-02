package org.etlforge.integration.data.entity.target;

import org.etlforge.integration.data.entity.ReferenceReplacementService;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.id.TargetId;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.HttpLocation;

public final class TargetReferencesReplacer {
   private final ReferenceReplacementService service;

   public TargetReferencesReplacer(ReferenceReplacementService var1) {
      this.service = var1;
   }

   public void replaceReferences(Target var1) {
      DataLocation var2 = var1.getLocation();
      if (var2 instanceof HttpLocation) {
         this.replaceHttpResponseTarget((HttpLocation)var2);
      }
   }

   private void replaceHttpResponseTarget(HttpLocation var1) {
      TargetId var2 = var1.getTargetIdForResponse();
      if (var2 != null) {
         Target var3 = this.service.getReplacement(var2, Target.class);
         if (var3 != null) {
            var1.setTargetForResponse(var3.getID());
         }
      }
   }
}
