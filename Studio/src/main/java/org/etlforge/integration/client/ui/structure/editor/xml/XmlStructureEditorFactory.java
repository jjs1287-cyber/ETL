package org.etlforge.integration.client.ui.structure.editor.xml;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class XmlStructureEditorFactory implements StructureEditorFactory {
   @Override
   public StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      ManagedProject var5 = ProjectUtils.getManagedProject(var1);
      XsdFileStore var6 = new XsdFileStore(var5.getFileStore());
      XmlStructureEditor var7 = new XmlStructureEditor((XmlStructure)var2, var6, var3);
      this.configureEditor(var1, var7, var3);
      return var7;
   }

   private void configureEditor(Transformation var1, XmlStructureEditor var2, SourceTarget var3) {
      boolean var4 = var3 == SourceTarget.Source ? var1.isXmlSourceEdi() : var1.isXmlTargetEdi();
      if (var4) {
         var2.useForEdi();
      }
   }
}
