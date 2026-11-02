package org.etlforge.application.ui.prefs;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.TextStyles;
import org.etlforge.application.ui.resources.CommonIcons;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.widget.label.KongaLabel;

public abstract class AbstractPreferencesPage implements PreferencesPage {
   private final String[] path;
   private final List<PreferenceUi> controls;

   protected AbstractPreferencesPage(String[] var1) {
      this.path = (String[])var1.clone();
      this.controls = new ArrayList<>();
   }

   @Override
   public final String[] getPath() {
      return (String[])this.path.clone();
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      for (PreferenceUi var3 : this.controls) {
         var3.addChangeListener(var1);
      }
   }

   @Override
   public void display(Container var1) {
      OneColumnPanel var2 = new OneColumnPanel();
      var2.withInsets(10, 10, 10, 10);
      var2.setWeightX(1.0);
      this.layoutControls(var2);
      var1.setLayout(new BorderLayout());
      var2.addToParent(var1, "North");
   }

   @Override
   public boolean isDirty() {
      for (PreferenceUi var2 : this.controls) {
         if (var2.isDirty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void apply() {
      for (PreferenceUi var2 : this.controls) {
         var2.apply();
      }
   }

   @Override
   public void restore() {
      for (PreferenceUi var2 : this.controls) {
         var2.restore();
      }
   }

   @Override
   public void restoreDefaults() {
      for (PreferenceUi var2 : this.controls) {
         var2.restoreDefault();
      }
   }

   @Override
   public void requestFocus() {
      if (!this.controls.isEmpty()) {
         this.controls.get(0).requestFocus();
      }
   }

   protected final void addControl(PreferenceUi var1) {
      this.controls.add(var1);
   }

   protected final void addControls(PreferenceUi... var1) {
      for (PreferenceUi var5 : var1) {
         this.addControl(var5);
      }
   }

   protected final void addControls(Iterable<? extends PreferenceUi> var1) {
      for (PreferenceUi var3 : var1) {
         this.addControl(var3);
      }
   }

   protected final Iterable<PreferenceUi> getControls() {
      return this.controls;
   }

   protected void layoutControls(UiCanvas var1) {
      for (PreferenceUi var3 : this.controls) {
         var3.decorate(var1);
      }
   }

   public static JLabel getRequiresRestartNote() {
      KongaLabel var0 = TextStyles.InfoText.makeLabel("Requires a restart of the application");
      var0.setIcon(CommonIcons.INFO_16);
      return var0;
   }
}
