package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.selector.combo.IntegrationEntityComboBox;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.functors.EntityFilters;
import org.etlforge.integration.data.structure.text.validation.OperatorArgument;
import org.etlforge.integration.data.structure.text.validation.ScriptArgument;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class ScriptSelector extends ArgumentUi {
   private final ScriptSelector.Selector selector;

   public ScriptSelector(FieldValidationModel var1, Optional<EntityExplorerSupport> var2) {
      this.selector = var2.isPresent() ? new ScriptSelector.DynamicSelector(var1, (EntityExplorerSupport)var2.get()) : new ScriptSelector.StaticSelector(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.selector.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.selector.requestFocus();
   }

   @Override
   public void dispose() {
      this.selector.dispose();
   }

   @Override
   public void setEnabled(boolean var1) {
      this.selector.setEnabled(var1);
   }

   @Override
   public void registerAsChangeSource(InputPanel var1) {
      this.selector.registerAsChangeSource(var1);
   }

   private static class DynamicSelector implements ScriptSelector.Selector {
      private final ProjectItemSelector<Script> dropdown;
      private final ScriptSelector.DynamicSelector.ModelBinding modelBinding;

      public DynamicSelector(FieldValidationModel var1, EntityExplorerSupport var2) {
         this.dropdown = this.createSelector(var2);
         this.modelBinding = new ScriptSelector.DynamicSelector.ModelBinding(var1);
         this.modelBinding.syncUi();
      }

      private ProjectItemSelector<Script> createSelector(EntityExplorerSupport var1) {
         EditorPanel var2 = new EditorPanel(var1.getWindow());
         boolean var3 = true;
         ProjectItemSelector var4 = new ProjectItemSelector<>(Script.class, EntityType.Script, var1, var2, var3);
         var4.setEditAutomaticallyOnSelect(false);
         return var4;
      }

      @Override
      public void dispose() {
         this.dropdown.dispose();
         this.modelBinding.dispose();
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.dropdown.getSelectorComponentForLayout(true));
      }

      @Override
      public void requestFocus() {
         this.dropdown.requestFocus();
      }

      @Override
      public void setEnabled(boolean var1) {
         this.dropdown.setEnabled(var1);
      }

      @Override
      public void registerAsChangeSource(InputPanel var1) {
         var1.addInputField(this.dropdown.asInputField("Script"));
      }

      private class ModelBinding extends AbstractBinding<OperatorArgument> implements SelectionListener {
         public ModelBinding(FieldValidationModel var2) {
            super(var2, "argument");
            DynamicSelector.this.dropdown.addSelectionListener(this);
         }

         @Override
         public void selectionChanged(SelectionChangedEvent var1) {
            if (this.isUiToModelEnabled()) {
               this.syncModel();
            }
         }

         @Override
         protected void disposeUiListener() {
            DynamicSelector.this.dropdown.removeSelectionListener(this);
         }

         protected OperatorArgument getValueFromUi() {
            Script var1 = DynamicSelector.this.dropdown.getSelectedItem();
            return var1 == null ? null : new ScriptArgument(var1.getID());
         }

         protected void updateUiWithValue(OperatorArgument var1) {
            if (var1 instanceof ScriptArgument) {
               DynamicSelector.this.dropdown.setSelectedItemId(((ScriptArgument)var1).getId());
            } else {
               DynamicSelector.this.dropdown.setSelectedItemId(null);
            }
         }
      }
   }

   private interface Selector extends UiProvider, Disposable {
      void setEnabled(boolean var1);

      void registerAsChangeSource(InputPanel var1);
   }

   private static class StaticSelector implements ScriptSelector.Selector {
      private final IntegrationEntityComboBox dropdown;
      private final ScriptSelector.StaticSelector.ModelBinding modelBinding;

      public StaticSelector(FieldValidationModel var1) {
         this.dropdown = new IntegrationEntityComboBox(var1.getProject(), EntityType.Script, EntityFilters.ACCEPTS_ALL, false);
         this.dropdown.setDynamic();
         this.modelBinding = new ScriptSelector.StaticSelector.ModelBinding(var1);
         this.modelBinding.syncUi();
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.dropdown.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.dropdown.requestFocus();
      }

      @Override
      public void dispose() {
         this.dropdown.dispose();
         this.modelBinding.dispose();
      }

      @Override
      public void setEnabled(boolean var1) {
         this.dropdown.setEnabled(var1);
      }

      @Override
      public void registerAsChangeSource(InputPanel var1) {
         var1.addInputField(this.dropdown.asInputField("Script", null));
      }

      private class ModelBinding extends AbstractBinding<OperatorArgument> implements ItemListener {
         public ModelBinding(FieldValidationModel var2) {
            super(var2, "argument");
            StaticSelector.this.dropdown.addItemListener(this);
         }

         @Override
         protected void disposeUiListener() {
            StaticSelector.this.dropdown.removeItemListener(this);
         }

         protected OperatorArgument getValueFromUi() {
            Script var1 = (Script)StaticSelector.this.dropdown.getSelectedEntity();
            return var1 == null ? null : new ScriptArgument(var1.getID());
         }

         protected void updateUiWithValue(OperatorArgument var1) {
            if (var1 instanceof ScriptArgument) {
               StaticSelector.this.dropdown.setSelectedEntity(((ScriptArgument)var1).getId());
            } else {
               StaticSelector.this.dropdown.setSelectedEntity((IntegrationEntity)null);
            }
         }

         @Override
         public void itemStateChanged(ItemEvent var1) {
            if (this.isUiToModelEnabled()) {
               this.syncModel();
            }
         }
      }
   }
}
