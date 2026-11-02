package org.etlforge.integration.client.ui.interchange.entity.operation.actions;

import com.google.common.collect.Lists;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.KongaSwingWorker;
import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.application.ui.window.action.AbstractSingleActiveObjectAction;
import org.etlforge.application.ui.window.editor.Editor;
import org.etlforge.application.ui.window.editor.EditorPredicates;
import org.etlforge.application.ui.window.editor.EditorService;
import org.etlforge.collections.KongaListUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.controller.OperationGraphController;
import org.etlforge.integration.client.ui.interchange.entity.operation.graph2d.node.OperationWrapperNode;
import org.etlforge.integration.client.ui.interchange.entity.operation.page.OperationPage;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsDialog;
import org.etlforge.integration.client.ui.operation.options.OperationOptionsPanel;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.operation.options.OperationOptions;
import org.etlforge.integration.data.persist.InterchangeSavingException;
import org.etlforge.integration.data.persist.ProjectPersistor;

public final class SetOperationOptionsAction extends AbstractSingleActiveObjectAction<Operation> {
   private final ApplicationWindow appWin;
   private final EntityExplorerSupport projectBrowser;
   private final ProjectPersistor persistor;

   public SetOperationOptionsAction(ApplicationWindow var1, EntityExplorerSupport var2, ProjectPersistor var3) {
      super(SetOperationOptionsAction.class.getName(), var1.getActiveObjectService(), "Options...", ClientIcons.getIcon("Options.16"), Operation.class);
      this.appWin = var1;
      this.projectBrowser = var2;
      this.persistor = var3;
      this.putValue("KongaMediumIconKey", ClientIcons.getIcon("Options.24"));
      this.putValue("SwingLargeIconKey", ClientIcons.getIcon("Options.32"));
   }

   protected void runOnObject(final Operation var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            SetOperationOptionsAction.this.openOptionsUi(var1);
         }
      });
   }

   private void openOptionsUi(Operation var1) {
      SetOperationOptionsAction.OperationPageFinder var2 = new SetOperationOptionsAction.OperationPageFinder(this.appWin.getEditorService());
      OperationPage var3 = var2.findPage(var1);
      if (var3 == null) {
         this.showOptionsDialog(var1);
      } else {
         this.openOptionsFromPage(var3, var1);
      }
   }

   private void showOptionsDialog(Operation var1) {
      OperationOptionsPanel var2 = new OperationOptionsPanel(var1, this.projectBrowser);
      OperationOptionsDialog var3 = new OperationOptionsDialog(this.appWin.getFrame(), var2);
      OperationOptions var4 = var3.show();
      if (var4 != null) {
         this.applyOptions(var1, var4);
      }
   }

   private void applyOptions(Operation var1, OperationOptions var2) {
      SetOperationOptionsAction.OptionsApplier var3 = new SetOperationOptionsAction.OptionsApplier(var1, var2);
      KongaSwingWorker.runWhileWaiting(var3, this.appWin);
   }

   private void openOptionsFromPage(OperationPage var1, Operation var2) {
      this.appWin.getEditorService().openEditor(var1);
      OperationGraphController var3 = var1.getGraphController();
      OperationWrapperNode var4 = var1.getGraph().getModel().getOperationNode(var2);
      var3.openOptionsUi(var4);
   }

   private static final class OperationPageFinder {
      private final EditorService editors;

      public OperationPageFinder(EditorService var1) {
         this.editors = var1;
      }

      public OperationPage findPage(Operation var1) {
         List var2 = this.findAllMatchingPages(var1);
         if (var2.isEmpty()) {
            return null;
         }

         OperationPage var3 = this.getOnlyMatch(var2);
         if (var3 != null) {
            return var3;
         }

         List var4 = KongaListUtils.filter(var2, EditorPredicates.DIRTY);
         var3 = this.getOnlyMatch(var4);
         if (var3 != null) {
            return var3;
         }

         List var5 = var4.size() > 1 ? var4 : var2;
         var3 = this.exactPage(var1, var5);
         if (var3 != null) {
            return var3;
         }

         var3 = this.lastVisited(var5);
         return var3 != null ? var3 : (OperationPage)var5.get(0);
      }

      private List<OperationPage> findAllMatchingPages(Operation var1) {
         ArrayList var2 = Lists.newArrayList();

         for (Editor var6 : this.editors.getOpenEditors()) {
            if (var6 instanceof OperationPage) {
               OperationPage var7 = (OperationPage)var6;
               if (var7.getGraph().getModel().getOperationNode(var1) != null) {
                  var2.add(var7);
               }
            }
         }

         return var2;
      }

      private OperationPage getOnlyMatch(List<OperationPage> var1) {
         return var1.size() == 1 ? (OperationPage)var1.get(0) : null;
      }

      private OperationPage exactPage(Operation var1, List<OperationPage> var2) {
         for (OperationPage var4 : var2) {
            if (var4.getObject() == var1) {
               return var4;
            }
         }

         return null;
      }

      private OperationPage lastVisited(List<OperationPage> var1) {
         for (Editor var5 : this.editors.getNavigationOrder()) {
            if (var1.contains(var5)) {
               return (OperationPage)var5;
            }
         }

         return null;
      }
   }

   private final class OptionsApplier implements Runnable {
      private final Operation op;
      private final OperationOptions oldOptions;
      private final OperationOptions newOptions;

      public OptionsApplier(Operation var2, OperationOptions var3) {
         this.op = var2;
         this.oldOptions = var2.getOperationOptions();
         this.newOptions = var3;
      }

      @Override
      public void run() {
         try {
            this.op.setOperationOptions(this.newOptions);
            SetOperationOptionsAction.this.persistor.save(this.op);
         } catch (InterchangeSavingException var2) {
            this.op.setOperationOptions(this.oldOptions);
            ErrorLog.attention(this.getClass(), "The new options could not be saved.", var2);
         }
      }
   }
}
