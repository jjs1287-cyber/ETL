package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JComponent;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field.FilterList;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field.StaticFilterEntryUi;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.split.KongaSplitPane;
import org.etlforge.ui.widget.tree.KongaTree;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.ui.widget.tree.SimpleTreeFilterField;
import org.etlforge.ui.widget.tree.event.KongaTreeAdapter;
import org.etlforge.ui.widget.tree.event.KongaTreeEvent;

final class CriteriaDefinitionUi implements UiProvider {
   public static final String TITLE = "Define Filter Criteria";
   public static final String FIELDTITLE = "Specify criteria to narrow down your search";
   private final KongaTree fieldSelectionTree;
   private final SimpleTreeFilterField fieldSelectionFilterField;
   private final FilterList filterList;
   private final StaticFilterEntryUi filterEntryUi;
   private final SearchRecordTreeModel recordTree;

   public CriteriaDefinitionUi(QueryBuilderModel var1) {
      this.recordTree = var1.getRecordTreeModel();
      this.fieldSelectionTree = Utils.createEmptyTree();
      this.fieldSelectionTree.asSelectionSource().setDefaultAction(new AbstractSelectionAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            Action var2 = CriteriaDefinitionUi.this.filterEntryUi.getAddFilterAction();
            if (var2.isEnabled()) {
               var2.actionPerformed(var1);
            }
         }
      });
      this.fieldSelectionFilterField = Utils.createFilterField(this.fieldSelectionTree);
      this.filterList = new FilterList(var1);
      this.filterEntryUi = new StaticFilterEntryUi(var1);
      this.fieldSelectionTree.addKongaTreeListener(new CriteriaDefinitionUi.CriteriaSelection(var1));
   }

   public void updateTree() {
      this.fieldSelectionTree.setModel(this.recordTree.criteria);
      if (this.recordTree.criteria != null && this.recordTree.isBasicSearch()) {
         this.fieldSelectionTree.expandAll();
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      CriteriaDefinitionUi.Layout var2 = new CriteriaDefinitionUi.Layout();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.fieldSelectionTree.requestFocus();
   }

   private static class CriteriaSelection extends KongaTreeAdapter {
      private final QueryBuilderModel model;

      public CriteriaSelection(QueryBuilderModel var1) {
         this.model = var1;
      }

      @Override
      public void selectionChanged(KongaTreeEvent var1) {
         KongaTreeNode var2 = var1.getSourceNode();
         if (var2 != null) {
            if (var2.isLeaf()) {
               this.model.setSelected(var2.getParent().getDisplayName(), var2.getDisplayName());
            }
         }
      }
   }

   private class Layout {
      private final KongaSplitPane topLevelSplit = KongaSplitPane.horizontalSplit(this.layoutLeftHandSide(), this.layoutRightHandSide());

      public Layout() {
         this.topLevelSplit.setPreferredSize(new Dimension(500, 500));
         this.topLevelSplit.setLeftMinimumSize(new Dimension(300, 200));
         this.topLevelSplit.setRightMinimumSize(new Dimension(200, 200));
         this.topLevelSplit.setResizeWeight(0.5);
         this.topLevelSplit.setProportionalDividerLocation(0.4);
         UiUtils.setOpaqueRecursivelySkipDefaults(this.topLevelSplit, false);
      }

      private UiProvider layoutLeftHandSide() {
         return new BorderLayoutBuilder(0, 10)
            .withBorder(StyleSheet.getDefaultTitleBorder("Specify criteria to narrow down your search"))
            .north("<html>Double-click on a field to enter a condition for it,<br>or click the Add Condition button to the left.</html>")
            .center(CriteriaDefinitionUi.this.fieldSelectionTree.asUiProvider())
            .south(CriteriaDefinitionUi.this.fieldSelectionFilterField);
      }

      private UiProvider layoutRightHandSide() {
         JComponent var1 = new BorderLayoutBuilder()
            .withBorder(StyleSheet.getDefaultTitleBorder("Add Condition"))
            .center(CriteriaDefinitionUi.this.filterEntryUi)
            .container();
         JComponent var2 = new BorderLayoutBuilder()
            .withBorder(StyleSheet.getDefaultTitleBorder("Current Conditions"))
            .center(CriteriaDefinitionUi.this.filterList)
            .container();
         return new BorderLayoutBuilder(0, 16).north(var1).center(var2);
      }

      public void decorate(UiCanvas var1) {
         var1.display(this.topLevelSplit);
      }
   }
}
