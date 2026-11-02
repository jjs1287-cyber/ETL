package com.etlforge.integration.client.ui.sfdc.page;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public final class ChunkingOptionsAction extends AbstractAction {
   private ChunkingSettingsEditor editor;

   public ChunkingOptionsAction() {
      super("Chunking Settings...");
      this.setEnabled(false);
   }

   public void setEditor(ChunkingSettingsEditor var1) {
      this.editor = var1;
      this.setEnabled(var1 != null);
   }

   @Override
   public void actionPerformed(ActionEvent var1) {
      EventQueue.invokeLater(new Runnable() {
         @Override
         public void run() {
            ChunkingOptionsAction.this.editor.edit();
         }
      });
   }
}
