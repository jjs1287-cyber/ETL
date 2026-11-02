package org.etlforge.integration.client.ui.interchange.entity.document.page.table;

import org.etlforge.integration.client.ui.interchange.entity.document.page.TextDocumentPage;
import org.etlforge.integration.data.structure.text.FieldDataType;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.ui.widget.table.TableColumnMetaData;

public final class FieldTableColumns {
   public static final TableColumnMetaData INDEX = new TableColumnMetaData("#", Void.class, false, 30);
   public static final TableColumnMetaData NAME = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.FieldName"), String.class, true, 225
   );
   public static final TableColumnMetaData TYPE = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.DataType"), FieldDataType.class, true, 100
   );
   public static final TableColumnMetaData DEFAULT = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.Default"), String.class, true, 125
   );
   public static final TableColumnMetaData FORMAT = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.Format"), String.class, true, 125
   );
   public static final TableColumnMetaData BEGIN = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.BeginPos"), Integer.class, true, 80
   );
   public static final TableColumnMetaData LENGTH = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.Length"), Integer.class, true, 80
   );
   public static final TableColumnMetaData END = new TableColumnMetaData(
      TextDocumentPage.getResources().getString("Table.Column.EndPos"), Integer.class, true, 80
   );
   public static final TableColumnMetaData VALIDATION = new TableColumnMetaData("Validation", FieldValidation.class, true, 150);

   private FieldTableColumns() {
   }
}
