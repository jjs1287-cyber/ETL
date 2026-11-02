package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.ui.widget.table.TableColumnAdjuster;
import org.etlforge.ui.widget.table.TableColumnMetaData;
import org.etlforge.ui.widget.table.TableEnumEditor;

public abstract class FieldTable extends DocDefTable {
   private static final FieldDataType[] validTypes = FieldDataType.allValueTypes().toArray(new FieldDataType[0]);

   public FieldTable(FieldTableModel var1) {
      super(var1);
      this.getTable().setMaxWidth(FieldTableColumns.TYPE, 150);
      this.setNameOfDeleteAction(RESOURCES.getString("DeleteField"));
   }

   @Override
   protected void setEditors(DocDefTableUi var1) {
      var1.setCellEditorFor(FieldTableColumns.TYPE, new TableEnumEditor(validTypes));
   }

   public final void installFieldValidation(ValidationTableService var1) {
      var1.install(this.getTable());
      TableColumnAdjuster var2 = new TableColumnAdjuster(this.getTable());
      var2.adjustColumns();
   }

   @Override
   protected final TableColumnMetaData getColumnToEditWhenNewRowAdded() {
      return FieldTableColumns.NAME;
   }
}
