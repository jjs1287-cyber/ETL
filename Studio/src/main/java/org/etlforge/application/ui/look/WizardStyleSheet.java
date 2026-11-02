package org.etlforge.application.ui.look;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.etlforge.application.ui.wizard.WizardPageLayout;
import org.etlforge.ui.Empty;
import org.etlforge.ui.layout.Grid;
import org.etlforge.ui.layout.GridBagLayoutPanel;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.label.KongaLabel;

public final class WizardStyleSheet {
   public static final TextStyle SECTION_STYLE = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 22, 1);
   public static final TextStyle SMALL_SECTION_STYLE = TextStyle.adjustSize(SECTION_STYLE, -4);
   public static final TextStyle CAPTION_STYLE = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 16, 1, Colors.TEXT_FOREGROUND);
   public static final TextStyle MEDIUM_CAPTION_STYLE = TextStyle.adjustSize(CAPTION_STYLE, -2);
   public static final TextStyle SMALL_CAPTION_STYLE = TextStyle.adjustSize(CAPTION_STYLE, -4);
   public static final TextStyle NUMBER_STYLE = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 48, 1, Colors.THEME_TEXT_COLOR);
   public static final TextStyle REQUIRED_STYLE = new TextStyle("Verdana, Geneva, Arial, Helvetica, sans-serif", 14, 1, Color.RED);

   public static KongaLabel caption(String var0) {
      return CAPTION_STYLE.makeLabel(var0);
   }

   public static KongaLabel smallCaption(String var0) {
      return SMALL_CAPTION_STYLE.makeLabel(var0);
   }

   public static KongaLabel number(int var0) {
      KongaLabel var1 = NUMBER_STYLE.makeLabel(String.valueOf(var0));
      var1.setAntiAliasingEnabled(true);
      return var1;
   }

   public static WizardPageLayout emptyPageLayout(String var0) {
      return emptyPageLayout(var0, null);
   }

   public static WizardPageLayout emptyPageLayout(String var0, String var1) {
      WizardPageLayout var2 = new WizardPageLayout(var0, var1) {
         @Override
         protected TextStyle getMainCaptionStyle() {
            return WizardStyleSheet.CAPTION_STYLE;
         }
      };
      var2.setBorder(Empty.border());
      var2.setInsets(0, 0, 0, 0);
      return var2;
   }

   public static JComponent layoutButtons(JButton... var0) {
      Grid var1 = Grid.oneRow(10).nonOpaque();
      var1.addAll(var0);
      OneColumnPanel var2 = new OneColumnPanel().withBackground(Colors.ETLFORGE_BLUE);
      var2.setAnchor(GridBagLayoutPanel.Anchor.EAST);
      var2.add(var1);
      return var2.container();
   }

   private WizardStyleSheet() {
   }
}

