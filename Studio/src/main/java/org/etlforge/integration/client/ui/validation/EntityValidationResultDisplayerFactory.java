package org.etlforge.integration.client.ui.validation;

import org.etlforge.integration.data.validation.EntityCollectionValidationResult;
import org.etlforge.ui.UiProvider;

public final class EntityValidationResultDisplayerFactory {
   private EntityValidationResultDisplayerFactory() {
   }

   public static UiProvider newListDisplayer(EntityCollectionValidationResult var0) {
      return new EntityValidationResultListDisplayer(var0);
   }

   public static UiProvider newTreeDisplayer(EntityCollectionValidationResult var0) {
      EntityValidationTreeDisplayer var1 = new EntityValidationTreeDisplayer();
      var1.display(var0);
      return var1;
   }
}
