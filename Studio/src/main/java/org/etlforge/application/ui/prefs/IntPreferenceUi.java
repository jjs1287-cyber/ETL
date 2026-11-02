package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import org.etlforge.application.prefs.IntPreference;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.input.IntegerInputField;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.DocumentContentChangeListener;
import org.etlforge.ui.widget.text.IntegerDocument;
import org.etlforge.ui.widget.text.KongaTextField;

public class IntPreferenceUi implements PreferenceUi {
   private static final IntPreferenceUi.SharedModels sharedModels = new IntPreferenceUi.SharedModels();
   private final IntPreference pref;
   private final IntegerInputField field;
   private final IntPreferenceUi.ChangeNotifier changeNotifier;

   public IntPreferenceUi(IntPreference var1, String var2, int var3) {
      Preconditions.checkNotNull(var1, "pref");
      this.pref = var1;
      this.field = this.createInputField(var2, var3);
      this.field.setMinimumValue(var1.min());
      this.field.setMaximumValue(var1.max());
      this.changeNotifier = new IntPreferenceUi.ChangeNotifier();
      this.field.getDocument().addDocumentListener(this.changeNotifier);
   }

   private IntegerInputField createInputField(String var1, int var2) {
      KongaTextField var3 = new KongaTextField(var2);
      var3.setFixedSize(true);
      var3.setDocument(new IntegerDocument());
      JLabel var4 = var1 != null && var1.length() > 0 ? new JLabel(var1) : null;
      return new IntegerInputField(var1, var3, Integer.MIN_VALUE, Integer.MAX_VALUE, var4);
   }

   @Override
   public void apply() {
      if (this.field.isFieldValid()) {
         this.pref.set(this.field.getIntValue());
      }
   }

   @Override
   public void restore() {
      this.field.setValue(this.pref.get());
   }

   @Override
   public void restoreDefault() {
      this.field.setValue(this.pref.getDefaultValue());
   }

   @Override
   public void share() {
      sharedModels.add(this);
   }

   @Override
   public void decorate(UiCanvas var1) {
      BoxBuilder var2 = BoxBuilder.horizontal();
      var2.addAllWithSpace(5, this.field.getLabel(), this.field.getInputComponent()).glue();
      var2.decorate(var1);
   }

   @Override
   public void requestFocus() {
      this.field.requestFocus();
   }

   @Override
   public boolean isDirty() {
      try {
         int var1 = this.field.getIntValue();
         return var1 != this.pref.get();
      } catch (NumberFormatException var2) {
         return true;
      }
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.changeNotifier.add(var1);
   }

   public void layout(TwoColumnLayout var1) {
      this.field.layoutIn(var1);
   }

   public void setSelectAllWhenFocused(boolean var1) {
      this.field.setSelectAllWhenFocused(var1);
   }

   public void setEnabled(boolean var1) {
      this.field.setEnabled(var1);
   }

   private class ChangeNotifier extends DocumentContentChangeListener {
      private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

      private ChangeNotifier() {
      }

      @Override
      public void contentChanged(DocumentEvent var1) {
         if (!this.listeners.isEmpty()) {
            ChangeEvent var2 = new ChangeEvent(IntPreferenceUi.this.field.getInputComponent());

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
      private final Set<IntPreferenceUi> sharingUis = new HashSet<>();
      private Document sharedModel;

      private SharedModels() {
      }

      public synchronized void add(IntPreferenceUi var1) {
         if (!this.sharingUis.contains(var1)) {
            this.sharingUis.add(var1);
            if (this.sharedModel == null) {
               this.sharedModel = var1.field.getDocument();
            } else {
               var1.field.setDocument(this.sharedModel);
            }
         }
      }
   }
}
