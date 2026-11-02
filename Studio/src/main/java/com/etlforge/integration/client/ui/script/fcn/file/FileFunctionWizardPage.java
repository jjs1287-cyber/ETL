package com.etlforge.integration.client.ui.script.fcn.file;

import com.etlforge.integration.client.ui.script.fcn.AbstractSimpleFunctionWizardPage;
import com.etlforge.integration.client.ui.script.fcn.VariableSelector;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.wizard.NumberedSectionsLayout;
import org.etlforge.integration.client.ui.entity.selector.EntitySelector;
import org.etlforge.integration.client.ui.entity.selector.EntityTreeSelector;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.functors.FileBasedLocationFilter;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.FileLocation;
import org.etlforge.integration.data.location.file.FileFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.Function;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;
import org.etlforge.ui.input.InputChangeListeners;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.selection.SelectionChangedEvent;
import org.etlforge.ui.selection.SelectionListener;

final class FileFunctionWizardPage<T extends IntegrationEntity> extends AbstractSimpleFunctionWizardPage {
   private final EntitySelector<T> locationSelector;
   private final Class<T> typeClass;
   private final TextInputField2 fileNameField;
   private final InputChangeListeners changeListeners;

   public FileFunctionWizardPage(Function var1, Class<T> var2, IntegrationProject var3, TextInputField2 var4, VariableSelector var5, UiProvider var6) {
      super(var1, var5, var6);
      this.locationSelector = this.createSourceSelector(var3, var2);
      this.typeClass = var2;
      this.fileNameField = var4;
      var5.setSingleFieldReceiver(var4.getInputComponent());
      this.changeListeners = new InputChangeListeners();
      this.setInputArea(this.doLayout());
   }

   private EntitySelector<T> createSourceSelector(IntegrationProject var1, Class<T> var2) {
      EntityTreeSelector var3 = new EntityTreeSelector(var1, var2);
      var3.setFilter(new FileBasedLocationFilter());
      var3.setSelectionMode(EntitySelector.SelectionMode.SINGLE);
      var3.selectFirst();
      var3.getSelectionSource().addSelectionListener(new FileFunctionWizardPage.SelectionHandler());
      return var3;
   }

   private UiProvider doLayout() {
      NumberedSectionsLayout var1 = new NumberedSectionsLayout();
      var1.setFills(new GridBagLayoutPanel.Fill[]{GridBagLayoutPanel.Fill.BOTH, GridBagLayoutPanel.Fill.HORIZONTAL})
         .setWeights(new double[]{1.0, 1.0}, new double[]{1.0, 0.0})
         .withBackground(StyleSheet.GenericEditorPanelClass.getBackground());
      var1.addSection(this.getTitle(), this.locationSelector);
      var1.addSection("Specify the name of the file (optional)", this.layoutNameField());
      return var1;
   }

   private String getTitle() {
      return this.typeClass.equals(Source.class) ? "Select a Source" : "Select a Target";
   }

   private UiProvider layoutNameField() {
      BoxBuilder var1 = BoxBuilder.horizontal().nonOpaque();
      var1.addAllWithSpace(5, this.fileNameField.getLabel(), this.fileNameField.getInputComponent());
      return var1;
   }

   @Override
   public void addChangeListener(InputChangeListener var1) {
      this.fileNameField.addInputChangeListener(var1);
      this.changeListeners.add(var1);
   }

   @Override
   public void removeChangeListener(InputChangeListener var1) {
      this.fileNameField.removeInputChangeListener(var1);
      this.changeListeners.remove(var1);
   }

   @Override
   public boolean isInputValid() {
      return this.getFileLocation() != null && this.fileNameField.isFieldValid();
   }

   public T getFileLocation() {
      return this.locationSelector.getSelectedEntity();
   }

   public String getFileName() {
      return this.fileNameField.getValue();
   }

   private class SelectionHandler implements SelectionListener {
      private SelectionHandler() {
      }

      @Override
      public void selectionChanged(SelectionChangedEvent var1) {
         Locatable var2 = (Locatable)FileFunctionWizardPage.this.getFileLocation();
         String var3 = this.getConfiguredFileName(var2);
         FileFunctionWizardPage.this.fileNameField.setTextWhenEmpty(var3);
         FileFunctionWizardPage.this.changeListeners.call().inputChanged(new InputChangeEvent(FileFunctionWizardPage.this, "Entity"));
      }

      private String getConfiguredFileName(Locatable var1) {
         if (var1 != null) {
            DataLocation var2 = var1.getLocation();
            if (var2 instanceof FileLocation) {
               FileFilter var3 = ((FileLocation)var2).getFileFilter();
               return var3.getFilter();
            }
         }

         return "";
      }
   }
}
