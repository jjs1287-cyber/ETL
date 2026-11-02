package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Supplier;
import java.awt.EventQueue;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.application.ui.window.editor.EditorPanel;
import org.etlforge.application.ui.wizard.AbstractWizardPage;
import org.etlforge.application.ui.wizard.ValidStateBinding;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.entity.factory.Result;
import org.etlforge.integration.client.ui.entity.factory.UiEntityFactoryCallback;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.LocatableBuilder;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.location.DataLocationType;
import org.etlforge.integration.data.location.LocatableFilter;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.property.AbstractBinding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.Property;

public abstract class LocationPage<T extends IntegrationEntity & Locatable> extends AbstractWizardPage {
   private final LocationPageModel<T> model;
   private final LocationTypeSelector typeSelector;
   private final ProjectItemSelector<T> locationSelector;
   private final JComponent locationSelectorContainer;
   private final BorderLayoutBuilder selectorArea = new BorderLayoutBuilder().nonOpaque().withEmptyBorder(0, 8, 0, 0);
   private final LocalFileSelectionUi<T> localFileSelectionUi;
   private T selectedLocation;

   protected LocationPage(SalesforceWizard<?> var1, LocationPageModel<T> var2, EditorPanel var3) {
      this(var1, "Select Your " + var2.getEntityType().getDisplayName(), var2, var3);
   }

   protected LocationPage(SalesforceWizard<?> var1, String var2, LocationPageModel<T> var3, EditorPanel var4) {
      super(var1, var2);
      this.model = var3;
      this.typeSelector = new LocationTypeSelector(var3);
      this.locationSelector = this.createLocationSelector(var1.getProjectExplorer(), var4);
      this.locationSelectorContainer = BorderLayoutBuilder.fillWith(this.locationSelector);
      this.localFileSelectionUi = new LocalFileSelectionUi<>(var3, this.locationSelector, this.locationSelectorContainer);
      this.selectorArea.setOpaque(false);
      this.installModelBindings();
   }

   private ProjectItemSelector<T> createLocationSelector(EntityExplorerSupport var1, EditorPanel var2) {
      ProjectItemSelector var3 = new ProjectItemSelector<>(this.model.getEntityClass(), this.model.getEntityType(), var1, var2);
      var3.setNewEntityCallback(new LocationPage.LocationTypeSetter());
      return var3;
   }

   private void installModelBindings() {
      Property var1 = this.model.getProperty("type");
      Bindings.of(
            new LocationPage.SelectorAreaBinding(var1),
            new LocationPage.SelectorDecorator(var1),
            this.locationSelector.bindWith(this.model.getProperty("selectedItem")),
            ValidStateBinding.forBoolean(this, this.model, "canContinue")
         )
         .syncUi();
   }

   public final LocationPageModel<T> getModel() {
      return this.model;
   }

   public final DataLocationType getLocationType() {
      return this.model.getSelectedLocationType();
   }

   public final void suggestFileName(String var1) {
      this.localFileSelectionUi.suggestFileName(var1);
   }

   public final void setFileExtensions(Iterable<String> var1) {
      this.localFileSelectionUi.setFileExtensions(var1);
   }

   @Override
   public final boolean prepareForForwardNavigation() {
      boolean var1 = this.isLocalFile() ? this.localFileSelectionUi.validate() : this.locationSelector.save();
      return var1
         && UiThreadSupplier.call(
            new Supplier<Boolean>() {
               public Boolean get() {
                  LocationPage.this.selectedLocation = LocationPage.this.isLocalFile()
                     ? LocationPage.this.localFileSelectionUi.getLocation()
                     : LocationPage.this.locationSelector.getSelectedItem();
                  return LocationPage.this.doAdditionalForwardNavigationValidation(LocationPage.this.model);
               }
            }
         );
   }

   private boolean isLocalFile() {
      return this.model.isLocalFileSelected();
   }

   protected boolean doAdditionalForwardNavigationValidation(LocationPageModel<T> var1) {
      return true;
   }

