package org.etlforge.ui.widget.text;

import java.io.File;
import java.util.List;
import javax.swing.text.JTextComponent;

public interface FileDropStrategy {
   FileDropStrategy INSERT_FULL_PATH = new FileDropStrategy() {
      @Override
      public boolean canDrop(JTextComponent var1) {
         return true;
      }

      @Override
      public boolean handleDrop(JTextComponent var1, List<File> var2) {
         if (var2.size() == 1) {
            File var3 = (File)var2.get(0);
            return DropStrategyUtils.insertAtCursor(var1, var3.getAbsolutePath());
         } else {
            return false;
         }
      }
   };
   FileDropStrategy INSERT_NAME = new FileDropStrategy() {
      @Override
      public boolean canDrop(JTextComponent var1) {
         return true;
      }

      @Override
      public boolean handleDrop(JTextComponent var1, List<File> var2) {
         if (var2.size() == 1) {
            File var3 = (File)var2.get(0);
            return DropStrategyUtils.insertAtCursor(var1, var3.getName());
         } else {
            return false;
         }
      }
   };
   FileDropStrategy SET_FULL_PATH = new FileDropStrategy() {
      @Override
      public boolean canDrop(JTextComponent var1) {
         return true;
      }

      @Override
      public boolean handleDrop(JTextComponent var1, List<File> var2) {
         if (var2.size() == 1) {
            File var3 = (File)var2.get(0);
            var1.setText(var3.getAbsolutePath());
            return true;
         } else {
            return false;
         }
      }
   };
   FileDropStrategy SET_NAME = new FileDropStrategy() {
      @Override
      public boolean canDrop(JTextComponent var1) {
         return true;
      }

      @Override
      public boolean handleDrop(JTextComponent var1, List<File> var2) {
         if (var2.size() == 1) {
            File var3 = (File)var2.get(0);
            var1.setText(var3.getName());
            return true;
         } else {
            return false;
         }
      }
   };

   boolean canDrop(JTextComponent var1);

   boolean handleDrop(JTextComponent var1, List<File> var2);
}
