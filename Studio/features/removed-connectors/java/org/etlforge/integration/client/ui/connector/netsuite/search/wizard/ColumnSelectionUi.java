package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;
import org.etlforge.ui.widget.tree.check.CheckBoxTree;

final class ColumnSelectionUi implements UiProvider {
   public static final String TITLE = "Select Columns To Return";
   private final SearchRecordTreeModel model;
   private final CheckBoxTree columnWidget = new CheckBoxTree(Utils.createEmptyTree());
   private final SimpleTreeFilterField columnTreeFilterField = Utils.createCheckBoxFilterField(this.columnWidget.getAdaptee());

   public ColumnSelectionUi(SearchRecordTreeModel var1) {
      this.model = var1;
   }

   public void updateTree() {
      this.columnWidget.setModel(this.model.column);
   }

   public KongaTreeNode[] getCheckedNodes() {
      return this.columnWidget.getCheckedNodes();
   }

   @Override
   public void decorate(UiCanvas var1) {
      new BorderLayoutBuilder(0, 10)
         .withBorder(StyleSheet.getDefaultTitleBorder("Select Columns To Return"))
         .north("(Right-Click to display context menu)")
         .center(this.columnWidget)
         .south(this.columnTreeFilterField)
         .decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.columnWidget.requestFocus();
   }
}
