package org.etlforge.integration.client.ui.connector.netsuite.search.wizard.field;

import com.google.common.base.Objects;
import com.google.common.collect.FluentIterable;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.JLabel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.client.ui.connector.netsuite.search.wizard.QueryBuilderModel;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.Horizontal;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.selection.OrderedSelection;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionFactory;
import org.etlforge.ui.selection.SelectionListener;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.util.Property;

public final class FilterList implements UiProvider {
   private final QueryBuilderModel model;
   private final KongaList listWidget;
   private final FilterList.EditFilterAction editAction = new FilterList.EditFilterAction();
   private final FilterList.DeleteFiltersAction deleteAction = new FilterList.DeleteFiltersAction();

   public FilterList(QueryBuilderModel var1) {
      this.model = var1;
      this.listWidget = this.createList();
      this.bindWithModel(var1);
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setVisibleRowCount(4);
      var1.setLabel(new JLabel("Defined filters:"));
      var1.setSelectionMode(ListSelectionMode.MULTIPLE_INTERVAL_SELECTION);
      KongaList var2 = var1.newList();
      var2.addKeyBinding(KeyUtils.DELETE, "delete", this.deleteAction);
      var2.setDefaultAction(this.editAction);
      this.deleteAction.setSelectionSource(var2);
      return var2;
   }

   private void bindWithModel(QueryBuilderModel var1) {
      Property var2 = var1.getProperty("filters");
      ListBinding var3 = new ListBinding(var2, this.listWidget);
      var3.setListOrder(Filter.alphabeticalOrder());
      var3.setSelectionRetentionMode(ListBinding.SelectionRetentionMode.INDECES);
      Bindings.of(var3, new FilterList.SelectionBinding(var1)).syncUi();
   }

   public FilterList.EditFilterAction getEditAction() {
      return this.editAction;
   }

   @Override
   public void decorate(UiCanvas var1) {
      Grid var2 = Horizontal.grid(5).of(this.editAction, this.deleteAction);
      new BorderLayoutBuilder(0, 5).center(this.listWidget).south(Horizontal.Align.west(var2)).decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.listWidget.requestFocus();
   }

   private class DeleteFiltersAction extends AbstractSingleTypeSelectionAction<Filter> {
      public DeleteFiltersAction() {
         super(Filter.class, "Delete");
         ActionDecorators.DELETE.decorate(this);
         this.putValue("ShortDescription", "Deletes the selected filters");
      }

      @Override
      protected void run(List<Filter> var1) {
         FilterList.this.model.removeFilters(var1);
      }
   }

   private class EditFilterAction extends AbstractSingleObjectSelectionAction<Filter> {
      public EditFilterAction() {
         super(Filter.class, "Edit");
      }

      protected void run(final Filter var1, final ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditFilterAction.this.edit(var1, var2);
            }
         });
      }

      private void edit(Filter var1, ActionEvent var2) {
         Window var3 = Objects.firstNonNull(UiUtils.getWindowAncestor(var2).orNull(), ApplicationUi.getAppWinFrame());
         FilterUi var4 = FilterUi.editFilter(var1);
         Filter var5 = var4.openInEditFilterDialog(var3);
         if (var5 != null) {
            FilterList.this.model.addOrReplaceFilter(var5);
         }
      }
   }

   private class SelectionBinding extends AbstractBinding<List<Filter>> implements SelectionListener {
      public SelectionBinding(QueryBuilderModel var2) {
         super(var2, "selectedFilters");
         FilterList.this.listWidget.addSelectionListener(this);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         if (this.isUiToModelEnabled()) {
            this.syncModel();
         }
      }

      @Override
      protected void disposeUiListener() {
         FilterList.this.listWidget.removeSelectionListener(this);
      }

      protected List<Filter> getValueFromUi() {
         IndexedSelection var1 = FilterList.this.listWidget.getSelection();
         return var1.isEmpty() ? Collections.emptyList() : FluentIterable.from(var1.items()).filter(Filter.class).toImmutableList();
      }

      protected void updateUiWithValue(List<Filter> var1) {
         OrderedSelection var2 = SelectionFactory.newOrderedSelection(var1);
         FilterList.this.listWidget.setSelection(var2);
      }
   }
}
