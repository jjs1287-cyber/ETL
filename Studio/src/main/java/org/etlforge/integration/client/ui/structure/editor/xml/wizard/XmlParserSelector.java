package org.etlforge.integration.client.ui.structure.editor.xml.wizard;

import java.util.Arrays;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.ui.widget.button.ToggleButtonKeySelector;

final class XmlParserSelector implements UiProvider {
   private final KongaRadioButton jbParser = new KongaRadioButton("Use ETLForge's XML parser", true);
   private final KongaRadioButton domParser = new KongaRadioButton("Use DOM parser");

   public XmlParserSelector() {
      ButtonUtils.makeMutuallyExclusive(this.jbParser, this.domParser);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel()
         .stretchHorizontally()
         .withInsets(0, 0, 5, 0)
         .of(this.jbParser, this.domParser)
         .withBorder(StyleSheet.getDefaultTitleBorder("Choice of XML parser"));
      ToggleButtonKeySelector.install(Arrays.asList(this.jbParser, this.domParser), var2.container());
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.jbParser.requestFocusInWindow();
   }

   public void applyTo(XmlStructure var1) {
      boolean var2 = this.jbParser.isSelected();
      var1.setUseJbXmlParser(var2);
   }

   public void setInitialState(XmlStructure var1) {
      if (var1.isUsingJbXmlParser()) {
         this.jbParser.setSelected(true);
      } else {
         this.domParser.setSelected(true);
      }
   }
}
