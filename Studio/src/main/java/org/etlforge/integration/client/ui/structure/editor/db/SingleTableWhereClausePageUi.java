package org.etlforge.integration.client.ui.structure.editor.db;

import java.awt.event.ActionEvent;
import javax.swing.JComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.database.ui.DatabaseColumnsTable;
import org.etlforge.integration.database.BeginEndQuote;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.widget.text.KongaTextArea;

public final class SingleTableWhereClausePageUi implements UiProvider {
   private final KongaTextArea sqlTextArea;
   private final DatabaseColumnsTable columnsTable;
   private final JComponent displayer;
   private BeginEndQuote quotes = BeginEndQuote.BLANK;

   public SingleTableWhereClausePageUi(DatabaseObject var1) {
      this.sqlTextArea = this.createSqlArea();
      this.columnsTable = this.createColumnsTable(var1);
      this.displayer = this.layoutComponents();
   }

   private KongaTextArea createSqlArea() {
      KongaTextArea var1 = new KongaTextArea(4, 30).disableTabs();
      var1.setLabel(PackageResources.SingleTableWhereClausePage.Labels.WHERE_CLAUSE);
      return var1;
   }

   private DatabaseColumnsTable createColumnsTable(DatabaseObject var1) {
      DatabaseColumnsTable var2 = new DatabaseColumnsTable(var1);
      var2.setVisibleRowCount(4);
      var2.setOpaque(false);
      var2.setCaption(PackageResources.SingleTableWhereClausePage.getColumnCaption(var1));
      var2.setDefaultAction(new AbstractSingleObjectSelectionAction<DatabaseColumn>(DatabaseColumn.class) {
         protected void run(DatabaseColumn var1, ActionEvent var2x) {
            SingleTableWhereClausePageUi.this.insertColumnName(var1);
            SingleTableWhereClausePageUi.this.sqlTextArea.requestFocusInWindow();
         }
      });
      return var2;
   }

   private void insertColumnName(DatabaseColumn var1) {
      String var2 = this.quotes.escapeName(var1.getName());
      this.sqlTextArea.replaceSelection(var2);
   }

   public String getWhereClause() {
      return StringUtils.removeStartIgnoreCase(this.sqlTextArea.getText().trim(), "where ");
   }

   public void setWhereClause(String var1) {
      this.sqlTextArea.setText(var1);
   }

   public void setBeginEndQuotes(BeginEndQuote var1) {
      this.quotes = var1 == null ? BeginEndQuote.BLANK : var1;
   }

   public void installUndoRedo(UndoRedoInstaller var1) {
      var1.install(this.sqlTextArea);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.sqlTextArea.requestFocusInWindow();
   }

   private JComponent layoutComponents() {
      return Vertical.column()
         .nonOpaque()
         .withInsets(0, 0, 10, 0)
         .setWeightX(1.0)
         .setWeightY(0.5)
         .setFill(GridBagLayoutPanel.Fill.BOTH)
         .of(this.sqlTextArea.asUiProvider(), this.columnsTable)
         .container();
   }
}
