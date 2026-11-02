package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.salesforce.ui.SchemaEntityNameFilterFunction;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import org.etlforge.application.ui.action.ActionDecorators;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionAction;

public final class ObjectSelectionView implements UiProvider {
   private final ObjectSelectionModel model;
   private final ObjectSelectionController controller;
   private final ObjectSelectionWidget objectSelector;
   private final ObjectSearchField searchField;
   private final ObjectCategorySelector categorySelector;
   private final ExternalIdFieldNameUi externalIdSelector;
   private UiProvider objectSelectionComponent;
   private final BorderLayoutBuilder container;

   public ObjectSelectionView(ObjectSelectionModel var1, ObjectSelectionController var2) {
      this.model = var1;
      this.controller = var2;
      this.objectSelector = new ObjectSelectionTableWidget(var1);
      this.externalIdSelector = new ExternalIdFieldNameUi(var1);
      this.searchField = new ObjectSearchField(this.objectSelector, new SchemaEntityNameFilterFunction(var1.getObjectNameDisplayMode()));
      this.categorySelector = new ObjectCategorySelector(var1);
      this.container = new BorderLayoutBuilder().nonOpaque();
      this.bindToModel();
   }

   private void bindToModel() {
      ObjectSelectionView.FieldRequiredBinding var1 = new ObjectSelectionView.FieldRequiredBinding();
      var1.syncUi();
   }

   public void setDefaultAction(SelectionAction var1) {
      this.objectSelector.setDefaultAction(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      if (this.container.isEmpty()) {
         this.doLayout();
      }

      this.container.decorate(var1);
   }

   private void doLayout() {
      if (this.model.isFieldSelectionRequired()) {
         ObjectSelectionView.FieldSelectorLayout var1 = new ObjectSelectionView.FieldSelectorLayout();
         var1.decorate(this.container);
      } else {
         WizardPageLayout var3 = WizardStyleSheet.emptyPageLayout("Select The Salesforce Object");
         var3.add(this.getObjectSelectionComponent(), true, true);
         JPanel var2 = var3.asCenterBorderLayout();
         this.container.center(var2);
      }
   }

   @Override
   public void requestFocus() {
      this.objectSelector.requestFocus();
   }

   public void focusFieldSelector() {
      this.externalIdSelector.requestFocus();
   }

   public UiProvider getObjectSelectionComponent() {
      if (this.objectSelectionComponent == null) {
         ObjectSelectionView.RefreshAction var1 = new ObjectSelectionView.RefreshAction();
         BoxBuilder var2 = BoxBuilder.horizontal().addAllWithSpace(24, this.searchField, this.categorySelector, var1);
         BorderLayoutBuilder var3 = new BorderLayoutBuilder(0, 10).north(var2).center(this.objectSelector);
         ActionKeyBinding.install(var1, var3.container(), 2);
         this.objectSelectionComponent = var3;
      }

      return this.objectSelectionComponent;
   }

   private class FieldRequiredBinding extends AbstractBinding<Boolean> {
      public FieldRequiredBinding() {
         super(ObjectSelectionView.this.model, "requiresExternalIdField");
      }

      protected void updateUiWithValue(Boolean var1) {
         boolean var2 = !ObjectSelectionView.this.container.isEmpty();
         ObjectSelectionView.this.container.removeAll();
         ObjectSelectionView.this.doLayout();
         if (var2) {
            ObjectSelectionView.this.container.refreshLayout();
         }
      }
   }

   private class FieldSelectorLayout {
      private final NumberedSectionsLayout sections = new NumberedSectionsLayout();

      public FieldSelectorLayout() {
         this.sections.setWeights(new double[]{1.0, 0.0}, new double[]{1.0, 0.0});
         this.sections.setFills(new GridBagLayoutPanel.Fill[]{GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.NONE});
         this.addObjectSelector();
         this.addFieldSelector();
      }

      private void addObjectSelector() {
         this.sections.addSection("Select The Salesforce Object", ObjectSelectionView.this.getObjectSelectionComponent());
      }

      private void addFieldSelector() {
         this.sections.addSection("Select An External ID Field", ObjectSelectionView.this.externalIdSelector);
      }

      public void decorate(UiCanvas var1) {
         this.sections.decorate(var1);
      }
   }

   private final class RefreshAction extends AbstractAction {
      public RefreshAction() {
         super("Refresh");
         ActionDecorators.REFRESH.decorate(this);
         this.putValue("ShortDescription", "Refresh the list of available objects");
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               ObjectSelectionView.this.controller.refreshObjectList();
            }
         });
      }
   }
}
