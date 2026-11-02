package org.etlforge.application.ui.prefs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.etlforge.application.prefs.FixedSetPreference;
import org.etlforge.lang.exception.NotImplementedYetException;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.input.RadioButtonGroupPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.layout.TwoColumnLayout;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public class FixedSetPreferenceUi<T> implements PreferenceUi {
   private final FixedSetPreference<T> pref;
   private final JLabel label;
   private final FixedSetPreferenceUi.Selector<T> selector;
   private final FixedSetPreferenceUi<T>.ChangeNotifier changeNotifier;
   private Border border;

   public FixedSetPreferenceUi(FixedSetPreference<T> var1, String var2, boolean var3) {
      Preconditions.checkNotNull(var1, "pref");
      this.pref = var1;
      this.label = new JLabel(var2);
      this.changeNotifier = new FixedSetPreferenceUi.ChangeNotifier();
      this.selector = this.createSelector(var1, var3);
   }

   private FixedSetPreferenceUi.Selector<T> createSelector(FixedSetPreference<T> var1, boolean var2) {
      ImmutableList var3 = var1.getValues();
      int var4 = var3.size();
      if (var2) {
         var4++;
      }

      return var4 <= 3 ? new FixedSetPreferenceUi.RadioButtonSelector(var3, var2) : new FixedSetPreferenceUi.ComboBoxSelector(var3, var2);
   }

   @Override
   public final void apply() {
      Object var1 = this.selector.getSelection();
      this.pref.set((T)var1);
   }

   @Override
   public final void restore() {
      this.selector.setSelection(this.pref.get());
   }

   @Override
   public final void restoreDefault() {
      this.selector.setSelection(this.pref.getDefaultValue());
   }

   @Override
   public void share() {
      throw new NotImplementedYetException();
   }

   @Override
   public boolean isDirty() {
      return this.selector.getSelection() != this.pref.get();
   }

   @Override
   public void addChangeListener(ChangeListener var1) {
      this.changeNotifier.add(var1);
   }

   public final void setBorder(Border var1) {
      this.border = var1;
   }

   @Override
   public void decorate(UiCanvas var1) {
      OneRowPanel var2 = new OneRowPanel().withBorder(this.border).withInsets(0, 0, 0, 5);
      var2.setAnchor(OneRowPanel.WEST);
      var2.add(this.label);
      var2.withInsets(0, 0, 0, 0);
      var2.add(this.selector);
      var2.setWeightX(1.0);
      var2.add(Box.createHorizontalGlue());
      var2.decorate(var1);
   }

   @Override
   public final void requestFocus() {
      this.selector.requestFocus();
   }

   protected String getDisplayString(T var1) {
      return var1.toString();
   }

   protected Icon getIcon(T var1) {
      return null;
   }

   public void layout(TwoColumnLayout var1) {
      JPanel var2 = InvisiblePanel.of(this.selector);
      var1.addRow(this.label, var2);
   }

   protected boolean include(T var1) {
      return true;
   }

   protected final UiProvider getSelector() {
      return this.selector;
   }

   protected RadioButtonGroupPanel.Layout getRadioButtonLayout() {
      return RadioButtonGroupPanel.Layout.ONE_ROW;
   }

   public final void setSelectedValue(T var1) {
      this.selector.setSelection((T)var1);
   }

   public final void setEnabled(boolean var1) {
      this.label.setEnabled(var1);
      this.selector.setEnabled(var1);
   }

   private class ChangeNotifier implements ItemListener {
      private final List<ChangeListener> listeners = new CopyOnWriteArrayList<>();

      private ChangeNotifier() {
      }

      @Override
      public void itemStateChanged(ItemEvent var1) {
         if (!this.listeners.isEmpty()) {
            ChangeEvent var2 = new ChangeEvent(var1.getSource());

            for (ChangeListener var4 : this.listeners) {
               var4.stateChanged(var2);
            }
         }
      }

      public void add(ChangeListener var1) {
         this.listeners.add(var1);
      }
   }

   private class ComboBoxSelector implements FixedSetPreferenceUi.Selector<T> {
      private final JComboBox comboBox;

      public ComboBoxSelector(Iterable<T> var2, boolean var3) {
         this.comboBox = this.createComboBox(var2, var3);
      }

      private JComboBox createComboBox(Iterable<T> var1, boolean var2) {
         ArrayList var3 = new ArrayList();
         if (var2) {
            var3.add(null);
         }

         for (Object var5 : var1) {
            if (FixedSetPreferenceUi.this.include((T)var5)) {
               var3.add(var5);
            }
         }

         JComboBox var6 = new JComboBox<>(var3.toArray());
         var6.setRenderer(new FixedSetPreferenceUi.ComboBoxSelector.Renderer());
         var6.addItemListener(FixedSetPreferenceUi.this.changeNotifier);
         return var6;
      }

      @Override
      public T getSelection() {
         return (T)this.comboBox.getSelectedItem();
      }

      @Override
      public void setSelection(T var1) {
         this.comboBox.setSelectedItem(var1);
      }

      @Override
      public void setEnabled(boolean var1) {
         this.comboBox.setEnabled(var1);
      }

      @Override
      public void requestFocus() {
         this.comboBox.requestFocus();
      }

      @Override
      public void decorate(UiCanvas var1) {
         var1.display(this.comboBox);
      }

      private class Renderer extends DefaultListCellRenderer {
         private Renderer() {
         }

         @Override
         public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
            super.getListCellRendererComponent(var1, var2, var3, var4, var5);
            if (var2 == null) {
               this.setText("(None)");
               this.setIcon(null);
            } else {
               Object var6 = var2;
               this.setText(FixedSetPreferenceUi.this.getDisplayString((T)var6));
               this.setIcon(FixedSetPreferenceUi.this.getIcon((T)var6));
            }

            return this;
         }
      }
   }

   private class RadioButtonSelector implements FixedSetPreferenceUi.Selector<T> {
      private final RadioButtonGroupPanel<T> panel = new RadioButtonGroupPanel<>();
      private final UiProvider ui;

      public RadioButtonSelector(Iterable<T> var2, boolean var3) {
         if (var3) {
            this.addNone();
         }

         this.addValues(var2);
         this.ui = this.createUi();
      }

      private void addNone() {
         this.panel.addOption(null, "None");
      }

      private void addValues(Iterable<T> var1) {
         for (Object var3 : var1) {
            if (FixedSetPreferenceUi.this.include((T)var3)) {
               JRadioButton var4 = this.panel.addOption((T)var3, FixedSetPreferenceUi.this.getDisplayString((T)var3));
               var4.addItemListener(FixedSetPreferenceUi.this.changeNotifier);
            }
         }
      }

      private UiProvider createUi() {
         return this.panel.getUi(null, FixedSetPreferenceUi.this.getRadioButtonLayout());
      }

      @Override
      public T getSelection() {
         return this.panel.getSelection();
      }

      @Override
      public void setSelection(T var1) {
         this.panel.setSelectedValue((T)var1);
      }

      @Override
      public void setEnabled(boolean var1) {
         this.panel.setEnabled(var1);
      }

      @Override
      public void requestFocus() {
         this.ui.requestFocus();
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.ui.decorate(var1);
      }
   }

   private interface Selector<E> extends UiProvider {
      E getSelection();

      void setSelection(E var1);

      void setEnabled(boolean var1);
   }
}