   @Override
   public final boolean prepareForBackwardNavigation() {
      return this.locationSelector.closeGracefully();
   }

   @Override
   protected final void dispose() {
      this.locationSelector.dispose();
      this.localFileSelectionUi.dispose();
   }

   @Override
   protected final JPanel layoutPage() {
      WizardPageLayout var1 = WizardStyleSheet.emptyPageLayout(this.getTitle());
      var1.add(this.createLayout(), true, true);
      return var1.asCenterBorderLayout();
   }

   private UiProvider createLayout() {
      return Vertical.borderLayout().withEmptyBorder(8).of(InvisiblePanel.of(this.typeSelector), this.selectorArea);
   }

   @Override
   public final void requestFocus() {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            LocationPage.this.typeSelector.requestFocus();
         }
      });
   }

   public final T getLocation() {
      return this.selectedLocation;
   }

   public final boolean isUTF8() {
      DataLocationType var1 = this.model.getSelectedLocationType();
      return var1.isFileBased() ? this.model.isUTF8() : false;
   }

   private class LocationTypeSetter extends UiEntityFactoryCallback.Adapter {
      private LocationTypeSetter() {
      }

      @Override
      public Result preProjectInsertion(IntegrationEntity var1, Folder var2) {
         DataLocationType var3 = LocationPage.this.getLocationType();
         LocatableBuilder var4 = new LocatableBuilder(var3);
         Folder var5 = var4.configure((Locatable)var1, var2);
         ((Locatable)var1).setPartOfSalesforceWizard(true);
         return Result.continueProcess(var5);
      }
   }

   private class SelectorAreaBinding extends AbstractBinding<DataLocationType> {
      private DataLocationType currentType;

      public SelectorAreaBinding(Property<DataLocationType> var2) {
         super(var2);
      }

      protected void updateUiWithValue(DataLocationType var1) {
         if (this.needsUpdate(var1)) {
            this.updateSelectorArea(var1);
         }

         this.currentType = var1;
      }

      private boolean needsUpdate(DataLocationType var1) {
         if (this.currentType == null) {
            return true;
         } else {
            return this.currentType == DataLocationType.LocalFile || var1 == DataLocationType.LocalFile
               ? true
               : this.currentType == DataLocationType.None || var1 == DataLocationType.None;
         }
      }

      private void updateSelectorArea(DataLocationType var1) {
         LocationPage.this.selectorArea.removeAll();
         UiUtils.removeFromParent(LocationPage.this.locationSelectorContainer);
         if (var1 == DataLocationType.LocalFile) {
            JComponent var2 = LocationPage.this.localFileSelectionUi.layout();
            LocationPage.this.selectorArea.center(var2);
            LocationPage.this.localFileSelectionUi.setActive(true);
         } else if (var1 != DataLocationType.None) {
            LocationPage.this.selectorArea.center(LocationPage.this.locationSelectorContainer);
            LocationPage.this.localFileSelectionUi.setActive(false);
         }

         LocationPage.this.selectorArea.refreshLayout();
      }
   }

   private class SelectorDecorator extends AbstractBinding<DataLocationType> {
      public SelectorDecorator(Property<DataLocationType> var2) {
         super(var2);
      }

      protected void updateUiWithValue(DataLocationType var1) {
         LocationPage.this.locationSelector.closeWithoutSaving();
         LocationPage.this.locationSelector.setFilter(this.getFilter());
         LocationPage.this.locationSelector.setSelectorLabel(this.getSelectorLabel());
      }

      private EntityFilter getFilter() {
         DataLocationType var1 = LocationPage.this.getLocationType();
         assert var1 != null;
         return new LocatableFilter(var1);
      }

      private String getSelectorLabel() {
         DataLocationType var1 = LocationPage.this.getLocationType();
         assert var1 != null;
         return var1 == DataLocationType.LocalFile ? "" : "Available " + var1 + " &" + LocationPage.this.model.getEntityType().getPlural();
      }
   }
}
