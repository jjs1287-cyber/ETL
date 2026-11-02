package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.property.ActionEnabledBinding;
import org.etlforge.ui.property.Binding;

final class CopyAction extends AbstractAction {
   private final FieldValidationModel model;

   public CopyAction(FieldValidationModel var1) {
      super("&Copy to clipboard");
      this.model = var1;
      this.putValue("ShortDescription", "Copies the defined validation rules to the clipboard");
      Binding var2 = ActionEnabledBinding.forBoolean(this, var1, "enabled");
      var2.syncUi();
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            CopyAction.this.copyToClipboard();
         }
      });
   }

   private void copyToClipboard() {
      Optional var1 = this.model.getFieldValidation();
      if (var1.isPresent()) {
         ClipboardSerializer.toClipboard((FieldValidation)var1.get());
      }
   }
}
