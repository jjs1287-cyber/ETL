package org.etlforge.integration.client.ui.interchange.entity.document.page;

import java.awt.Dimension;
import org.etlforge.integration.client.ui.MappingStructureTreeWidget;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;
import org.etlforge.system.callback.Receiver;
import org.etlforge.ui.dialog.UiProviderDialogDisplayer;
import org.etlforge.ui.layout.BorderLayoutBuilder;
import org.etlforge.ui.util.UiUtils;

final class MappingTreeStructureViewer implements Receiver<MappingTreeStructure> {
   private final String title;

   public MappingTreeStructureViewer(String var1) {
      this.title = var1;
   }

   public void handle(MappingTreeStructure var1) {
      MappingStructureTreeWidget var2 = new MappingStructureTreeWidget(this.title, var1);
      var2.setRootVisible(false);
      var2.expandAll();
      BorderLayoutBuilder var3 = this.ensureMinimumWidth(var2);
      UiProviderDialogDisplayer.display(UiUtils.getActiveWindow(), var3, this.title);
   }

   private BorderLayoutBuilder ensureMinimumWidth(MappingStructureTreeWidget var1) {
      BorderLayoutBuilder var2 = new BorderLayoutBuilder(var1);
      Dimension var3 = var2.container().getPreferredSize();
      var3.width = Math.max(300, var3.width);
      var2.container().setPreferredSize(var3);
      return var2;
   }
}
