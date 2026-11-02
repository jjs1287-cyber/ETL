package org.etlforge.ui.input;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import org.etlforge.collections.KongaMaps;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.action.ActionKeyBinding;
import org.etlforge.ui.input.undo.UndoableButtonGroup;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.layout.OneColumnPanel;
import org.etlforge.ui.layout.OneRowPanel;
import org.etlforge.ui.property.Binding;
import org.etlforge.ui.widget.button.ButtonUtils;
import org.etlforge.ui.widget.button.KongaRadioButton;
import org.etlforge.util.Property;

public class RadioButtonGroupPanel<T> {
   private final RadioButtonGroupPanel.ButtonMap<T> buttons = new RadioButtonGroupPanel.ButtonMap<>();
   private final UndoableButtonGroup undoableGroup;
   private final ButtonGroup standardGroup = new ButtonGroup();

   public static <E extends Enum<E>> RadioButtonGroupPanel<E> forEnum(Class<E> var0) {
      RadioButtonGroupPanel var1 = new RadioButtonGroupPanel();
      boolean var2 = true;

      for (Enum var6 : (Enum[])var0.getEnumConstants()) {
         var1.addOption(var6, var6.toString(), var2);
         var2 = false;
      }

      return var1;
   }

   public RadioButtonGroupPanel() {
      this(null);
   }

   public RadioButtonGroupPanel(InputPanel var1) {
      this.undoableGroup = new UndoableButtonGroup();
      if (var1 != null) {
         this.registerAsChangeSource(var1);
      }
   }

   public final void registerAsChangeSource(InputPanel var1) {
      var1.addChangeSource(this.undoableGroup);
   }

   public final JRadioButton addOption(T var1, String var2) {
      return this.addOption((T)var1, var2, false);
   }

   public final JRadioButton addOption(T var1, String var2, boolean var3) {
      KongaRadioButton var4 = new KongaRadioButton(var2, var3);
      this.standardGroup.add(var4);
      this.buttons.add(var4, (T)var1);
      if (this.undoableGroup != null) {
         this.undoableGroup.add(var4, var3);
      }

      return var4;
   }

   public final T getSelection() {
      return this.buttons.getSelection();
   }

   public final boolean hasSelection() {
      return this.standardGroup.getSelection() != null;
   }

   public final void setSelectedValue(T var1) {
      this.buttons.setSelectedValue((T)var1);
   }

   public final Binding bindWith(Property<T> var1) {
      return new RadioButtonGroupPanelBinding<>(var1, this);
   }

   public final void addSelectionListener(Receiver<T> var1) {
      this.buttons.addReceiver(var1);
   }

   public final void removeSelectionListener(Receiver<T> var1) {
      this.buttons.removeReceiver(var1);
   }

   public final void addActionListener(ActionListener var1) {
      this.buttons.addActionListener(var1);
   }

   public final void removeActionListener(ActionListener var1) {
      this.buttons.removeActionListener(var1);
   }

   public boolean contains(T var1) {
      return this.buttons.contains((T)var1);
   }

   public final UiProvider oneRowLayout() {
      return this.getUi(null, RadioButtonGroupPanel.Layout.ONE_ROW);
   }

   public final UiProvider getUi(JLabel var1, RadioButtonGroupPanel.Layout var2) {
      switch (var2) {
         case ONE_ROW:
            return new RadioButtonGroupPanel.OneRowLayout(var1);
         case ONE_COLUMN:
            return new RadioButtonGroupPanel.OneColumnLayout(var1);
         case TWO_ROWS:
            return var1 != null ? new RadioButtonGroupPanel.TwoRowsLayout(var1) : new RadioButtonGroupPanel.OneRowLayout(null);
         default:
            throw new IllegalArgumentException("Unknown layout: " + var2);
      }
   }

   public void requestFocus() {
      JRadioButton var1 = this.getButtonToFocus();
      var1.requestFocusInWindow();
   }

