package org.etlforge.ui.widget.list;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import org.etlforge.system.Disposable;
import org.etlforge.ui.util.KeyUtils;

public final class ListContainerKeyBindingsInstaller implements Disposable {
   private static final KeyStroke[] TO_TRANSFER = new KeyStroke[]{
      KeyUtils.DOWN, KeyUtils.getShiftDown(40), KeyUtils.UP, KeyUtils.getShiftDown(38), KeyUtils.getCtrlDown(65)
   };
   private final JComponent target;
   private final List<ListContainerKeyBindingsInstaller.KeyBindingMemento> toRestore;

   public ListContainerKeyBindingsInstaller(JComponent var1) {
      this.target = var1;
      this.toRestore = new ArrayList<>();
   }

   public void install(JList var1) {
      this.install(var1, 0);
   }

   public void install(JTable var1) {
      this.install(var1, 1);
   }

   private void install(JComponent var1, int var2) {
      InputMap var3 = var1.getInputMap(var2);
      ActionMap var4 = var1.getActionMap();

      for (KeyStroke var8 : TO_TRANSFER) {
         ListContainerKeyBindingsInstaller.KeyBindingMemento var9 = this.install(var1, var8, var3, var4);
         if (var9 != null) {
            this.toRestore.add(var9);
         }
      }
   }

   @Override
   public void dispose() {
      for (ListContainerKeyBindingsInstaller.KeyBindingMemento var2 : this.toRestore) {
         var2.restore(this.target);
      }
   }

   private ListContainerKeyBindingsInstaller.KeyBindingMemento install(JComponent var1, KeyStroke var2, InputMap var3, ActionMap var4) {
      Object var5 = var3.get(var2);
      if (var5 == null) {
         return null;
      }

      Action var6 = var4.get(var5);
      return var6 == null ? null : this.copyBinding(var2, var5, var6, var1);
   }

   private ListContainerKeyBindingsInstaller.KeyBindingMemento copyBinding(KeyStroke var1, Object var2, final Action var3, final JComponent var4) {
      InputMap var5 = this.target.getInputMap(1);
      ActionMap var6 = this.target.getActionMap();
      Object var7 = var5.get(var1);
      Action var8 = var7 != null ? var6.get(var7) : null;
      ListContainerKeyBindingsInstaller.KeyBindingMemento var9 = new ListContainerKeyBindingsInstaller.KeyBindingMemento(var1, var7, var8);
      var5.put(var1, var2);
      var6.put(var2, new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent var1) {
            var3.actionPerformed(new ActionEvent(var4, var1.getID(), var1.getActionCommand()));
         }
      });
      return var9;
   }

   private static final class KeyBindingMemento {
      private final KeyStroke keyStroke;
      private final Object mapKey;
      private final Action action;

      public KeyBindingMemento(KeyStroke var1, Object var2, Action var3) {
         this.keyStroke = var1;
         this.mapKey = var2;
         this.action = var3;
      }

      public void restore(JComponent var1) {
         InputMap var2 = var1.getInputMap(1);
         var2.put(this.keyStroke, this.mapKey);
         if (this.mapKey != null) {
            var1.getActionMap().put(this.mapKey, this.action);
         }
      }
   }
}
