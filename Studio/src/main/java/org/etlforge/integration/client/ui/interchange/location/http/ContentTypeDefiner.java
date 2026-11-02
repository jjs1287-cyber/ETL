package org.etlforge.integration.client.ui.interchange.location.http;

import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.client.ui.interchange.component.page.panel.IntegrationDataPanelException;
import org.etlforge.integration.client.ui.interchange.location.DataLocationPanelLayout;
import org.etlforge.integration.client.ui.interchange.location.resources.LocationResources;
import org.etlforge.integration.data.location.HttpLocation;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField;
import org.etlforge.ui.input.ToggleButtonActivationConstraint;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;
import org.etlforge.ui.widget.text.KongaTextField;

final class ContentTypeDefiner implements HttpLocationPanelPart {
   private final CheckBoxInputField defaultActivator;
   private final TextInputField typeField;

   public ContentTypeDefiner(InputPanel var1) {
      this.defaultActivator = this.createActivator(var1);
      this.typeField = this.createTypeField(var1);
      this.addActivationConstraint();
   }

   public void reset(HttpLocation var1) {
      String var2 = var1.getContentType();
      if (var2 == null) {
         this.defaultActivator.setSelected(true);
         this.typeField.setValue("");
      } else {
         this.defaultActivator.setSelected(false);
         this.typeField.setValue(var2);
      }
   }

   public void applyTo(HttpLocation var1) throws IntegrationDataPanelException {
      String var2 = this.defaultActivator.isSelected() ? null : this.typeField.getValue().trim();
      var1.setContentType(var2);
   }

   @Override
   public void layout(TwoColumnLayout var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(10, InputFieldFactory.getCapsule(this.defaultActivator, 0), InputFieldFactory.getCapsule(this.typeField, 0));
      var1.addRow(var2.container());
   }

   @Override
   public void addToLayout(DataLocationPanelLayout var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(0, 0, 10, 0).withBorder(StyleSheet.getDefaultTitleBorder("Content-Type"));
      var2.setWeightX(1.0).setFill(GridBagLayoutPanel.Fill.HORIZONTAL);
      var2.add(InputFieldFactory.getCapsule(this.defaultActivator, 0));
      var2.add(InputFieldFactory.getCapsule(this.typeField, 0));
      var1.add(null, new BorderLayoutBuilder().north(var2).container(), true);
   }

   @Override
   public void dispose() {
      this.defaultActivator.dispose();
      this.typeField.dispose();
   }

   private CheckBoxInputField createActivator(InputPanel var1) {
      KongaCheckBox var2 = new KongaCheckBox(true);
      CheckBoxInputField var3 = new CheckBoxInputField(
         LocationResources.getString("Http.ContentType.Name"), var2, KongaLabel.withMnemonic(LocationResources.getString("Http.ContentType.Default"))
      );
      var1.addInputField(var3);
      return var3;
   }

   private TextInputField createTypeField(InputPanel var1) {
      TextInputField var2 = new TextInputField(
         LocationResources.getString("Http.ContentType.Name"),
         new KongaTextField(20),
         KongaLabel.withMnemonic(LocationResources.getString("Http.ContentType.Label"))
      );
      var1.addInputField(var2);
      return var2;
   }

   private void addActivationConstraint() {
      ToggleButtonActivationConstraint var1 = new ToggleButtonActivationConstraint(this.defaultActivator);
      var1.reverse();
      var1.bind(this.typeField, true);
      this.typeField.addActivationConstraint(var1);
   }
}
