package com.etlforge.integration.client.ui.sfdc.soql.test;

import java.util.Arrays;
import org.jdesktop.swingx.treetable.AbstractMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.etlforge.integration.salesforce.query.Field;
import org.etlforge.integration.salesforce.query.QueryRecord;
import org.etlforge.integration.salesforce.query.QueryResult;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.KongaTreeTable;

final class QueryResultTreeTable implements UiProvider {
   private final QueryResult result;
   private final int recordLimit;
   private final KongaTreeTable treeTable;

   public QueryResultTreeTable(QueryResult var1, int var2) {
      assert var1.isNested();
      this.result = var1;
      this.recordLimit = var2;
      this.treeTable = this.createTreeTable();
   }

   private KongaTreeTable createTreeTable() {
      TreeTableModel var1 = this.createModel();
      KongaTreeTable var2 = new KongaTreeTable(var1);
      var2.setRootVisible(false);
      var2.setShowsRootHandles(true);
      var2.setLeafIcon(null);
      var2.setFixedColumnWidth(0, 200);
      var2.setFixedColumnWidth(1, 200);

      for (int var3 = var1.getChildCount(var1.getRoot()); var3 >= 0; var3--) {
         var2.expandRow(var3);
      }

      return var2;
   }

   private TreeTableModel createModel() {
      DefaultMutableTreeTableNode var1 = new DefaultMutableTreeTableNode();

      for (QueryRecord var3 : this.result.getRecords()) {
         this.buildRecordNode(var3, var1);
      }

      return new DefaultTreeTableModel(var1, Arrays.asList("Object", "Field", "Value"));
   }

   private void buildRecordNode(QueryRecord var1, AbstractMutableTreeTableNode var2) {
      QueryResultTreeTable.ObjectNode var3 = new QueryResultTreeTable.ObjectNode(var1.getName());
      var2.add(var3);
      insertFieldNodes(var3, var1.getFields());

      for (QueryRecord var5 : var1.getChildren()) {
         this.buildRecordNode(var5, var3);
      }
   }

   private static void insertFieldNodes(QueryResultTreeTable.ObjectNode var0, Iterable<Field> var1) {
      for (Field var3 : var1) {
         QueryResultTreeTable.FieldNode var4 = new QueryResultTreeTable.FieldNode(var3);
         var0.add(var4);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.result.getNumberOfRecords() == this.recordLimit) {
         String var2 = "Only the first " + (this.recordLimit == 1 ? "record is" : "first " + this.recordLimit + " records are") + " displayed.";
         CaptionLayout.of(var2, this.treeTable).decorate(var1);
      } else {
         var1.display(new KongaScrollPane(this.treeTable));
      }
   }

   @Override
   public void requestFocus() {
      this.treeTable.requestFocusInWindow();
   }

   private static class FieldNode extends AbstractMutableTreeTableNode {
      public FieldNode(Field var1) {
         super(var1, false);
      }

      public Field getUserObject() {
         return (Field)super.getUserObject();
      }

      @Override
      public Object getValueAt(int var1) {
         switch (var1) {
            case 0:
               return "";
            case 1:
               return this.getUserObject().getName();
            case 2:
               return this.getUserObject().getValueAsString();
            default:
               throw new IndexOutOfBoundsException("Index: " + var1);
         }
      }

      @Override
      public int getColumnCount() {
         return 3;
      }
   }

   private static class ObjectNode extends AbstractMutableTreeTableNode {
      public ObjectNode(String var1) {
         super(var1, true);
      }

      @Override
      public Object getValueAt(int var1) {
         switch (var1) {
            case 0:
               return this.getUserObject();
            default:
               throw new IndexOutOfBoundsException("Index: " + var1);
         }
      }

      @Override
      public boolean isLeaf() {
         return false;
      }

      @Override
      public int getColumnCount() {
         return 1;
      }
   }
}
