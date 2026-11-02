package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import java.util.ResourceBundle;
import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentPage;
import org.etlforge.ui.widget.table.AbstractKongaTableModel;
import org.etlforge.ui.widget.table.DefaultTableRowOrderControl;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableRowOrderControl;

public abstract class DocDefTableModel extends AbstractKongaTableModel {
   protected static final ResourceBundle RESOURCES = TextDocumentPage.getResources();

   public abstract Object createNewObject();

   public DocDefTableModel(TableColumnMetaData... var1) {
      super(var1);
      this.setUndoSupported(true);
   }

   public TableRowOrderControl getRowOrderControl() {
      return new DefaultTableRowOrderControl(this);
   }

   public boolean canRowBeDeleted(int var1) {
      return true;
   }

   @Override
   public void setValueAt(Object var1, int var2, int var3) {
      boolean var4 = !this.isInCompoundEdit();
      if (var4) {
         this.startCompoundEdit();
      }

      super.setValueAt(var1, var2, var3);
      if (var4) {
         this.stopCompoundEdit();
      }
   }
}
