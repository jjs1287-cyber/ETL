package org.etlforge.ui.dialog;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.Dialog.ModalityType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.etlforge.ui.Empty;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.layout.BorderLayoutBuilder;

public final class UiProviderDialogDisplayer {
   public static void display(JFrame var0, UiProvider var1, String var2) {
      display(var0, var1, var2, KongaDialog.OK_ONLY);
   }

   public static void display(JFrame var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3) {
      display(var0, var1, var2, var3, null);
   }

   public static void display(JFrame var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      KongaDialog var5 = createDialog(var0, var1, var2, var3, var4);
      var5.setVisible(true);
   }

   public static void display(Window var0, UiProvider var1, String var2) {
      display(var0, var1, var2, KongaDialog.OK_ONLY, null);
   }

   public static void display(Window var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      KongaDialog var5 = createDialog(var0, var1, var2, ModalityType.APPLICATION_MODAL, var3, var4);
      var5.setVisible(true);
   }

   public static void display(DialogTrigger var0, UiProvider var1, String var2) {
      display(var0, var1, var2, KongaDialog.OK_ONLY);
   }

   public static void display(DialogTrigger var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3) {
      display(var0, var1, var2, var3, null);
   }

   public static void display(DialogTrigger var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      KongaDialog var5 = null;
      Window var6 = var0.getDialogOwner();
      if (var6 instanceof Dialog) {
         var5 = createDialog((Dialog)var6, var1, var2, var3, var4);
      } else if (var6 instanceof Frame) {
         var5 = createDialog((Frame)var6, var1, var2, var3, var4);
      } else {
         var5 = createDialog((Frame)null, var1, var2, var3, var4);
      }

      var5.setVisible(true);
   }

   public static KongaDialog createDialog(Dialog var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      KongaDialog var5 = new KongaDialog(var0, var2, null, true);
      completeDialog(var5, var0, var1, var3, var4);
      return var5;
   }

   public static KongaDialog createDialog(Frame var0, UiProvider var1, String var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      KongaDialog var5 = new KongaDialog(var0, var2, true);
      completeDialog(var5, var0, var1, var3, var4);
      return var5;
   }

   public static KongaDialog createDialog(Window var0, UiProvider var1, String var2, ModalityType var3, KongaDialog.ButtonMode var4, Class<?> var5) {
      KongaDialog var6 = new KongaDialog(var0, var2, var3);
      completeDialog(var6, var0, var1, var4, var5);
      return var6;
   }

   private static void completeDialog(KongaDialog var0, Component var1, UiProvider var2, KongaDialog.ButtonMode var3, Class<?> var4) {
      layoutDialog(var0, var2, var3);
      setDefaultButton(var0, var3);
      installFocusHandling(var0, var2);
      var0.pack();
      if (var4 != null) {
         var0.manageLocation(var4);
      } else {
         var0.setLocationRelativeTo(var1);
      }
   }

   private static void layoutDialog(KongaDialog var0, UiProvider var1, KongaDialog.ButtonMode var2) {
      BorderLayoutBuilder var3 = new BorderLayoutBuilder(var1).withEmptyBorder(10);
      var3.south(createButtonPanel(var0, var2));
      var3.asContentPaneFor(var0);
   }

   private static JComponent createButtonPanel(KongaDialog var0, KongaDialog.ButtonMode var1) {
      JPanel var2 = var0.getButtonPanel(var1, 10);
      var2.setBorder(Empty.border(10, 0, 10, 0));
      return var2;
   }

   private static void setDefaultButton(KongaDialog var0, KongaDialog.ButtonMode var1) {
      if (var1 == KongaDialog.OK_ONLY) {
         var0.setOkButtonAsDefault();
      } else if (var1 == KongaDialog.CLOSE_ONLY) {
         var0.setCloseButtonAsDefault();
      }
   }

   private static void installFocusHandling(KongaDialog var0, final UiProvider var1) {
      var0.addWindowListener(new WindowAdapter() {
         @Override
         public void windowOpened(WindowEvent var1x) {
            var1.requestFocus();
         }
      });
   }

   private UiProviderDialogDisplayer() {
   }
}
