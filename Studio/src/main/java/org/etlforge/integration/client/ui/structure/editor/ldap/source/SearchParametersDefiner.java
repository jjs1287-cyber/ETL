package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import org.etlforge.integration.client.ldap.ui.AttributeViewer;
import org.etlforge.integration.client.ldap.ui.LdapStructureEntryTree;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.ldap.LdapSearchParameters;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.selection.AbstractSelectionAction;
import org.etlforge.ui.widget.tree.KongaTree;

final class SearchParametersDefiner implements UiProvider {
   private final LdapStructureEntryNode entryNode;
   private final AttributeViewer attributeViewer;
   private final BaseNameField pathField;
   private final FilterExpressionArea filterArea;
   private final ScopeDefiner scopeDefiner;

   public SearchParametersDefiner(LdapStructureEntryNode var1, String var2) {
      this.entryNode = var1;
      this.attributeViewer = this.createTree();
      this.pathField = new BaseNameField(var2);
      this.filterArea = new FilterExpressionArea(var1);
      this.scopeDefiner = new ScopeDefiner();
   }

   public void applyTo(LdapStructure var1) {
      NumericOid var2 = this.entryNode.getEntry().getOid();
      var1.addSearchParameters(var2, this.getSearchParameters());
   }

   public String getClassName() {
      return this.entryNode.getName();
   }

   public void setSourceBasePath(String var1) {
      this.pathField.setNewBasePath(var1);
   }

   public void setSearchParameters(LdapSearchParameters var1) {
      if (!var1.getClassName().equals(this.getClassName())) {
         throw new IllegalArgumentException(
            "This UI displays the search parameters for class \""
               + this.getClassName()
               + "\" - received search parameters for class \""
               + var1.getClassName()
               + "\""
         );
      }

      this.pathField.setPath(var1.getBaseDN());
      String var2 = var1.getFilter();
      if (var2.length() == 0) {
         var2 = SearchParamUtils.getDefaultFilter(this.entryNode);
      }

      this.filterArea.setExpression(var2);
      this.scopeDefiner.setScope(var1.getScope());
   }

   public LdapSearchParameters getSearchParameters() {
      String var1 = this.pathField.getPath();
      String var2 = this.filterArea.getExpression().trim();
      return new LdapSearchParameters(this.getClassName(), var1, var2, this.scopeDefiner.getScope());
   }

   @Override
   public void decorate(UiCanvas var1) {
      new SearchParametersDefiner.Layout().decorate(var1);
   }

   @Override
   public void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SearchParametersDefiner.this.pathField.requestFocus();
         }
      });
   }

   private LdapStructureEntryTree createTree() {
      LdapStructureEntryTree var1 = new LdapStructureEntryTree(this.entryNode, false);
      var1.setDragEnabled();
      var1.setRootVisible(true);
      var1.setSelectionMode(KongaTree.SelectionMode.SINGLE_TREE_SELECTION);
      var1.asSelectionSource().setDefaultAction(new SearchParametersDefiner.TreeDoubleClickAction(var1));
      return var1;
   }

   private class Layout {
      private final BorderLayoutBuilder container = new BorderLayoutBuilder(10, 20);

      public Layout() {
         this.layoutTree();
         this.layoutOtherControls();
      }

      public void decorate(UiCanvas var1) {
         this.container.decorate(var1);
      }

      private void layoutTree() {
         String var1 = PackageResources.SearchParamsDefiner.ATTRIBUTES;
         this.container.west(CaptionLayout.of(var1, SearchParametersDefiner.this.attributeViewer));
      }

      private void layoutOtherControls() {
         this.container
            .northToSouth(SearchParametersDefiner.this.pathField, SearchParametersDefiner.this.filterArea, SearchParametersDefiner.this.scopeDefiner);
      }
   }

   private class TreeDoubleClickAction extends AbstractSelectionAction {
      private final AttributeViewer viewer;

      public TreeDoubleClickAction(AttributeViewer var2) {
         this.viewer = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         LdapStructureAttribute[] var2 = this.viewer.getSelectedAttributes();
         if (var2.length == 1) {
            this.insertAttributeIntoFilter(var2[0]);
         }
      }

      @Override
      protected void handleSelectionChange() {
         this.setEnabled(this.viewer.getSelectedAttributes().length == 1);
      }

      private void insertAttributeIntoFilter(LdapStructureAttribute var1) {
         SearchParametersDefiner.this.filterArea.insertAttribute(var1);
      }
   }
}
