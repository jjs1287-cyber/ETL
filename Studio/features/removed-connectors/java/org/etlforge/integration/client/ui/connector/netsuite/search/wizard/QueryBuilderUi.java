package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import com.google.common.base.Preconditions;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.etlforgeconnector.netsuite.NetSuiteSearchCategory;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.widget.tabbedpane.KongaTabbedPane;

public final class QueryBuilderUi implements UiProvider {
   private final QueryBuilderModel model;
   private final ColumnSelectionUi columnSelectionUi;
   private final CriteriaDefinitionUi criteriaDefinitionUi;
   private KongaTabbedPane tabs;
   private final BorderLayoutBuilder container = new BorderLayoutBuilder(0, 20);
   private final PageSizeSelectorAndTestButtonUi testUi;

   public QueryBuilderUi(QueryBuilderModel var1, PageSizeSelectorAndTestButtonUi var2) {
      this.model = Preconditions.checkNotNull(var1);
      this.columnSelectionUi = new ColumnSelectionUi(var1.getRecordTreeModel());
      this.criteriaDefinitionUi = new CriteriaDefinitionUi(var1);
      this.testUi = var2;
      this.tabs = null;
      this.setColumnSelectionVisible(true);
      this.bindWithModel(var1);
   }

   boolean isValid() {
      this.model.updateResult();
      boolean var1 = this.model.isValueValid();
      if (!var1) {
         NonBlockingAlert.error(this.model.getInvalidMessage(), "Error Message");
      }

      return var1;
   }

   private void bindWithModel(QueryBuilderModel var1) {
      QueryBuilderUi.ObjectBinding var2 = new QueryBuilderUi.ObjectBinding(var1.getRecordTreeModel());
      var2.syncUi();
   }

   QueryBuilderModel getModel() {
      return this.model;
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.container.decorate(var1);
   }

   @Override
   public void requestFocus() {
      if (this.tabs != null && this.tabs.getSelectedIndex() == 0) {
         this.columnSelectionUi.requestFocus();
      } else {
         this.criteriaDefinitionUi.requestFocus();
      }
   }

   public void setNetSuiteSearchCategory(NetSuiteSearchCategory var1) {
      this.setColumnSelectionVisible(NetSuiteSearchCategory.NetSuiteSearchAdvanced == var1);
   }

   public void setColumnSelectionVisible(boolean var1) {
      boolean var2 = this.tabs != null;
      if (var1 != var2) {
         this.container.removeAll();
         if (var1) {
            this.tabs = new KongaTabbedPane();
            this.tabs.addTab("Select Columns To Return", this.columnSelectionUi);
            this.tabs.addTab("Define Filter Criteria", this.criteriaDefinitionUi);
            this.tabs.setSelectedIndex(0);
            this.container.center(this.tabs);
         } else {
            this.tabs = null;
            this.container.center(this.criteriaDefinitionUi);
         }

         this.container.south(this.testUi.layoutPageSizeSelectorAndTestButton());
         this.container.refreshLayout();
      }
   }

   private class ObjectBinding extends AbstractBinding<Integer> {
      public ObjectBinding(SearchRecordTreeModel var2) {
         super(var2, "ChangeModel");
      }

      protected void updateUiWithValue(Integer var1) {
         QueryBuilderUi.this.columnSelectionUi.updateTree();
         QueryBuilderUi.this.criteriaDefinitionUi.updateTree();
         QueryBuilderUi.this.setNetSuiteSearchCategory(QueryBuilderUi.this.model.getSearchType());
      }
   }
}
