package org.etlforge.util.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public final class ClipboardUtils {
   private static final ClipboardOwner OWNER = new ClipboardOwner() {
      @Override
      public void lostOwnership(Clipboard var1, Transferable var2) {
      }
   };

   public static Clipboard getSystemClipboard() {
      return Toolkit.getDefaultToolkit().getSystemClipboard();
   }

   public static void copyToSystemClipboard(String var0) {
      copyToSystemClipboard(new StringSelection(var0));
   }

   public static void copyToSystemClipboard(Transferable var0) {
      getSystemClipboard().setContents(var0, OWNER);
   }

   public static Object getSystemClipboardContents(DataFlavor var0) {
      Clipboard var1 = getSystemClipboard();
      if (var1.isDataFlavorAvailable(var0)) {
         try {
            return var1.getData(var0);
         } catch (UnsupportedFlavorException var3) {
            var3.printStackTrace();
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

      return null;
   }

   private ClipboardUtils() {
   }
}
