package org.etlforge.integration.client.ui.interchange.component.page.entity;

import com.google.common.collect.Lists;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.ui.widget.InputFieldLayoutUtils;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanel;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.util.InterchangeViewUtils;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.ui.input.InputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.panel.InvisiblePanel;
import org.etlforge.util.name.IllegalNameException;

public abstract class IntegrationEntityPanel extends IntegrationDataPanel {
   private final IntegrationEntity entity;
   private final ApplicationWindow window;
   private final TextInputField2 nameField;
   private final List<JComponent> componentsAfterNameField;
   private boolean includeNameField = true;

   protected IntegrationEntityPanel(ApplicationWindow var1, IntegrationEntity var2) {
      this.entity = var2;
      this.window = var1;
      this.nameField = this.createNameField(var2);
      this.componentsAfterNameField = Lists.newArrayList();
      this.setSendInputChangeEvents(true);
      this.setSendUndoEvents(true);
   }

   public IntegrationEntity getDisplayedObject() {
      return this.entity;
   }

   public final void setIncludeNameField(boolean var1) {
      this.includeNameField = var1;
   }

   public final boolean isNameFieldIncluded() {
      return this.includeNameField;
   }

   @Override
   public final void apply() throws IntegrationDataPanelException {
      this.validate();
      this.applyName();
      this.applyOtherPropertiesThanTheName();
   }

   protected void validate() throws IntegrationDataPanelException {
   }

   private void applyName() throws IntegrationDataPanelException {
      if (this.includeNameField) {
         try {
            this.entity.setName(this.nameField.getValue());
         } catch (IllegalNameException var2) {
            throw new IntegrationDataPanelException(var2);
         }
      }
   }

   protected abstract void applyOtherPropertiesThanTheName() throws IntegrationDataPanelException;

   @Override
   public void setInitialFocus() {
      this.nameField.requestFocus();
   }

   public final BoundProperty getNameFieldProperty() {
      return BoundPropertyFactory.nameProperty(this.nameField);
   }

   public final ApplicationWindow getWindow() {
      return this.window;
   }

   protected final void addComponentAfterNameField(JComponent var1) {
      this.componentsAfterNameField.add(var1);
   }

   @Override
   protected IntegrationDataPanel.Layout getDefaultLayout() {
      IntegrationDataPanel.Layout var1 = super.getDefaultLayout();
      if (this.includeNameField) {
         this.addNameFieldToDefaultLayout(var1);
      }

      return var1;
   }

   protected final IntegrationDataPanel.Layout getDefaultLayoutWithoutNameField() {
      return super.getDefaultLayout();
   }

   private void addNameFieldToDefaultLayout(IntegrationDataPanel.Layout var1) {
      if (this.componentsAfterNameField.isEmpty()) {
         if (this.includeNameField) {
            var1.addToTopGrid(this.nameField);
         }
      } else {
         JLabel var2 = this.nameField.getLabel();
         BoxBuilder var3 = BoxBuilder.horizontal().nonOpaque();
         var3.add(this.nameField.getInputComponent()).strut(10).addAllWithSpace(10, this.componentsAfterNameField);
         var1.addToTopGrid(var2, var3.container());
      }
   }

   protected final TextInputField2 getNameField() {
      return this.nameField;
   }

   protected final JComponent getNameFieldLayout() {
      if (this.includeNameField) {
         InputPanel var1 = new InputPanel();
         var1.setOpaque(false);
         InputFieldLayoutUtils.layoutOneColumn(var1, new InputField[]{this.nameField}, true, false);
         return InvisiblePanel.of(var1.getUi());
      } else {
         return InvisiblePanel.newPanel();
      }
   }

   private TextInputField2 createNameField(IntegrationEntity var1) {
      String var2 = Strings.get("EntityPage.NameField.Name");
      KongaLabel var3 = KongaLabel.withMnemonic(Strings.get("EntityPage.NameField.Label"));
      TextInputField2 var4 = InterchangeViewUtils.getIntegrationEntityNameInputField(var1, var2, var3, 30);
      var4.setValue(var1.getName());
      this.addInputField(var4, var4, var4);
      return var4;
   }

   public final String getDisplayedName() {
      return this.getNameField().getValue();
   }
}
