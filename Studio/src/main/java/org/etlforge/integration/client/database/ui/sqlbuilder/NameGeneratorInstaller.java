package org.etlforge.integration.client.database.ui.sqlbuilder;

import java.awt.EventQueue;
import org.etlforge.integration.database.BeginEndQuote;

public final class NameGeneratorInstaller extends DbObjectsModelListener.Adapter {
   private final SqlInputArea inputArea;

   public NameGeneratorInstaller(SqlInputArea var1) {
      this.inputArea = var1;
   }

   @Override
   public void beginEndQuotesChanged(DbObjectsModel var1, BeginEndQuote var2) {
      final NameGenerator var3 = NameGenerators.forDatabase(var1.getDriver(), var2);
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            NameGeneratorInstaller.this.inputArea.setNameGenerator(var3);
         }
      });
   }
}
