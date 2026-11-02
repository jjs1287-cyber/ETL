package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import java.awt.Component;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;

public class PreferenceUiLayout {
   private final PreferenceUi prefUi;
   private String title;
   private String caption;

   public PreferenceUiLayout(PreferenceUi var1) {
      this.prefUi = Preconditions.checkNotNull(var1);
   }

   public PreferenceUiLayout setTitle(String var1) {
      this.title = var1;
      return this;
   }

   public PreferenceUiLayout setCaption(String var1) {
      this.caption = var1;
      return this;
   }

   public void decorate(UiCanvas var1) {
      if (this.title == null && this.caption == null) {
         this.prefUi.decorate(var1);
      } else {
         OneColumnPanel var2 = new OneColumnPanel();
         var2.withInsets(5, 0, 5, 0);
         var2.setWeightX(1.0);
         this.layoutCaption(var2);
         this.layoutPrefUi(var2);
         this.layoutTitle(var2);
         var2.decorate(var1);
      }
   }

   private void layoutCaption(OneColumnPanel var1) {
      if (this.caption != null) {
         var1.add(TextStyles.InfoText.makeLabel(this.caption));
      }
   }

   private void layoutPrefUi(final OneColumnPanel var1) {
      this.prefUi.decorate(new UiCanvas() {
         @Override
         public void display(Component var1x) {
            var1.noFill(var1x);
         }
      });
   }

   private void layoutTitle(OneColumnPanel var1) {
      if (this.title != null) {
         var1.withBorder(StyleSheet.getDefaultTitleBorder(this.title));
      }
   }
}
