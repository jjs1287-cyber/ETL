package org.etlforge.integration.client.ui.structure.editor.json;

import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.StructureEditor;
import org.etlforge.integration.client.ui.structure.editor.StructureEditorFactory;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public final class JsonStructureEditorFactory implements StructureEditorFactory {
   @Override
   public StructureEditor getEditor(Transformation var1, DataStructure var2, SourceTarget var3, EntityExplorerSupport var4) {
      ManagedProject var5 = ProjectUtils.getManagedProject(var1);
      XsdFileStore var6 = new XsdFileStore(var5.getFileStore());
      return new JsonStructureEditor((JsonStructure)var2, var6, var3);
   }
}
