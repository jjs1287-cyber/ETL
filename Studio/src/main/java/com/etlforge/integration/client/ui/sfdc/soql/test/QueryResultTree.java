package com.etlforge.integration.client.ui.sfdc.soql.test;

import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;

final class QueryResultTree implements UiProvider {
   private final QueryResult result;
   private final int recordLimit;
   private final KongaTree tree;

   public QueryResultTree(QueryResult var1, int var2) {
      assert var1.isNested();
      this.result = var1;
      this.recordLimit = var2;
      this.tree = this.createTree();
   }

   private KongaTree createTree() {
      KongaTree var1 = new KongaTree(this.createModel());
      var1.setRootVisible(false);
      var1.setShowsRootHandles(true);

      for (KongaTreeNode var3 : var1.getModel().getRoot().children()) {
         var1.makeNodeExpanded(var3);
      }

      return var1;
   }

   private KongaTreeModel createModel() {
      DefaultKongaTreeNode var1 = new DefaultKongaTreeNode("Root", true);

      for (QueryRecord var3 : this.result.getRecords()) {
         this.buildRecordNode(var3, var1);
      }

      return new DefaultKongaTreeModel(var1);
   }

   private void buildRecordNode(QueryRecord var1, KongaTreeNode var2) {
      DefaultKongaTreeNode var3 = new DefaultKongaTreeNode(var2, var1.getName(), true);
      insertFieldNodes(var3, var1.getFields());

      for (QueryRecord var5 : var1.getChildren()) {
         this.buildRecordNode(var5, var3);
      }
   }

   private static void insertFieldNodes(KongaTreeNode var0, Iterable<Field> var1) {
      for (Field var3 : var1) {
         DefaultKongaTreeNode var4 = new DefaultKongaTreeNode(var3.getName() + " = " + var3.getValueAsString(), false);
         var0.addChild(var4);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.result.getNumberOfRecords() == this.recordLimit) {
         String var2 = "Only the first " + (this.recordLimit == 1 ? "record is" : "first " + this.recordLimit + " records are") + " displayed.";
         CaptionLayout.of(var2, this.tree).decorate(var1);
      } else {
         this.tree.asUiProvider().decorate(var1);
      }
   }

   @Override
   public void requestFocus() {
      this.tree.requestFocusInWindow();
   }
}
