package org.etlforge.integration.client.ui.connector.mscrm.query;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import javax.swing.JFrame;
import org.etlforge.system.callback.Receiver;

final class EditAsTextAction extends AbstractEditQueryAction {
   private final String objectName;

   public EditAsTextAction(String var1, Receiver<String> var2) {
      super("In text editor", var2);
      this.objectName = Preconditions.checkNotNull(var1, "objectName");
      this.putValue("ShortDescription", "Edit the fetch XML in a text editor");
   }

   @Override
   protected void edit() {
      JFrame var1 = this.getPage().getWindow().getFrame();
      Optional var2 = QueryTextEditorDialog.editQueryXml(var1, this.objectName, this.getCurrentXml());
      if (var2.isPresent()) {
         this.applyChange((String)var2.get());
      }
   }
}
