package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.prefs.BooleanPreference;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.widget.button.KongaCheckBox;

public class BooleanPreferenceUi implements PreferenceUi {
   private static final BooleanPreferenceUi.SharedModels sharedModels = new BooleanPreferenceUi.SharedModels();
   private final BooleanPreference pref;
   private final JCheckBox check;
   private final BooleanPreferenceUi.ChangeNotifier changeNotifier;

   public BooleanPreferenceUi(BooleanPreference var1, String var2) {
      Preconditions.checkNotNull(var1, "pref");
      this.pref = var1;
      this.check = new KongaCheckBox(var2);
      this.changeNotifier = new BooleanPreferenceUi.ChangeNotifier();
      this.check.addItemListener(this.changeNotifier);
   }

   @Override
   public void apply() {
      this.pref.set(this.check.isSelected());
   }

   @Override
   public void restore() {
      this.check.setSelected(this.pref.get());
   }

   @Override
   public void restoreDefault() {
      this.check.setSelected(this.pref.getDefaultValue());
   }

   @Override
   public void share() {
      sharedModels.add(this);
   }

   @Override
   public boolean isDirty() {
      return this.check.isSelected() != this.pref.get();
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.changeNotifier.add(var1);
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.check);
   }

   @Override
   public void requestFocus() {
      this.check.requestFocusInWindow();
   }

   public final boolean isSelected() {
      return this.check.isSelected();
   }

   public final void setEnabled(boolean var1) {
      this.check.setEnabled(var1);
   }

   private class ChangeNotifier implements ItemListener {
      private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

      private ChangeNotifier() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (!this.listeners.isEmpty()) {
            ChangeEvent var2 = new ChangeEvent(BooleanPreferenceUi.this.check);

            for (ChangeListener var4 : this.listeners) {
               var4.stateChanged(var2);
            }
         }
      }

      public void add(ChangeListener var1) {
         this.listeners.add(var1);
      }
   }

   private static class SharedModels {
      private final Set<BooleanPreferenceUi> sharingUis = new HashSet<>();
      private ButtonModel sharedModel;

      private SharedModels() {
      }

      public synchronized void add(BooleanPreferenceUi var1) {
         if (!this.sharingUis.contains(var1)) {
            this.sharingUis.add(var1);
            if (this.sharedModel == null) {
               this.sharedModel = var1.check.getModel();
            } else {
               var1.check.setModel(this.sharedModel);
            }
         }
      }
   }
}
