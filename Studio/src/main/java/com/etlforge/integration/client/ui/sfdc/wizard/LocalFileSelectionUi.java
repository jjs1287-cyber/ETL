package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import javax.swing.JComponent;
import org.etlforge.application.ui.NonBlockingAlert;
import org.etlforge.integration.client.ui.entity.wizard.ProjectItemSelector;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.system.Disposable;
import org.etlforge.system.DisposableCollection;
import org.etlforge.ui.UiThreadSupplier;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.property.Bindings;
import org.etlforge.ui.property.RadioButtonEnumBinding;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.util.file.FileSelectorPanel;
import org.etlforge.ui.util.focus.FocusRoot;
import org.etlforge.ui.util.focus.FocusUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.util.Property;

final class LocalFileSelectionUi<T extends IntegrationEntity & Locatable> implements Disposable {
   private final LocationPageModel<T> model;
   private final ProjectItemSelector<T> locationSelector;
   private final JComponent locationSelectorContainer;
   private final KongaRadioButton browseOption = new KongaRadioButton("Select a fil&e", true);
   private final KongaRadioButton existingOption;
   private final LocalFileBrowser<T> browser;
   private DisposableCollection inactivator;
   private FocusRoot existingOptionFocusRoot;
   private JComponent ui;

   public LocalFileSelectionUi(LocationPageModel<T> var1, ProjectItemSelector<T> var2, JComponent var3) {
      this.model = var1;
      this.existingOption = new KongaRadioButton(this.getExistingOptionText());
      this.locationSelector = var2;
      this.locationSelectorContainer = var3;
      this.browser = new LocalFileBrowser<>(var1.getEntityClass(), this.getFileSelectionMode());
      this.browser.setFileValidator(var1.getFileValidator());
      this.browseOption.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            LocalFileSelectionUi.this.locationSelector.forceCloseAfterSaving();
         }
      });
      this.installModelBindings();
   }

   private String getExistingOptionText() {
      return "Select an existing Local File &" + (this.model.getEntityType() == EntityType.Source ? "Source" : "Target");
   }

   private FileSelectorPanel.Mode getFileSelectionMode() {
      return this.model.getEntityType() == EntityType.Source ? FileSelectorPanel.OPEN : FileSelectorPanel.SAVE;
   }

   private void installModelBindings() {
      Bindings.of(this.bindOptions(), this.browser.bindWith(this.model.getProperty("localFile"))).syncUi();
   }

   private Binding bindOptions() {
      Property var1 = this.model.getProperty("localFileMode");
      EnumMap var2 = Maps.newEnumMap(LocalFileSelectionMode.class);
      var2.put(LocalFileSelectionMode.BROWSE, this.browseOption);
      var2.put(LocalFileSelectionMode.SELECT_EXISTING, this.existingOption);
      return RadioButtonEnumBinding.forButtons(var1, var2);
   }

   @Override
   public void dispose() {
      this.setActive(false);
      this.browser.dispose();
   }

   public void suggestFileName(String var1) {
      this.browser.suggestFileName(var1);
   }

   public void setFileExtensions(Iterable<String> var1) {
      this.browser.setFileExtensions(var1);
   }

   public void setActive(boolean var1) {
      if (var1) {
         this.activate();
      } else {
         this.inactivate();
      }
   }

   private void activate() {
      if (this.inactivator == null) {
         this.existingOptionFocusRoot = FocusRoot.of(this.locationSelectorContainer).withFocusReceiver(this.locationSelector);
         this.inactivator = new DisposableCollection(
            FocusUtils.bindChoiceComponents(this.browseOption, this.browser.asFocusable()),
            FocusUtils.bindChoiceComponents(this.existingOption, this.existingOptionFocusRoot)
         );
         if (this.browseOption.isSelected()) {
            this.browser.requestFocus();
         } else {
            this.locationSelector.requestFocus();
         }
      }
   }

   private void inactivate() {
      if (this.inactivator != null) {
         this.inactivator.dispose();
         this.existingOptionFocusRoot.dispose();
         this.inactivator = null;
         this.existingOptionFocusRoot = null;
         UiUtils.setEnabledRecursively(this.ui, true);
         this.locationSelector.setEnabled(true);
      }
   }

   public JComponent layout() {
      UiUtils.removeFromParent(this.locationSelectorContainer);
      byte var1 = 32;
      OneColumnPanel var2 = new OneColumnPanel(GridBagLayoutPanel.Anchor.WEST, GridBagLayoutPanel.Fill.HORIZONTAL)
         .nonOpaque()
         .withInsets(0, 0, 8, 0)
         .setWeightX(1.0);
      var2.add(this.browseOption);
      var2.withInsets(0, var1, 16, 0).add(this.browser);
      var2.withInsets(0, 0, 8, 0).add(this.existingOption);
      var2.withInsets(0, var1, 0, 0).fillOut(this.locationSelectorContainer);
      this.ui = var2.container();
      return this.ui;
   }

   public final boolean validate() {
      return UiThreadSupplier.call(new Supplier<Boolean>() {
         public Boolean get() {
            return LocalFileSelectionUi.this.validateEDT();
         }
      });
   }

   private boolean validateEDT() {
      assert EventQueue.isDispatchThread();
      return this.browseOption.isSelected() ? this.validateBrowsedFile() : this.locationSelector.save();
   }

   private Boolean validateBrowsedFile() {
      String var1 = this.browser.validateLocation();
      if (var1 == null) {
         return true;
      }

      NonBlockingAlert.error(this.browser.getUi(), var1, "Error");
      return false;
   }

   public T getLocation() {
      if (this.browseOption.isSelected()) {
         IntegrationEntity var1 = this.browser.asProjectItem();
         ((Locatable)var1).setPartOfSalesforceWizard(true);
         return (T)var1;
      } else {
         return this.locationSelector.getSelectedItem();
      }
   }
}
