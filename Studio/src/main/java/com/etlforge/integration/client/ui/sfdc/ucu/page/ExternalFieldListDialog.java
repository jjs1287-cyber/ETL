package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.etlforge.integration.client.ui.sfdc.AttentionErrorHandler;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import javax.swing.DefaultListModel;
import org.etlforge.application.ui.ApplicationUi;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.application.ui.widget.ComponentFactories;
import org.etlforge.integration.salesforce.wsdl.SObjectSchema;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.KongaDialog;
import org.etlforge.ui.layout.CaptionLayout;
import org.etlforge.ui.selection.DialogSelectionSupport;
import org.etlforge.ui.selection.IndexedSelection;
import org.etlforge.ui.widget.list.KongaList;
import org.etlforge.ui.widget.list.KongaListFactory;
import org.etlforge.ui.widget.list.ListSelectionMode;
import org.etlforge.ui.widget.list.ListUtils;

final class ExternalFieldListDialog {
   private final Callable<List<SObjectSchema.Field>> fieldProducer;
   private final KongaList list;
   private final KongaDialog dialog;

   public ExternalFieldListDialog(Callable<List<SObjectSchema.Field>> var1) {
      this.fieldProducer = var1;
      this.list = this.createList();
      this.dialog = this.createDialog();
   }

   private KongaList createList() {
      KongaListFactory var1 = ComponentFactories.newKongaListFactory();
      var1.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      var1.setVisibleRowCount(6);
      return var1.newList();
   }

   private KongaDialog createDialog() {
      CaptionLayout var1 = CaptionLayout.of("Select the external ID field:", this.list);
      KongaDialog var2 = new KongaDialog(ApplicationUi.getAppWinFrame(), "Select External ID Field", true);
      var2.standardLayout(var1, KongaDialog.OK_CANCEL);
      var2.setLocationRelativeTo(null);
      var2.setOkButtonAsDefault();
      new DialogSelectionSupport(this.list, var2);
      return var2;
   }

   public SObjectSchema.Field open() {
      Future var1 = this.startListPopulator();
      this.dialog.setVisible(true);
      var1.cancel(true);
      if (this.dialog.wasOkPressed()) {
         IndexedSelection var2 = this.list.getSelection();
         if (var2.head() instanceof SObjectSchema.Field) {
            return (SObjectSchema.Field)var2.head();
         }
      }

      return null;
   }

   private Future<?> startListPopulator() {
      ExternalFieldListDialog.ListModelConsumer var1 = new ExternalFieldListDialog.ListModelConsumer(this.list);
      Computation var2 = Computation.create(this.fieldProducer, var1);
      return var2.swingWorkerBuilder().withErrorHandler(new AttentionErrorHandler(this.getClass())).withWaitService(this.dialog).buildAndExecute();
   }

   private static class ListModelConsumer implements Receiver<List<SObjectSchema.Field>> {
      private final KongaList list;

      public ListModelConsumer(KongaList var1) {
         this.list = var1;
      }

      public void handle(List<SObjectSchema.Field> var1) {
         DefaultListModel var2 = ListUtils.createModel(var1);
         this.list.setModel(var2);
      }
   }
}
