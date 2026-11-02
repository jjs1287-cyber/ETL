package org.etlforge.ui;

import com.google.common.base.Preconditions;
import java.awt.Component;
import java.awt.event.FocusListener;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import org.etlforge.ui.util.focus.Focusable;

public class UiProviderAdapter implements UiProvider, Focusable {
   private final Component component;
   private boolean suppressScroll;

   public static UiProviderAdapter of(Component var0) {
      return new UiProviderAdapter(var0);
   }

   public static UiProviderAdapter label(String var0) {
      return new UiProviderAdapter(new JLabel(var0));
   }

   public UiProviderAdapter(Component var1) {
      this.component = Preconditions.checkNotNull(var1, "component");
   }

   @Override
   public void decorate(UiCanvas var1) {
      Component var2 = isSubjectForScroll(this.component) && !this.suppressScroll ? new JScrollPane(this.component) : this.component;
      var1.display(var2);
   }

   @Override
   public void requestFocus() {
      this.component.requestFocusInWindow();
   }

   public void setSuppressScroll(boolean var1) {
      this.suppressScroll = var1;
   }

   private static boolean isSubjectForScroll(Component var0) {
      return var0 instanceof Scrollable && !(var0 instanceof JTextField) && !(var0 instanceof JPasswordField);
   }

   public Component getAdaptee() {
      return this.component;
   }

   @Override
   public void addFocusListener(FocusListener var1) {
      this.component.addFocusListener(var1);
   }

   @Override
   public void removeFocusListener(FocusListener var1) {
      this.component.removeFocusListener(var1);
   }
}
