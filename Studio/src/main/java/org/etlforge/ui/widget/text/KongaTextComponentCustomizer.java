package org.etlforge.ui.widget.text;

import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultEditorKit.CopyAction;
import javax.swing.text.DefaultEditorKit.PasteAction;
import org.etlforge.ui.event.MouseActionListener;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.util.KeyUtils;
import org.etlforge.ui.widget.popup.KongaPopupMenu;

public final class KongaTextComponentCustomizer {
   public static void customize(JTextComponent var0) {
      registerKeyBindings(var0);
      new KongaTextComponentCustomizer.PopupHandler(var0);
   }

   public static void disablePopup(JTextComponent var0) {
      MouseListener[] var1 = var0.getMouseListeners();

      for (MouseListener var5 : var1) {
         if (var5 instanceof KongaTextComponentCustomizer.PopupHandler) {
            var0.removeMouseListener(var5);
         }
      }
   }

   static void registerKeyBindings(JTextComponent var0) {
      KongaTextComponentCustomizer.PopupKeyShortCut.install(var0);
   }

   static <T extends JTextComponent> T disableTabs(T var0) {
      HashSet var1 = new HashSet();
      var1.add(KeyStroke.getKeyStroke(9, 0));
      var0.setFocusTraversalKeys(0, var1);
      var1 = new HashSet();
      var1.add(KeyStroke.getKeyStroke(9, 1));
      var0.setFocusTraversalKeys(1, var1);
      return (T)var0;
   }

   static KongaPopupMenu getPopupMenu(JTextComponent var0) {
      HashMap var1 = Maps.newHashMap();
      Action[] var2 = var0.getActions();

      for (int var3 = 0; var3 < var2.length; var3++) {
         Action var4 = var2[var3];
         var1.put(var4.getValue("Name"), var4);
      }

      boolean var10 = var0.getSelectionStart() != var0.getSelectionEnd();
      boolean var11 = var0.isEnabled();
      boolean var5 = var0.isEditable();
      boolean var6 = isPasswordField(var0);
      KongaPopupMenu var7 = new KongaPopupMenu();
      Action var8 = (Action)var1.get("cut-to-clipboard");
      var8.setEnabled(var10 && var11 && var5 && !var6);
      JMenuItem var9 = new JMenuItem(var8);
      var9.setText(UiResourceBundle.getString("Cut"));
      var7.add(var9);
      var8 = new CopyAction();
      var8.setEnabled(var10 && !var6);
      var9 = new JMenuItem(var8);
      var9.setText(UiResourceBundle.getString("Copy"));
      var7.add(var9);
      PasteAction var13 = new PasteAction();
      var13.setEnabled(var11 && var5);
      var9 = new JMenuItem(var13);
      var9.setText(UiResourceBundle.getString("Paste"));
      var7.add(var9);
      KongaTextComponentCustomizer.DeleteAction var14 = new KongaTextComponentCustomizer.DeleteAction(var0);
      var14.setEnabled(var10 && var11 && var5);
      var7.add(var14);
      var7.addSeparator();
      var7.add(new KongaTextComponentCustomizer.SelectAllAction(var0));
      return var7;
   }

   private static boolean isPasswordField(JTextComponent var0) {
      return var0 instanceof KongaPasswordField ? !((KongaPasswordField)var0).isShowPassword() : var0 instanceof JPasswordField;
   }

   private KongaTextComponentCustomizer() {
   }

   private static class DeleteAction extends AbstractAction {
      private final JTextComponent component;

      public DeleteAction(JTextComponent var1) {
         super(UiResourceBundle.getString("Delete"));
         this.component = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            int var2 = this.component.getSelectionStart();
            int var3 = this.component.getSelectionEnd() - var2;
            this.component.getDocument().remove(var2, var3);
         } catch (BadLocationException var4) {
            var4.printStackTrace();
         }
      }
   }

   private static class PopupHandler extends MouseActionListener {
      private final JTextComponent component;

      public PopupHandler(JTextComponent var1) {
         this.component = var1;
         this.component.addMouseListener(this);
      }

      @Override
      protected void onPopupTrigger(MouseEvent var1) {
         this.component.requestFocusInWindow();
         KongaTextComponentCustomizer.getPopupMenu(this.component).show(this.component, var1.getX(), var1.getY());
      }
   }

   private static final class PopupKeyShortCut extends AbstractAction {
      private final JTextComponent component;

      public static void install(JTextComponent var0) {
         KongaTextComponentCustomizer.PopupKeyShortCut var1 = new KongaTextComponentCustomizer.PopupKeyShortCut(var0);
         String var2 = "kongatextcomponent.contextmenu";
         var0.getInputMap().put(KeyUtils.CONTEXT_MENU, var2);
         var0.getActionMap().put(var2, var1);
      }

      private PopupKeyShortCut(JTextComponent var1) {
         this.component = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.component.requestFocusInWindow();
         KongaTextComponentCustomizer.getPopupMenu(this.component).show(this.component, 4, 4);
      }
   }

   private static class SelectAllAction extends AbstractAction {
      private final JTextComponent component;

      public SelectAllAction(JTextComponent var1) {
         super(UiResourceBundle.getString("SelectAll"));
         this.component = var1;
         this.setEnabled(this.component.getDocument().getLength() > 0);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         int var2 = this.component.getDocument().getLength();
         this.component.setCaretPosition(0);
         this.component.moveCaretPosition(var2);
      }
   }
}
