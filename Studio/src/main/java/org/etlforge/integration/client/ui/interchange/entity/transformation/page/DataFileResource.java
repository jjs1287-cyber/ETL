package org.etlforge.integration.client.ui.interchange.entity.transformation.page;

import java.io.IOException;
import org.etlforge.application.ui.window.AbstractWindowElementResource;
import org.etlforge.application.ui.window.WindowElement;
import org.etlforge.application.ui.window.WindowElementStateSpan;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.TreeComponent;

public class DataFileResource extends AbstractWindowElementResource {
   private final TreeMapper treeMapper;

   public DataFileResource(TreeMapper var1) {
      super(WindowElementStateSpan.WHEN_ACTIVE);
      this.treeMapper = var1;
   }

   @Override
   public void dispose(WindowElement var1) {
      this.closeReader(this.treeMapper.m_sourceTreeComponent);
      this.closeReader(this.treeMapper.m_targetTreeComponent);
   }

   private void closeReader(TreeComponent var1) {
      try {
         if (var1 != null && var1.m_DocReader != null) {
            var1.m_DocReader.close();
         }
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }
}
