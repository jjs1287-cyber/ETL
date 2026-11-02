package org.etlforge.ui.layout;

import java.awt.Component;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.ui.style.TextStyle;
import org.etlforge.ui.widget.KongaScrollPane;
import org.etlforge.ui.widget.button.KongaButton;
import org.etlforge.ui.widget.panel.InvisiblePanel;

public abstract class AbstractLayoutBuilder {
   private boolean automaticallyWrapScrollables = true;
   private boolean useSmallSizedIconsForActions;
   private boolean useMediumSizedIconsForActions;
   private TextStyle buttonStyle;

   protected Component forComponent(Component var1) {
      if (isSubjectForScroll(var1) && this.isAutomaticallyWrappingScrollables()) {
         return new JScrollPane(var1);
      } else {
         return var1 instanceof JCheckBox ? InvisiblePanel.of(var1) : var1;
      }
   }

   private static boolean isSubjectForScroll(Component var0) {
      return var0 instanceof Scrollable && !(var0 instanceof JTextField) && !(var0 instanceof JPasswordField);
   }

   protected JComponent forString(String var1) {
      return new JLabel(var1);
   }

   protected JComponent forAction(Action var1) {
      KongaButton var2 = new KongaButton(var1);
      if (this.useSmallSizedIconsForActions) {
         var2.setUseSmallActionIcon(true);
      } else if (this.useMediumSizedIconsForActions) {
         var2.setUseMediumActionIcon(true);
      }

      if (this.buttonStyle != null) {
         this.buttonStyle.makeOver(var2);
      }

      return var2;
   }

   protected JComponent forIcon(Icon var1) {
      return new JLabel(var1);
   }

   public final boolean isAutomaticallyWrappingScrollables() {
      return this.automaticallyWrapScrollables;
   }

   public AbstractLayoutBuilder setAutomaticallyWrapScrollables(boolean var1) {
      this.automaticallyWrapScrollables = var1;
      return this;
   }

   protected JComponent forObject(Object var1) {
      throw new IllegalArgumentException("I do not know how to add an object of type " + var1.getClass().getName());
   }

   public void setUseSmallSizedIconsForActions(boolean var1) {
      this.useSmallSizedIconsForActions = var1;
      if (var1) {
         this.useMediumSizedIconsForActions = false;
      }
   }

   public final void setUseMediumSizedIconsForActions(boolean var1) {
      this.useMediumSizedIconsForActions = var1;
      if (var1) {
         this.useSmallSizedIconsForActions = false;
      }
   }

   public final void setButtonStyle(TextStyle var1) {
      this.buttonStyle = var1;
   }

   public abstract JComponent container();

   public final JPanel invisible() {
      return InvisiblePanel.of(this.container());
   }

   public final KongaScrollPane inScroll() {
      return new KongaScrollPane(this.container());
   }

   public final void repaint() {
      this.container().repaint();
   }
}
