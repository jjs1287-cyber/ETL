package org.etlforge.integration.client.ui.structure.editor.db.rels;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import org.apache.commons.lang.SystemUtils;
import org.etlforge.application.ui.ask.Answer;
import org.etlforge.application.ui.ask.Ask;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.collections.Pair;
import org.etlforge.integration.client.database.ui.ColumnDisplayOrder;
import org.etlforge.integration.client.database.ui.ColumnDisplayOrderSelector;
import org.etlforge.integration.client.database.ui.resources.DatabaseUiResources;
import org.etlforge.integration.data.structure.database.FieldRelationship;
import org.etlforge.integration.data.structure.database.TableRelationship;
import org.etlforge.integration.database.info.DatabaseColumn;
import org.etlforge.integration.database.info.DatabaseObject;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.MnemonicStringCollection;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.widget.list.AddConnectionAction;
import org.etlforge.ui.widget.list.ConnectedLists;
import org.etlforge.ui.widget.list.ConnectedListsSelection;
import org.etlforge.ui.widget.list.DeleteConnectionAction;
import org.etlforge.ui.widget.list.KongaListModel;
import org.etlforge.ui.widget.list.ListFilter;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.string.StringLimiter;

final class ListLinkKeysDefiner implements LinkKeysDefiner {
   private final TableRelationship relation;
   private final ColumnDisplayOrderSelector displayOrderSelector;
   private final ConnectedLists lists;

   public ListLinkKeysDefiner(TableRelationship var1) {
      this.relation = var1;
      this.displayOrderSelector = new ColumnDisplayOrderSelector();
      this.lists = this.createLists(var1);
      this.listenToDisplayOrderChange();
   }

   private ConnectedLists createLists(TableRelationship var1) {
      ColumnDisplayOrder var2 = this.displayOrderSelector.getSelectedOrder();
      ListModel var3 = createColumnModel(var1.getParentObject(), var2);
      ListModel var4 = createColumnModel(var1.getChildObject(), var2);
      ConnectedLists var5 = new ConnectedLists(var3, var4);
      var5.setLeftRenderer(new ListLinkKeysDefiner.Renderer());
      var5.setRightRenderer(new ListLinkKeysDefiner.Renderer());
      this.setListLabels(var5, var1);
      this.linkExistingKeys(var1, var5);
      this.installFilters(var5);
      return var5;
   }

   private static ListModel createColumnModel(DatabaseObject var0, ColumnDisplayOrder var1) {
      DatabaseColumn[] var2 = var1.getColumns(var0);
      return new KongaListModel(var2);
   }

   private void setListLabels(ConnectedLists var1, TableRelationship var2) {
      MnemonicStringCollection var3 = new MnemonicStringCollection();
      var3.markCharactersAsUsed('L', 'R');
      String var4 = getNameForLabel(var2.getParentObject());
      MnemonicString var5 = var3.autoTranslate(var4);
      String var6 = getNameForLabel(var2.getChildObject());
      MnemonicString var7 = var3.autoTranslate(var6);
      var1.setLabels(var5.getRaw(), var7.getRaw());
      var1.setLabelStyle(WizardStyleSheet.SMALL_CAPTION_STYLE);
   }

   private static String getNameForLabel(DatabaseObject var0) {
      StringLimiter var1 = new StringLimiter(var0.getName());
      return var1.getLimitedString(40);
   }

   private void linkExistingKeys(TableRelationship var1, ConnectedLists var2) {
      for (FieldRelationship var4 : var1.getFieldRelationships()) {
         DatabaseColumn var5 = var4.getParentColumn();
         DatabaseColumn var6 = var4.getChildColumn();
         if (var2.canConnect(var5, var6)) {
            var2.addConnection(var5, var6);
         }
      }
   }

   private void installFilters(ConnectedLists var1) {
      Function var2 = new Function<String, ListFilter>() {
         public ListFilter apply(String var1) {
            return new ListLinkKeysDefiner.NameFilter(var1);
         }
      };
      var1.installFilterFields(var2, var2);
   }

   private void listenToDisplayOrderChange() {
      this.displayOrderSelector.setCallback(new Receiver<ColumnDisplayOrder>() {
         public void handle(ColumnDisplayOrder var1) {
            ListModel var2 = ListLinkKeysDefiner.createColumnModel(ListLinkKeysDefiner.this.relation.getParentObject(), var1);
            ListModel var3 = ListLinkKeysDefiner.createColumnModel(ListLinkKeysDefiner.this.relation.getChildObject(), var1);
            ListLinkKeysDefiner.this.lists.setListModels(var2, var3, true);
         }
      });
   }

   @Override
   public void decorate(UiCanvas var1) {
      String var2 = SystemUtils.IS_OS_MAC
         ? "Select a parent and a child column and click the Link button to link them."
         : "Drag and drop a parent column to a child column to link them.";
      Vertical.borderLayout(5).of(var2, this.lists, this.layoutControls()).decorate(var1);
   }

   private JComponent layoutControls() {
      Grid var1 = Grid.oneRow(10).of(new AddConnectionAction("&Link", this.lists), new DeleteConnectionAction("&Remove Link", this.lists));
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.add(InvisiblePanel.of(var1)).glue().add(this.displayOrderSelector);
      return var2.container();
   }

   @Override
   public void requestFocus() {
      this.lists.requestFocus();
   }

   @Override
   public boolean checkInput() {
      ConnectedListsSelection var1 = this.lists.getSelection();
      if (var1.canConnect()) {
         DatabaseColumn var2 = (DatabaseColumn)var1.getLeftObject();
         DatabaseColumn var3 = (DatabaseColumn)var1.getRightObject();
         String var4 = String.format("The columns %s and %s are selected, but not joined.\n\nDo you want to join them?", var2.getName(), var3.getName());
         Answer var5 = Ask.yesNoOrCancel(var4, "Join Selected Columns?");
         if (var5.isCancel()) {
            return false;
         }

         if (var5.isYes()) {
            this.lists.addConnection(var2, var3);
         }
      }

      return true;
   }

   @Override
   public void apply() {
      ArrayList var1 = Lists.newArrayList();

      for (Pair var4 : this.lists.getConnections()) {
         DatabaseColumn var5 = (DatabaseColumn)var4.first;
         DatabaseColumn var6 = (DatabaseColumn)var4.second;
         var1.add(new FieldRelationship(var5, var6));
      }

      this.relation.setFieldRelationships(var1);
   }

   private static class NameFilter implements ListFilter {
      private final String string;

      public NameFilter(String var1) {
         this.string = var1.trim().toLowerCase();
      }

      @Override
      public boolean test(Object var1) {
         if (this.string.isEmpty()) {
            return true;
         } else if (var1 instanceof DatabaseColumn) {
            DatabaseColumn var2 = (DatabaseColumn)var1;
            return var2.getName().toLowerCase().startsWith(this.string);
         } else {
            return true;
         }
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         DatabaseColumn var6 = (DatabaseColumn)var2;
         this.setText(var6.getName());
         if (var6.isPrimaryKey()) {
            this.setIcon(DatabaseUiResources.PRIMARY_KEY_ICON);
         } else {
            this.setIcon(null);
         }

         return this;
      }
   }
}
