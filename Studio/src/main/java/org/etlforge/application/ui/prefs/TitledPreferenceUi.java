package org.etlforge.application.ui.prefs;

import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;

public class TitledPreferenceUi implements PreferenceUi {
   private final PreferenceUi field;
   private final String title;
   private final String description;

   public TitledPreferenceUi(PreferenceUi var1, String var2, String var3) {
      this.field = var1;
      this.title = var2;
      this.description = var3;
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 5, 5, 5).withBorder(StyleSheet.getDefaultTitleBorder(this.title)).setWeightX(1.0);
      KongaLabel var3 = TextStyles.InfoText.makeLabel(this.description);
      var2.add(var3);
      var2.noFill(this.field);
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   @Override
   public void apply() {
      this.field.apply();
   }

   @Override
   public void restore() {
      this.field.restore();
   }

   @Override
   public void restoreDefault() {
      this.field.restoreDefault();
   }

   @Override
   public void share() {
      this.field.share();
   }

   @Override
   public boolean isDirty() {
      return this.field.isDirty();
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.field.addChangeListener(var1);
   }
}
