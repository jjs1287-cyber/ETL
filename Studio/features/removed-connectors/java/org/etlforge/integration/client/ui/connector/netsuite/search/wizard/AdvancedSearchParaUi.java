package org.etlforge.integration.client.ui.connector.netsuite.search.wizard;

import java.util.List;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.widget.SelectorTable;
import org.etlforge.ui.widget.SelectorTableBindings;

public class AdvancedSearchParaUi implements UiProvider {
   private final AdvancedSearchPropertyModel model;
   private final AdvancedSearchOptionUi optionUi;
   private final SelectorTable<NetSuiteCustomSearchObject> subtypeList;
   private final BorderLayoutBuilder container = new BorderLayoutBuilder(0, 20);

   public AdvancedSearchParaUi(AdvancedSearchPropertyModel var1) {
      this.model = var1;
      this.optionUi = new AdvancedSearchOptionUi(var1);
      this.subtypeList = this.createSubtypeList();
      this.bindWithModel();
   }

   boolean isValid() {
      if (this.model.isValueValid()) {
         return true;
      }

      NonBlockingAlert.error("No subtype is selected", "Error Message");
      return false;
   }

   private void bindWithModel() {
      SelectorTableBindings var1 = new SelectorTableBindings(this.model, "subtypes", "selectedSubtypes");
      Bindings var2 = var1.install(this.subtypeList);
      var2.syncUi();
      AdvancedSearchParaUi.VisibleBinding var3 = new AdvancedSearchParaUi.VisibleBinding();
      var3.syncUi();
   }

   private SelectorTable<NetSuiteCustomSearchObject> createSubtypeList() {
      SelectorTable var1 = SelectorTable.empty(NetSuiteCustomSearchObject.class);
      var1.setVisibleRows(10);
      var1.setValueWidth(200);
      var1.setLabel("select sub types for item or transaction search. You must select at least one when applicable");
      return var1;
   }

   private void setSubtypeListVisible(boolean var1) {
      this.container.removeAll();
      this.container.center(this.newLayout(var1));
      this.container.refreshLayout();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.container.decorate(var1);
   }

   private BorderLayoutBuilder newLayout(boolean var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 20).north(this.optionUi.getComponent());
      return var1 ? var2.center(this.layoutSubTypeList()) : var2;
   }

   private BorderLayoutBuilder layoutSubTypeList() {
      BoxBuilder var1 = Horizontal.box().glue().addAllWithSpace(10, this.subtypeList.getSelectAllAction(), this.subtypeList.getUnselectAllAction());
      return new BorderLayoutBuilder(0, 10).center(this.subtypeList).south(var1);
   }

   @Override
   public void requestFocus() {
      this.subtypeList.requestFocus();
   }

   private class VisibleBinding extends AbstractBinding<List<NetSuiteCustomSearchObject>> {
      public VisibleBinding() {
         super(AdvancedSearchParaUi.this.model, "subtypes");
      }

      protected void updateUiWithValue(List<NetSuiteCustomSearchObject> var1) {
         boolean var2 = var1 != null && !var1.isEmpty();
         AdvancedSearchParaUi.this.setSubtypeListVisible(var2);
      }
   }
}
