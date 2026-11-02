package com.etlforge.integration.client.ui.sfdc.wizard;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.application.ui.look.WizardStyleSheet;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.ui.Empty;
import org.etlforge.ui.input.CheckBoxInputField;
import org.etlforge.ui.input.InputPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.Vertical;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.button.KongaCheckBox;
import org.etlforge.ui.widget.label.KongaLabel;

public final class AutoMapperChoice {
   private final KongaCheckBox autoMapChoice = new KongaCheckBox("Use &Automapper", true).withBorder(Empty.border(10, 20, 0, 0));
   private String mappingsMessage;
   private Border border;
   private TextStyle captionStyle = WizardStyleSheet.CAPTION_STYLE;
   private Icon icon = ClientIcons.AUTO_MAPPER_32;

   public AutoMapperChoice() {
      this(true);
   }

   public AutoMapperChoice(boolean var1) {
      this.autoMapChoice.setSelected(var1);
   }

   public AutoMapperChoice(AutoMapperConfig var1) {
      this(var1.getDefaultValue());
      this.withMappingsMessage(var1.getMessage());
   }

   public boolean isSelected() {
      return this.autoMapChoice.isSelected();
   }

   public AutoMapperChoice asChangeSourceIn(InputPanel var1) {
      var1.addInputField(new CheckBoxInputField("Automapper", this.autoMapChoice));
      return this;
   }

   public void setBorder(Border var1) {
      this.border = var1;
   }

   public AutoMapperChoice withSmallIcon() {
      this.icon = ClientIcons.AUTO_MAPPER_16;
      return this;
   }

   public AutoMapperChoice withCaptionStyle(TextStyle var1) {
      this.captionStyle = var1;
      return this;
   }

   public AutoMapperChoice withMappingsMessage(String var1) {
      this.mappingsMessage = var1;
      return this;
   }

   public JPanel getUi() {
      JLabel var1 = this.getCaption();
      OneColumnPanel var2 = Vertical.column().setWeightX(1.0).withInsets(0, 0, 3, 0).withBorder(this.border).of(this.mappingsMessage, this.autoMapChoice);
      return Vertical.borderLayout(10).of(var1, var2).container();
   }

   private JLabel getCaption() {
      KongaLabel var1 = this.captionStyle.makeLabel("Automapper");
      var1.setIcon(this.icon);
      return var1;
   }
}
