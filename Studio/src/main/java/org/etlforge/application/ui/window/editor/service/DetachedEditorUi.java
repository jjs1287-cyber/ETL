package org.etlforge.application.ui.window.editor.service;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Dialog.ModalityType;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.lang.reflect.InvocationTargetException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.WindowElementState;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.ui.DimensionUtils;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitLock;

final class DetachedEditorUi {
   private final ApplicationWindow appWin;
   private final Editor editor;
   private final DetachedEditorUi.Attacher attacher;
   private final FocusListener focusListener;
   private KongaDialog editorWindow;

   static DetachedEditorUi createAndShow(Editor var0, ApplicationWindow var1, DetachedEditorUi.Attacher var2, FocusListener var3) {
      WaitLock var4 = var1.startWait();
      DetachedEditorUi var5 = new DetachedEditorUi(var1, var0, var2, var3);
      if (EventQueue.isDispatchThread()) {
         var5.createAndShowDialog(var4);
      } else {
         createDialogAndWait(var4, var5);
      }

      return var5;
   }

   private static void createDialogAndWait(final WaitLock var0, final DetachedEditorUi var1) {
      try {
         EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
               var1.createAndShowDialog(var0);
            }
         });
      } catch (InterruptedException var5) {
         Thread.currentThread().interrupt();
         var5.printStackTrace();
      } catch (InvocationTargetException var6) {
         Throwable var3 = var6.getCause();
         String var4 = "An unexpected error occurred: " + (var3 != null ? var3.getMessage() : var6.getMessage());
         ErrorLog.attention(DetachedEditorUi.class, var4, var6);
      }
   }

   public DetachedEditorUi(ApplicationWindow var1, Editor var2, DetachedEditorUi.Attacher var3, FocusListener var4) {
      this.appWin = var1;
      this.editor = var2;
      this.attacher = var3;
      this.focusListener = var4;
      this.createUi();
   }

   private void createAndShowDialog(WaitLock var1) {
      try {
         this.show();
      } finally {
         var1.release();
      }
   }

   void activate() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetachedEditorUi.this.editorWindow.toFront();
            DetachedEditorUi.this.editorWindow.getRootPane().requestFocus();
         }
      });
   }

   void updateTitle() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetachedEditorUi.this.editorWindow.setTitle(DetachedEditorUi.this.editor.getTitle());
         }
      });
   }

   void close() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetachedEditorUi.this.editorWindow.dispose();
         }
      });
   }

   void attach() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetachedEditorUi.this.editor.appendState(WindowElementState.ATTACHED);
            DetachedEditorUi.this.editorWindow.dispose();
         }
      });
   }

   public void show() {
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            DetachedEditorUi.this.editorWindow.setVisible(true);
         }
      });
   }

   private void createUi() {
      this.editorWindow = new KongaDialog(UiUtils.getActiveWindow(), this.editor.getTitle(), ModalityType.APPLICATION_MODAL) {
         @Override
         protected void onDialogClose() {
            if (DetachedEditorUi.this.attachEditor()) {
               super.onDialogClose();
            }
         }

         @Override
         protected void onDialogOpened() {
            EventQueue.invokeLater(new Runnable() {
               @Override
               public void run() {
                  DetachedEditorUi.this.editor.requestFocus();
               }
            });
         }
      };
      this.editorWindow.setCancelOnEscape(false);
      this.editorWindow.setDefaultCloseOperation(0);
      this.layoutDialog();
      this.editorWindow.addWindowFocusListener(new DetachedEditorUi.FocusOwnerListener());
      this.setInitialSize();
      this.editorWindow.manageLocation(this.editor.getClass());
      this.ensureEditorNotLargerThanMainWindow();
   }

   private void layoutDialog() {
      new BorderLayoutBuilder(this.editor).asContentPaneFor(this.editorWindow);
   }

   private void setInitialSize() {
      this.editorWindow.pack();
      Dimension var1 = this.editorWindow.getSize();
      var1.width += 50;
      var1.height += 50;
      this.enforceMinimumSize(var1);
      this.editorWindow.setSize(var1);
   }

   private void enforceMinimumSize(Dimension var1) {
      DimensionUtils.ensureMinimumSize(var1, 600, 400);
   }

   private void ensureEditorNotLargerThanMainWindow() {
      Dimension var1 = this.editorWindow.getSize();
      Dimension var2 = this.appWin.getFrame().getSize();
      byte var3 = 10;
      boolean var4 = false;
      if (var1.width > var2.width - var3) {
         var1.width = var2.width - var3;
         var4 = true;
      }

      if (var1.height > var2.height - var3) {
         var1.height = var2.height - var3;
         var4 = true;
      }

      if (var4) {
         this.editorWindow.setSize(var1);
      }
   }

   private boolean attachEditor() {
      return this.attacher.attach(this.editor);
   }

   interface Attacher {
      boolean attach(Editor var1);
   }

   private class FocusOwnerListener implements WindowFocusListener {
      private FocusOwnerListener() {
      }

      @Override
      public void windowGainedFocus(WindowEvent var1) {
         DetachedEditorUi.this.editor.appendState(WindowElementState.ACTIVE);
         DetachedEditorUi.this.focusListener.focusGained(new FocusEvent(DetachedEditorUi.this.editorWindow, 1004));
      }

      @Override
      public void windowLostFocus(WindowEvent var1) {
         DetachedEditorUi.this.editor.revokeState(WindowElementState.ACTIVE);
         DetachedEditorUi.this.focusListener.focusLost(new FocusEvent(DetachedEditorUi.this.editorWindow, 1005));
      }
   }
}
