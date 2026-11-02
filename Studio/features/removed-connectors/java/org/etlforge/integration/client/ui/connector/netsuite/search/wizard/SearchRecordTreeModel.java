package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasic;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchBasicRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.NetSuiteSearchRecord;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchObjectValueRow;
import org.etlforge.integration.client.ui.connector.netsuite.search.record.value.NetSuiteSearchRecordValue;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeModel;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.check.AllNodesVisitor;
import org.etlforge.ui.widget.tree.check.CheckBoxNode;
import org.etlforge.ui.widget.tree.check.CheckBoxTree;
import org.etlforge.ui.widget.tree.check.DefaultCheckBoxNode;
import org.etlforge.util.AbstractPropertyBasedModel;
import org.etlforge.util.Property;

public class SearchRecordTreeModel extends AbstractPropertyBasedModel {
   public static final String CHANGEMODEL = "ChangeModel";
   private final Property<Integer> modelChanged = this.newProperty("ChangeModel");
   KongaTreeModel column = null;
   KongaTreeModel criteria = null;
   private NetSuiteSearchRecord record = null;
   private NetSuiteSearchCategory category = null;
   private String basicObjectDisplayName = null;

   public SearchRecordTreeModel() {
      this.modelChanged.set(0);
   }

   public void setModel(NetSuiteSearchRecord var1, NetSuiteSearchCategory var2) {
      if (var1 != null && var2 != null) {
         if (this.record != var1 || this.category != var2) {
            this.record = var1;
            this.category = var2;
            this.basicObjectDisplayName = var1.getNetSuiteConnectorObject().getName();
            this.changeModelImpl();
            this.modelChanged.set(this.modelChanged.get() + 1);
         }
      }
   }

   public void setSelectedColumnNodes(Map<String, NetSuiteSearchObjectValueRow> var1) {
      if (!var1.isEmpty()) {
         List var2 = this.getSelectedColumnNodes(var1);
         CheckBoxNode[] var3 = (CheckBoxNode[]) var2.toArray(new CheckBoxNode[0]);
         CheckBoxTree.setCheckedNodesOnModel(this.column, var3);
      }
   }

   public void updateSelectedColumns(NetSuiteSearchRecordValue var1) {
      if (this.column != null) {
         KongaTreeNode[] var2 = CheckBoxTree.getCheckedNodes(this.column);
         this.updateSelectedColumns(var2, var1);
      }
   }

   public String getObjectDisplayName(String var1) {
      return this.record.isBasicObjectKey(var1) ? this.basicObjectDisplayName : var1;
   }

   public String getInternalName(String var1) {
      return var1.equals(this.basicObjectDisplayName) ? this.record.getBasicObjectKey() : var1;
   }

   private void changeModelImpl() {
      this.column = null;
      this.criteria = null;
      if (this.record != null && this.category != null && !this.category.isSavedSearch()) {
         this.changeModelImplCriteria();
         this.changeModelImplColumns();
      }
   }

   boolean isBasicSearch() {
      return this.category == null || this.category.isSearchBasic();
   }

   private void changeModelImplCriteria() {
      List var1 = this.record.getNetSuiteSearchBasicKeys();
      if (!var1.isEmpty()) {
         if (this.isBasicSearch()) {
            String var2 = this.record.getBasicObjectKey();
            String var3 = this.getObjectDisplayName(var2);
            DefaultKongaTreeNode var4 = new DefaultKongaTreeNode(var3, true);
            this.criteria = new DefaultKongaTreeModel(var4);
            DefaultKongaTreeNode var5 = new DefaultKongaTreeNode(var4, var3, true);
            NetSuiteSearchBasic var6 = this.record.getNetSuiteSearchBasic(var2);
            this.addFields(var5, var6.getFieldNames());
         } else {
            DefaultKongaTreeNode var8 = new DefaultKongaTreeNode("root", true);
            this.criteria = new DefaultKongaTreeModel(var8);

            for (Object __etlItem103 : var1) {
               String var10 = (String) __etlItem103;
               String var11 = this.getObjectDisplayName(var10);
               DefaultKongaTreeNode var12 = new DefaultKongaTreeNode(var8, var11, true);
               NetSuiteSearchBasic var7 = this.record.getNetSuiteSearchBasic(var10);
               this.addFields(var12, var7.getFieldNames());
            }
         }
      }
   }

   private void changeModelImplColumns() {
      if (this.category.isAdvancedSearch()) {
         List var1 = this.record.getNetSuiteSearchBasicRowKeys();
         if (!var1.isEmpty()) {
            DefaultKongaTreeNode var2 = new DefaultKongaTreeNode("root", true);
            this.column = new DefaultKongaTreeModel(var2);

            for (Object __etlItem120 : var1) {
               String var4 = (String) __etlItem120;
               String var5 = this.getObjectDisplayName(var4);
               DefaultKongaTreeNode var6 = new DefaultKongaTreeNode(var2, var5, true);
               NetSuiteSearchBasicRow var7 = this.record.getNetSuiteSearchBasicRow(var4);
               this.addFields(var6, var7.getFieldNames());
            }
         }
      }
   }

   private void addFields(KongaTreeNode var1, List<String> var2) {
      for (String var4 : var2) {
         new DefaultCheckBoxNode(var1, var4, false);
      }
   }

   private Map<String, CheckBoxNode> asColumnCheckBoxNodes() {
      final HashMap var1 = Maps.newHashMap();
      if (this.column == null) {
         return var1;
      }

      CheckBoxTree.visitAllCheckBoxNodesNoFilter(this.column, new AllNodesVisitor() {
         @Override
         public void visit(CheckBoxNode var1x) {
            if (var1x.isLeaf()) {
               String var2 = SearchRecordTreeModel.getKey(var1x.getParent().getDisplayName(), var1x.getDisplayName());
               var1.put(var2, var1x);
            }
         }
      });
      return var1;
   }

   private static String getKey(String var0, String var1) {
      return var0 + "." + var1;
   }

   private List<CheckBoxNode> getSelectedColumnNodes(Map<String, NetSuiteSearchObjectValueRow> var1) {
      ArrayList var2 = Lists.newArrayList();
      if (this.column == null) {
         return var2;
      }

      Map var3 = this.asColumnCheckBoxNodes();

      for (Entry var5 : var1.entrySet()) {
         String var6 = this.getObjectDisplayName((String)var5.getKey());

         for (String var8 : ((NetSuiteSearchObjectValueRow)var5.getValue()).getFilters()) {
            String var9 = getKey(var6, var8);
            CheckBoxNode var10 = (CheckBoxNode)var3.get(var9);
            if (var10 == null) {
               throw new RuntimeException("Fatal error: no column node for " + var9);
            }

            var2.add(var10);
         }
      }

      return var2;
   }

   private void updateSelectedColumns(KongaTreeNode[] var1, NetSuiteSearchRecordValue var2) {
      for (KongaTreeNode var6 : var1) {
         if (var6.isLeaf()) {
            String var7 = var6.getParent().getDisplayName();
            String var8 = var6.getDisplayName();
            var2.putRow(this.getInternalName(var7), var8);
         }
      }
   }
}
