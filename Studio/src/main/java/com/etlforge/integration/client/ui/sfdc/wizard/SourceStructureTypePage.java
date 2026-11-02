package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import org.etlforge.application.ui.wizard.EnumSelectionPage;
import org.etlforge.application.ui.wizard.Wizard;

public final class SourceStructureTypePage extends EnumSelectionPage<RequestSourceType> {
   public SourceStructureTypePage(Wizard var1, Iterable<RequestSourceType> var2) {
      super(var1, "Select File Type", var2);
   }

   public void setDeducedType(Optional<RequestSourceType> var1) {
      if (var1.isPresent() && this.contains(var1.get())) {
         this.setTitle("Confirm File Type");
         this.setSelectedValue(var1.get());
      }
   }
}
