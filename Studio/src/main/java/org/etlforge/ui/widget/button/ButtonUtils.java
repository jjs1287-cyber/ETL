package org.etlforge.ui.widget.button;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import org.etlforge.ui.Empty;
import org.etlforge.ui.MnemonicString;
import org.etlforge.ui.util.border.BorderBuilder;
import org.etlforge.ui.widget.Label;
import org.etlforge.ui.widget.LabelAdapter;
import org.etlforge.ui.widget.text.TextDecorator;
import org.etlforge.ui.widget.text.TextDecoratorUtils;

public final class ButtonUtils {
   private static final Border normalIconButtonBorder = Empty.border(3);
   private static final Border rollOverIconButtonBorder = new BorderBuilder().etched().empty(1);
   public static final Function<Action, JButton> ACTION_TO_BUTTON = new Function<Action, JButton>() {
      public JButton apply(Action var1) {
         return var1 != null ? new KongaButton(var1) : null;
      }
   };
   public static final Function<Action, JButton> ACTION_TO_TOOLBAR = new Function<Action, JButton>() {
      public JButton apply(Action var1) {
         return var1 != null ? KongaButton.forToolbar(var1) : null;
      }
   };
   public static final Function<Action, JButton> ACTION_TO_SMALL_TOOLBAR = new Function<Action, JButton>() {
      public JButton apply(Action var1) {
         return var1 != null ? KongaButton.forToolbar(var1).setUseSmallActionIcon(true) : null;
      }
   };
   public static final Predicate<JToggleButton> TOGGLE_BUTTON_SELECTED = new Predicate<JToggleButton>() {
      public boolean apply(JToggleButton var1) {
         return var1.isSelected();
      }
   };

   public static JRadioButton[] getExclusiveRadioButtons(String[] var0) {
      JRadioButton[] var1 = new JRadioButton[var0.length];

      for (int var2 = 0; var2 < var0.length; var2++) {
         var1[var2] = new JRadioButton(var0[var2]);
      }

      makeMutuallyExclusive(var1);
      return var1;
   }

   public static <T extends AbstractButton> T decorate(T var0, Action var1) {
      var0.setText((String)var1.getValue("Name"));
      var0.setIcon((Icon)var1.getValue("SmallIcon"));
      var0.setToolTipText((String)var1.getValue("ShortDescription"));
      return (T)var0;
   }

   public static <T extends AbstractButton> T decorate(T var0, MnemonicString var1) {
      var0.setText(var1.getText());
      return applyMnemonics((T)var0, var1);
   }

   public static <T extends AbstractButton> T applyMnemonics(T var0, MnemonicString var1) {
      char var2 = var1.getMnemonic();
      if (var2 != 0) {
         var0.setMnemonic(var2);
      }

      int var3 = var1.getDisplayedMnemonicIndex();
      if (var3 >= 0) {
         var0.setDisplayedMnemonicIndex(var3);
      }

      return (T)var0;
   }

   public static JButton createLabelButton(Action var0, TextDecorator var1) {
      JButton var2 = new JButton(var0);
      var2.setContentAreaFilled(false);
      var2.setBorder(null);
      if (var1 != null) {
         TextDecoratorUtils.decorate(var2, var1);
      }

      return var2;
   }

   public static JButton findButtonForAction(Action var0, Component var1) {
      if (var1 instanceof JButton && ((JButton)var1).getAction() == var0) {
         return (JButton)var1;
      }

      if (var1 instanceof Container) {
         for (Component var5 : ((Container)var1).getComponents()) {
            JButton var6 = findButtonForAction(var0, var5);
            if (var6 != null) {
               return var6;
            }
         }
      }

      return null;
   }

   public static Label asLabel(JButton var0) {
      return new LabelAdapter(var0);
   }

   public static <T extends AbstractButton> T configureButtonForToolBar(T var0) {
      var0.setOpaque(false);
      var0.setContentAreaFilled(false);
      var0.setFocusPainted(false);
      enableRolloverForButton(var0);
      var0.setMnemonic(0);
      var0.setDisplayedMnemonicIndex(-1);
      return (T)var0;
   }

   public static void configureButtonsForToolBar(AbstractButton... var0) {
      for (AbstractButton var4 : var0) {
         configureButtonForToolBar(var4);
      }
   }

   public static <T extends AbstractButton> T enableRolloverForButton(final T var0) {
      var0.setBorder(normalIconButtonBorder);
      var0.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseEntered(MouseEvent var1) {
            var0.setBorder(ButtonUtils.rollOverIconButtonBorder);
         }

         @Override
         public void mouseExited(MouseEvent var1) {
            var0.setBorder(ButtonUtils.normalIconButtonBorder);
         }
      });
      return (T)var0;
   }

   public static ButtonGroup makeMutuallyExclusive(AbstractButton... var0) {
      return makeMutuallyExclusive(Arrays.asList(var0));
   }

   public static ButtonGroup makeMutuallyExclusive(Iterable<? extends AbstractButton> var0) {
      ButtonGroup var1 = new ButtonGroup();

      for (AbstractButton var3 : var0) {
         var1.add(var3);
      }

      return var1;
   }

   public static void addActionListener(ActionListener var0, AbstractButton... var1) {
      for (AbstractButton var5 : var1) {
         var5.addActionListener(var0);
      }
   }

   public static void addActionListener(ActionListener var0, Iterable<? extends AbstractButton> var1) {
      for (AbstractButton var3 : var1) {
         var3.addActionListener(var0);
      }
   }

   public static void addItemListener(ItemListener var0, AbstractButton... var1) {
      for (AbstractButton var5 : var1) {
         var5.addItemListener(var0);
      }
   }

   public static void addItemListener(ItemListener var0, Iterable<? extends AbstractButton> var1) {
      for (AbstractButton var3 : var1) {
         var3.addItemListener(var0);
      }
   }

   private ButtonUtils() {
   }
}
