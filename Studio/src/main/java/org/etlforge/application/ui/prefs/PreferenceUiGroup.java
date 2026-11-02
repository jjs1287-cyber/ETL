package org.etlforge.application.ui.prefs;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import org.etlforge.application.ui.look.StyleSheet;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.layout.OneColumnPanel;

public final class PreferenceUiGroup implements PreferenceUi {
   private final ImmutableList<PreferenceUi> prefs;
   private final String title;
   private Optional<JLabel> caption = Optional.absent();

   public PreferenceUiGroup(String var1, PreferenceUi var2, PreferenceUi var3, PreferenceUi... var4) {
      this(var1, toList(var2, var3, var4));
   }

   private static ImmutableList<PreferenceUi> toList(PreferenceUi var0, PreferenceUi var1, PreferenceUi... var2) {
      Builder var3 = ImmutableList.builder();
      var3.add(var0, var1);
      var3.add(var2);
      return var3.build();
   }

   public PreferenceUiGroup(String var1, Iterable<PreferenceUi> var2) {
      this(var1, ImmutableList.copyOf(var2));
   }

   public PreferenceUiGroup(String var1, ImmutableList<PreferenceUi> var2) {
      this.title = var1;
      this.prefs = var2;
   }

   public void setCaption(JLabel var1) {
      this.caption = Optional.of(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneColumnPanel var2 = new OneColumnPanel().withInsets(5, 5, 5, 5).withBorder(StyleSheet.getDefaultTitleBorder(this.title)).setWeightX(1.0);
      if (this.caption.isPresent()) {
         var2.add(this.caption.get());
      }

      for (PreferenceUi var4 : this.prefs) {
         var2.add(var4);
      }

      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.prefs.get(0).requestFocus();
   }

   @Override
   public void apply() {
      for (PreferenceUi var2 : this.prefs) {
         var2.apply();
      }
   }

   @Override
   public void restore() {
      for (PreferenceUi var2 : this.prefs) {
         var2.restore();
      }
   }

   @Override
   public void restoreDefault() {
      for (PreferenceUi var2 : this.prefs) {
         var2.restoreDefault();
      }
   }

   @Override
   public boolean isDirty() {
      for (PreferenceUi var2 : this.prefs) {
         if (var2.isDirty()) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      for (PreferenceUi var3 : this.prefs) {
         var3.addChangeListener(var1);
      }
   }

   @Override
   public void share() {
      for (PreferenceUi var2 : this.prefs) {
         var2.share();
      }
   }
}
