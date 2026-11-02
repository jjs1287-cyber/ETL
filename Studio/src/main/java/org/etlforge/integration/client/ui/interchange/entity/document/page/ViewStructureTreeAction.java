package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.job.Computation;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

final class ViewStructureTreeAction extends AbstractAction {
   private final TextDocumentPage page;

   public ViewStructureTreeAction(TextDocumentPage var1) {
      super("Review structure...");
      this.page = var1;
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ViewStructureTreeAction.this.start();
         }
      });
   }

   private void start() {
      try {
         Computation var1 = this.createJob();
         this.runJobInBackground(var1);
      } catch (TextDocumentDefinitionException var2) {
         ErrorLog.attention(this.getClass(), null, var2);
      }
   }

   private Computation<MappingTreeStructure> createJob() throws TextDocumentDefinitionException {
      TextStructure var1 = this.page.getStructureRepresentation();
      MappingTreeStructureBuilder var2 = new MappingTreeStructureBuilder(var1);
      MappingTreeStructureViewer var3 = new MappingTreeStructureViewer(this.page.getObject().getName());
      return Computation.create(var2, var3);
   }

   private void runJobInBackground(Computation<MappingTreeStructure> var1) {
      var1.swingWorkerBuilder().withWaitService(this.page.getWaitService()).buildAndExecute();
   }
}
