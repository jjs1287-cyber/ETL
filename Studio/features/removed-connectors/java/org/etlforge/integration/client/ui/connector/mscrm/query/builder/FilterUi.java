package org.etlforge.integration.client.ui.connector.mscrm.query.builder;

import com.google.common.base.Optional;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.integration.mscrm.metadata.MSCrmObject;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.ListBinding;
import org.etlforge.ui.selection.AbstractSingleObjectSelectionAction;
import org.etlforge.ui.selection.AbstractSingleTypeSelectionAction;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.button.PopupMenuButton;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.util.ListProperty;
import org.etlforge.util.Property;
import org.etlforge.util.string.StringLimiter;

final class FilterUi implements DataEntryUi<Filter> {
   private final FilterDefinitionModel model;
   private final RadioButtonGroupPanel<FilterOperator> typeSelector;
   private final KongaList list;
   private final FilterUi.EditEntryAction editEntryAction = new FilterUi.EditEntryAction();
   private final FilterUi.DeleteEntriesAction deleteEntriesAction = new FilterUi.DeleteEntriesAction();

   public FilterUi(MSCrmObject var1) {
      this(new FilterDefinitionModel(var1));
   }

   public FilterUi(FilterDefinitionModel var1) {
      this.model = var1;
      this.typeSelector = RadioButtonGroupPanel.forEnum(FilterOperator.class);
      this.list = this.createList();
      this.bindToModel();
   }

   private KongaList createList() {
      KongaListFactory var1 = LayoutUtils.newListFactory();
      var1.setCellRenderer(new FilterUi.Renderer());
      var1.addFocusKeyBinding(this.deleteEntriesAction, "delete");
      KongaList var2 = var1.newList();
      this.deleteEntriesAction.setSelectionSource(var2);
      var2.setDefaultAction(this.editEntryAction);
      return var2;
   }

   private void bindToModel() {
      Property var1 = this.model.getProperty("entries");
      ListBinding var2 = new ListBinding(var1, this.list);
      var2.setSelectionRetentionMode(ListBinding.SelectionRetentionMode.VALUES);
      Property var3 = this.model.getProperty("filterOperator");
      Bindings.of(var2, this.typeSelector.bindWith(var3)).syncUi();
   }

   @Override
   public Optional<Filter> getEnteredValue() {
      return Optional.of(this.model.getFilter());
   }

   public Filter getFilter() {
      return this.model.getFilter();
   }

   @Override
   public EnteredDataValidation validate() {
      return EnteredDataValidation.OK;
   }

   @Override
   public void focusInvalidComonent() {
      throw new UnsupportedOperationException();
   }

   @Override
   public void decorate(UiCanvas var1) {
      UiProvider var2 = this.layoutList();
      new BorderLayoutBuilder(0, 10).north(this.typeSelector.oneRowLayout()).center(var2).decorate(var1);
   }

   private UiProvider layoutList() {
      ListProperty var1 = (ListProperty)this.model.<List>getProperty("entries");
      PopupMenuButton var2 = new PopupMenuButton("Add...", new Action[]{new FilterUi.AddConditionAction(), new FilterUi.AddNestedFilterAction()});
      return LayoutUtils.layoutListWithButtons(this.list, var1, var2, this.editEntryAction, this.deleteEntriesAction);
   }

   @Override
   public void requestFocus() {
      this.list.requestFocus();
   }

   private class AddConditionAction extends AbstractAction {
      public AddConditionAction() {
         super("Condition");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddConditionAction.this.addCondition();
            }
         });
      }

      private void addCondition() {
         ConditionUi var1 = new ConditionUi(FilterUi.this.model.getObject());
         Optional var2 = DataEntryUiDialog.open(var1, ApplicationUi.getAppWinFrame(), "Add Condition");
         if (var2.isPresent()) {
            FilterUi.this.model.addEntry((FilterEntry)var2.get());
         }
      }
   }

   private class AddNestedFilterAction extends AbstractAction {
      public AddNestedFilterAction() {
         super("Nested Filter");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AddNestedFilterAction.this.addFilter();
            }
         });
      }

      private void addFilter() {
         FilterUi var1 = new FilterUi(FilterUi.this.model.getObject());
         Optional var2 = DataEntryUiDialog.open(var1, ApplicationUi.getAppWinFrame(), "Add Nested Filter");
         if (var2.isPresent()) {
            FilterUi.this.model.addEntry((FilterEntry)var2.get());
         }
      }
   }

   private class DeleteEntriesAction extends AbstractSingleTypeSelectionAction<FilterEntry> {
      public DeleteEntriesAction() {
         super(FilterEntry.class, "Delete");
         this.putValue("AcceleratorKey", KeyUtils.DELETE);
      }

      @Override
      protected void run(final List<FilterEntry> var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               FilterUi.this.model.removeEntries(var1);
            }
         });
      }
   }

   private class EditEntryAction extends AbstractSingleObjectSelectionAction<FilterEntry> {
      public EditEntryAction() {
         super(FilterEntry.class, "Edit");
      }

      protected void run(final FilterEntry var1, ActionEvent var2) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               EditEntryAction.this.editEntry(var1);
            }
         });
      }

      private void editEntry(FilterEntry var1) {
         if (var1 instanceof Condition) {
            this.editCondition((Condition)var1);
         } else {
            if (!(var1 instanceof Filter)) {
               throw new NotImplementedYetException("Editing " + var1.getClass().getSimpleName());
            }

            this.editNestedFilter((Filter)var1);
         }
      }

      private void editCondition(Condition var1) {
         ConditionUi var2 = new ConditionUi(FilterUi.this.model.getObject());
         var2.setCondition(var1);
         Optional var3 = DataEntryUiDialog.open(var2, ApplicationUi.getAppWinFrame(), "Edit Condition");
         this.updateModel(var1, var3);
      }

      private <T extends FilterEntry> void updateModel(T var1, Optional<T> var2) {
         if (var2.isPresent() && !((FilterEntry)var2.get()).equals(var1)) {
            FilterUi.this.model.replaceEntry(var1, (FilterEntry)var2.get());
         }
      }

      private void editNestedFilter(Filter var1) {
         FilterDefinitionModel var2 = new FilterDefinitionModel(FilterUi.this.model.getObject());
         var2.setFilter(var1);
         FilterUi var3 = new FilterUi(var2);
         Optional var4 = DataEntryUiDialog.open(var3, ApplicationUi.getAppWinFrame(), "Edit Nested Filter");
         this.updateModel(var1, var4);
      }
   }

   private static class Renderer extends DefaultListCellRenderer {
      private Renderer() {
      }

      @Override
      public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
         super.getListCellRendererComponent(var1, var2, var3, var4, var5);
         if (var2 instanceof Condition) {
            this.render((Condition)var2);
         } else if (var2 instanceof Filter) {
            this.render((Filter)var2);
         }

         return this;
      }

      private void render(Condition var1) {
         String var2 = String.format("%s %s %s", var1.getAttribute().getName(), var1.getOperator(), var1.getValue().getDisplay());
         String var3 = StringLimiter.limit(var2, 100);
         this.setText(var3);
      }

      private void render(Filter var1) {
         String var2 = "Filter (" + var1.getOperator() + ") ...";
         this.setText(var2);
      }
   }
}
