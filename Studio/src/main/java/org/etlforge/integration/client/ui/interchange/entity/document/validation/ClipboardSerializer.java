package org.etlforge.integration.client.ui.interchange.entity.document.validation;

import com.google.common.base.Optional;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.etlforge.integration.data.structure.text.validation.FieldValidation;
import org.etlforge.system.Disposable;
import org.etlforge.system.callback.Receiver;
import org.etlforge.util.clipboard.ClipboardUtils;
import org.etlforge.util.persist.XmlPersistor;

final class ClipboardSerializer {
   public static void toClipboard(FieldValidation var0) {
      ClipboardSerializer.TransferObject var1 = new ClipboardSerializer.TransferObject(var0);
      ClipboardUtils.copyToSystemClipboard(var1);
   }

   public static Optional<FieldValidation> fromClipboard() {
      Object var0 = ClipboardUtils.getSystemClipboardContents(ClipboardSerializer.TransferObject.FLAVOR);
      return var0 != null ? (Optional)var0 : Optional.absent();
   }

   public static Disposable registerPasteAvailableCallback(final Receiver<Boolean> var0) {
      final Clipboard var1 = ClipboardUtils.getSystemClipboard();
      final FlavorListener var2 = new FlavorListener() {
         @Override
         public void flavorsChanged(FlavorEvent var1x) {
            ClipboardSerializer.notifyCallback(var1, var0);
         }
      };
      var1.addFlavorListener(var2);
      notifyCallback(var1, var0);
      return new Disposable() {
         @Override
         public void dispose() {
            var1.removeFlavorListener(var2);
         }
      };
   }

   private static void notifyCallback(Clipboard var0, Receiver<Boolean> var1) {
      boolean var2 = var0.isDataFlavorAvailable(ClipboardSerializer.TransferObject.FLAVOR);
      var1.handle(var2);
   }

   private ClipboardSerializer() {
   }

   private static class TransferObject implements Transferable {
      public static final DataFlavor FLAVOR = new DataFlavor(FieldValidation.class, "Field Validation Definition");
      private final String xml;

      public TransferObject(FieldValidation var1) {
         this.xml = toXml(var1);
      }

      private static String toXml(FieldValidation var0) {
         XmlPersistor var1 = XmlPersistor.newRoot("FieldValidation");
         var0.writeTo(var1);
         return var1.toXml();
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
         return new DataFlavor[]{FLAVOR, DataFlavor.stringFlavor};
      }

      @Override
      public boolean isDataFlavorSupported(DataFlavor var1) {
         return var1.equals(FLAVOR) || var1.equals(DataFlavor.stringFlavor);
      }

      @Override
      public Object getTransferData(DataFlavor var1) throws UnsupportedFlavorException, IOException {
         if (var1.equals(FLAVOR)) {
            return fromXml(this.xml);
         } else if (var1.equals(DataFlavor.stringFlavor)) {
            return this.xml;
         } else {
            throw new UnsupportedFlavorException(var1);
         }
      }

      private static Optional<FieldValidation> fromXml(String var0) {
         try {
            XmlPersistor var1 = XmlPersistor.load(var0);
            FieldValidation var2 = FieldValidation.readFrom(var1);
            return Optional.of(var2);
         } catch (Exception var3) {
            return Optional.absent();
         }
      }
   }
}
