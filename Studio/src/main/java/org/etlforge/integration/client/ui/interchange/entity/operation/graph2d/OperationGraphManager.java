package org.etlforge.integration.client.ui.interchange.entity.operation.graph2d;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.LoggingExceptionHandler;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.application.ui.window.editor.SaveEditorException;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.flow.OperationFlow;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.layout.BoxBuilder;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.label.HtmlLabel;

public final class OperationGraphManager {
   private static final Map<ApplicationWindow, OperationGraphManager> managers = Maps.newIdentityHashMap();
   private final EditorService editorService;
   private final List<OperationPage> pages = Lists.newArrayList();
   private final Object lock = new Object();

   public static synchronized OperationGraphManager getManager(ApplicationWindow var0) {
      OperationGraphManager var1 = managers.get(var0);
      if (var1 == null) {
         var1 = new OperationGraphManager(var0.getEditorService());
         managers.put(var0, var1);
      }

      return var1;
   }

   private OperationGraphManager(EditorService var1) {
      this.editorService = var1;
   }

   public void registerGraph(OperationPage var1) {
      synchronized (this.lock) {
         this.pages.add(var1);
      }
   }

   public void removeGraph(OperationPage var1) {
      synchronized (this.lock) {
         this.pages.remove(var1);
      }
   }

   public boolean isChangeAllowed(Operation var1, OperationGraph2D var2) {
      synchronized (this.lock) {
         OperationPage var4 = this.findDirtyPageWithOperation(var1, var2);
         if (var4 != null) {
            OperationGraphManager.ConcurrentEditPrompt var5 = new OperationGraphManager.ConcurrentEditPrompt(var4);
            var5.open();
            return false;
         } else {
            return true;
         }
      }
   }

   private OperationPage findDirtyPageWithOperation(Operation var1, OperationGraph2D var2) {
      for (OperationPage var4 : this.pages) {
         if (var4.getGraph() != var2) {
            OperationFlow var5 = var4.getGraph().getModel().getModeledFlow();
            if (var5.isDirty() && var5.contains(var1)) {
               return var4;
            }
         }
      }

      return null;
   }

   private class ConcurrentEditPrompt {
      private final OperationPage otherPage;

      public ConcurrentEditPrompt(OperationPage var2) {
         this.otherPage = var2;
      }

      public void open() {
         Runnable var1 = new Runnable() {
            @Override
            public void run() {
               ConcurrentEditPrompt.this.openImpl();
            }
         };
         LoggingExceptionHandler var2 = new LoggingExceptionHandler(OperationGraphManager.class, "An unexpected error occurred");
         UiUtils.runOnEventThreadAndWait(var1, var2);
      }

      private void openImpl() {
         KongaDialog var1 = this.createDialog();
         var1.setVisible(true);
      }

      private KongaDialog createDialog() {
         KongaDialog var1 = new KongaDialog(UiUtils.getActiveWindow(), "Potential Conflict");
         BorderLayoutBuilder var2 = new BorderLayoutBuilder(0, 10).north(this.createCaption()).south(this.createButtons(var1));
         var2.withEmptyBorder(10).asContentPaneFor(var1);
         var1.pack();
         var1.setResizable(false);
         var1.manageLocation(OperationGraphManager.class);
         return var1;
      }

      private JComponent createButtons(KongaDialog var1) {
         return BoxBuilder.horizontal()
            .glue()
            .addAllWithSpace(
               10,
               new OperationGraphManager.SaveOtherPageAction(this.otherPage, var1),
               OperationGraphManager.this.new SwitchToOtherPageAction(this.otherPage, var1),
               var1.createCancelButton()
            )
            .container();
      }

      private JLabel createCaption() {
         String var1 = "This change may conflict with unsaved changes in the operation graph \""
            + this.otherPage.getObject().getName()
            + "\".\n\nWhat do you want to do?";
         return new HtmlLabel(var1);
      }
   }

   private static class SaveOtherPageAction extends AbstractAction {
      private final OperationPage otherPage;
      private final KongaDialog dialog;

      public SaveOtherPageAction(OperationPage var1, KongaDialog var2) {
         super("Save Other Graph Edits First");
         this.otherPage = var1;
         this.dialog = var2;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         try {
            this.dialog.dispose();
            this.otherPage.save();
         } catch (SaveEditorException var3) {
            ErrorLog.attention(OperationGraphManager.class, "Could not save the other graph.", var3);
         }
      }
   }

   private class SwitchToOtherPageAction extends AbstractAction {
      private final OperationPage otherPage;
      private final KongaDialog dialog;

      public SwitchToOtherPageAction(OperationPage var2, KongaDialog var3) {
         super("Switch To Other Graph");
         this.otherPage = var2;
         this.dialog = var3;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.dialog.dispose();
         OperationGraphManager.this.editorService.openEditor(this.otherPage);
      }
   }
}