   private JRadioButton getButtonToFocus() {
      JRadioButton var1 = this.buttons.getSelectedButton();
      if (var1 == null) {
         var1 = this.buttons.getButtons().get(0);
      }

      return var1;
   }

   public final void shareModel(RadioButtonGroupPanel<T> var1) {
      if (this != var1) {
         for (Object var3 : this.buttons.getOptions()) {
            JRadioButton var4 = this.buttons.getButton((T)var3);
            JRadioButton var5 = var1.buttons.getButton((T)var3);
            if (var5 == null) {
               throw new IllegalArgumentException("Incompatible panels");
            }

            var4.setModel(var5.getModel());
         }
      }
   }

   public final void setEnabled(boolean var1) {
      this.buttons.setEnabled(var1);
   }

   public final void setUndoRedoPresentationName(String var1) {
      this.undoableGroup.setUndoRedoPresentationName(var1);
   }

   protected JComponent getButtonLayout(JRadioButton var1, T var2) {
      return var1;
   }

   private List<JComponent> getButtonLayouts() {
      ArrayList var1 = Lists.newArrayList();

      for (Object var3 : this.buttons.getOptions()) {
         JRadioButton var4 = this.buttons.getButton((T)var3);
         var1.add(this.getButtonLayout(var4, (T)var3));
      }

      return var1;
   }

   private void installLeftRightNavigation(JComponent var1) {
      ActionKeyBinding.install(new RadioButtonGroupPanel.NavigationAction(37), var1, 1);
      ActionKeyBinding.install(new RadioButtonGroupPanel.NavigationAction(39), var1, 1);
   }

