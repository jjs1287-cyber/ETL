package org.etlforge.integration.client.ui.interchange.entity.httpendpoint.page;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.widget.InputFieldFactory;
import org.etlforge.integration.data.binding.BoundProperty;
import org.etlforge.integration.data.binding.ui.BoundPropertyFactory;
import org.etlforge.integration.data.entity.HttpEndpoint;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.input.TextInputField2;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.widget.label.KongaLabel;

final class UrlPanel {
   private final TextInputField2 field = InputFieldFactory.getNonEmptyTextInputField("Endpoint URL", KongaLabel.withMnemonic("Endpoint &URL"), 20);

   public UrlPanel(HttpEndpoint var1, InputPanel var2) {
      this.field.setSelectAllWhenFocused(true);
      this.reset(var1);
      if (var2 != null) {
         var2.addInputField(this.field, this.field, this.field);
      }
   }

   public void reset(HttpEndpoint var1) {
      this.field.setValue(var1.getUrl());
   }

   public void apply(HttpEndpoint var1) {
      var1.setUrl(this.field.getValue().trim());
   }

   public JLabel getLabel() {
      return this.field.getLabel();
   }

   public JComponent getFieldForLayout() {
      KongaLabel var1 = TextStyles.DefaultBold.makeLabel("http://[server name]:[server port]/");
      var1.setForeground(Color.DARK_GRAY);
      JTextComponent var2 = this.field.getInputComponent();
      Border var3 = var2.getBorder();
      var2.setBorder(null);
      return BoxBuilder.horizontal().withBorder(var3).withBackground(var2.getBackground()).addAllWithSpace(2, var1, var2).container();
   }

   public BoundProperty asBoundProperty() {
      return BoundPropertyFactory.textProperty("endpoint_url", this.field);
   }
}
