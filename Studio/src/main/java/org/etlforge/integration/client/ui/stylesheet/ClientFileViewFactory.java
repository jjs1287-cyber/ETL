package org.etlforge.integration.client.ui.stylesheet;

import java.io.File;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;
import org.etlforge.integration.client.ui.icons.ClientIcons;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.uidelegate.entity.EntityIcons;
import org.etlforge.ui.widget.FileViewFactory;

public final class ClientFileViewFactory implements FileViewFactory {
   @Override
   public FileView createView() {
      return new ClientFileViewFactory.ClientFileView();
   }

   private static class ClientFileView extends FileView {
      private static final Icon JITTERPACK = ClientIcons.APPLICATION_16;
      private static final Icon PROJECT = EntityIcons.forType(EntityType.Project).getDefaultIcon(EntityIcons.Size.SMALL);

      private ClientFileView() {
      }

      @Override
      public Icon getIcon(File var1) {
         String var2 = var1.getName().toLowerCase();
         if (var2.endsWith(".jpk")) {
            return JITTERPACK;
         } else {
            return var2.endsWith(".jip") ? PROJECT : super.getIcon(var1);
         }
      }
   }
}
