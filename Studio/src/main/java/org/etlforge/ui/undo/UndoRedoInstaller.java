package org.etlforge.ui.undo;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.Container;
import java.util.Map;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;
import org.etlforge.system.callback.Acceptor;
import org.etlforge.ui.action.ActionDecorator;
import org.etlforge.ui.widget.ComponentTraverser;
import org.etlforge.ui.widget.combobox.undo.ComboBoxUndoRedoControllerFactory;
import org.etlforge.ui.widget.text.undo.TextUndoRedoControllerFactory;
import org.etlforge.ui.widget.toggle.ToggleButtonUndoRedoControllerFactory;

public final class UndoRedoInstaller {
   private final TextUndoRedoControllerFactory textFactory = new TextUndoRedoControllerFactory();
   private final ComboBoxUndoRedoControllerFactory comboFactory = new ComboBoxUndoRedoControllerFactory();
   private final ToggleButtonUndoRedoControllerFactory toggleFactory = new ToggleButtonUndoRedoControllerFactory();
   private final Map<Component, UndoRedoController> installedControllers = Maps.newIdentityHashMap();

   public boolean install(Component var1) {
      if (this.installedControllers.containsKey(var1)) {
         return true;
      }

      UndoRedoController var2 = null;
      if (var1 instanceof JTextComponent) {
         var2 = this.textFactory.newController((JTextComponent)var1);
      } else if (var1 instanceof JComboBox) {
         var2 = this.comboFactory.newController((JComboBox)var1);
      } else if (var1 instanceof JCheckBox) {
         var2 = this.toggleFactory.newController((JCheckBox)var1);
      }

      if (var2 != null) {
         this.installedControllers.put(var1, var2);
         return true;
      } else {
         return false;
      }
   }

   public void installIntoComponentTree(Container var1) {
      ComponentTraverser.traverse(var1, new Acceptor<Component>() {
         public boolean accept(Component var1) {
            UndoRedoInstaller.this.install(var1);
            return true;
         }
      });
   }

   public void dispose() {
      for (UndoRedoController var2 : this.installedControllers.values()) {
         var2.dispose();
      }

      this.installedControllers.clear();
   }

   public void decorateUndoAction(ActionDecorator var1) {
      this.textFactory.setUndoActionDecorator(var1);
      this.comboFactory.setUndoActionDecorator(var1);
      this.toggleFactory.setUndoActionDecorator(var1);
   }

   public void decorateRedoAction(ActionDecorator var1) {
      this.textFactory.setRedoActionDecorator(var1);
      this.comboFactory.setRedoActionDecorator(var1);
      this.toggleFactory.setRedoActionDecorator(var1);
   }
}
