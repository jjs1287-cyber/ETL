package org.etlforge.application.ui.prefs;

import javax.swing.event.ChangeListener;
import org.etlforge.ui.UiCanvas;

public class PreferenceUiWrapper implements PreferenceUi {
   private final PreferenceUi wrappee;
   private final PreferenceUiLayout layout;

   public PreferenceUiWrapper(PreferenceUi var1) {
      this.wrappee = var1;
      this.layout = new PreferenceUiLayout(var1);
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.wrappee.addChangeListener(var1);
   }

   @Override
   public void apply() {
      this.wrappee.apply();
   }

   @Override
   public boolean isDirty() {
      return this.wrappee.isDirty();
   }

   @Override
   public void restore() {
      this.wrappee.restore();
   }

   @Override
   public void restoreDefault() {
      this.wrappee.restoreDefault();
   }

   @Override
   public void share() {
      this.wrappee.share();
   }

   @Override
   public void decorate(UiCanvas var1) {
      this.layout.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.wrappee.requestFocus();
   }

   public final PreferenceUiLayout getLayout() {
      return this.layout;
   }
}