   private static class ButtonMap<E> {
      private final Map<JRadioButton, E> buttonToOption = Maps.newLinkedHashMap();
      private final Map<E, JRadioButton> optionToButton = Maps.newLinkedHashMap();
      private final List<Receiver<E>> selectionListeners = new CopyOnWriteArrayList<>();
      private final ItemListener selectionDispatcher = new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent var1) {
            if (var1.getStateChange() == 1) {
               Object var2 = ButtonMap.this.getSelection();

               for (Receiver var4 : ButtonMap.this.selectionListeners) {
                  var4.handle(var2);
               }
            }
         }
      };

      private ButtonMap() {
      }

      public void add(JRadioButton var1, E var2) {
         this.buttonToOption.put(var1, (E)var2);
         this.optionToButton.put((E)var2, var1);
         var1.addItemListener(this.selectionDispatcher);
      }

      public List<JRadioButton> getButtons() {
         return Lists.newArrayList(this.buttonToOption.keySet());
      }

      public Collection<E> getOptions() {
         return this.optionToButton.keySet();
      }

      public boolean contains(E var1) {
         return this.optionToButton.containsKey(var1);
      }

      public JRadioButton getButton(E var1) {
         return this.optionToButton.get(var1);
      }

      public E getSelection() {
         return KongaMaps.findValue(this.buttonToOption, ButtonUtils.TOGGLE_BUTTON_SELECTED).orNull();
      }

      public void setSelectedValue(E var1) {
         JRadioButton var2 = this.optionToButton.get(var1);
         if (var2 != null) {
            var2.setSelected(true);
         } else {
            for (JRadioButton var4 : this.buttonToOption.keySet()) {
               var4.setSelected(false);
            }
         }
      }

      public void selectNext() {
         int var1 = this.getIndexOfSelectedButton();
         int var2 = ++var1;
         if (var1 == this.buttonToOption.size()) {
            var2 = 0;
         }

         this.selectButton(var2);
      }

      private int getIndexOfSelectedButton() {
         Object var1 = this.getSelection();
         return Lists.newArrayList(this.buttonToOption.values()).indexOf(var1);
      }

      private void selectButton(int var1) {
         JRadioButton var2 = this.getButtons().get(var1);
         var2.doClick();
      }

      public void selectPrevious() {
         int var1 = this.getIndexOfSelectedButton();
         int var2 = --var1;
         if (var2 < 0) {
            var2 = this.buttonToOption.size() - 1;
         }

         this.selectButton(var2);
      }

      public JRadioButton getSelectedButton() {
         int var1 = this.getIndexOfSelectedButton();
         return var1 >= 0 ? this.getButtons().get(var1) : null;
      }

      public void addReceiver(Receiver<E> var1) {
         this.selectionListeners.add(var1);
      }

      public void removeReceiver(Receiver<E> var1) {
         this.selectionListeners.remove(var1);
      }

      public void addActionListener(ActionListener var1) {
         for (JRadioButton var3 : this.buttonToOption.keySet()) {
            var3.addActionListener(var1);
         }
      }

      public void removeActionListener(ActionListener var1) {
         for (JRadioButton var3 : this.buttonToOption.keySet()) {
            var3.removeActionListener(var1);
         }
      }

      public void setEnabled(boolean var1) {
         for (JRadioButton var3 : this.buttonToOption.keySet()) {
            var3.setEnabled(var1);
         }
      }
   }

   public enum Layout {
      ONE_ROW,
      TWO_ROWS,
      ONE_COLUMN;
   }

   private class NavigationAction extends AbstractAction {
      private final int key;

      public NavigationAction(int var2) {
         this.key = var2;
         this.putValue("AcceleratorKey", KeyStroke.getKeyStroke(var2, 0));
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         switch (this.key) {
            case 37:
            case 38:
               RadioButtonGroupPanel.this.buttons.selectPrevious();
               break;
            case 39:
            case 40:
               RadioButtonGroupPanel.this.buttons.selectNext();
               break;
            default:
               throw new RuntimeException("Unexpected keycode: " + this.key);
         }
      }
   }

   private class OneColumnLayout implements UiProvider {
      private final OneColumnPanel column = new OneColumnPanel();

      public OneColumnLayout(JLabel var2) {
         this.column.withInsets(0, 0, 5, 0);
         if (var2 != null) {
            this.column.add(var2);
         }

         this.column.addAll(RadioButtonGroupPanel.this.getButtonLayouts().toArray());
         this.installNavigationActions();
      }

      private void installNavigationActions() {
         JPanel var1 = this.column.container();
         ActionKeyBinding.install(RadioButtonGroupPanel.this.new NavigationAction(40), var1, 1);
         ActionKeyBinding.install(RadioButtonGroupPanel.this.new NavigationAction(38), var1, 1);
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.column.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.column.requestFocus();
      }
   }

   private class OneRowLayout implements UiProvider {
      private final OneRowPanel row = new OneRowPanel();

      public OneRowLayout(JLabel var2) {
         this.row.setAnchor(OneRowPanel.WEST);
         this.row.withInsets(0, 0, 0, 12);
         if (var2 != null) {
            this.row.add(var2);
         }

         BoxBuilder var3 = BoxBuilder.horizontal();
         var3.addAllWithSpace(12, RadioButtonGroupPanel.this.getButtonLayouts());
         this.row.setWeightX(1.0);
         this.row.add(var3);
         RadioButtonGroupPanel.this.installLeftRightNavigation(this.row.container());
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.row.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.row.requestFocus();
      }
   }

   private class TwoRowsLayout implements UiProvider {
      private final OneColumnPanel column;

      public TwoRowsLayout(JLabel var2) {
         assert var2 != null;
         this.column = new OneColumnPanel();
         this.column.setWeightX(1.0);
         this.column.withInsets(0, 0, 5, 0);
         this.column.add(var2);
         this.column.add(RadioButtonGroupPanel.this.new OneRowLayout(null));
         RadioButtonGroupPanel.this.installLeftRightNavigation(this.column.container());
      }

      @Override
      public void decorate(UiCanvas var1) {
         this.column.decorate(var1);
      }

      @Override
      public void requestFocus() {
         this.column.requestFocus();
      }
   }
}
