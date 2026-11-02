package org.etlforge.integration.client.ui.interchange.entity.transformation;

import com.google.common.base.Supplier;
import org.etlforge.integration.client.ui.interchange.entity.transformation.database.TransformationDatabaseInfoFactory;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.structure.editor.db.StructureToEdit;
import org.etlforge.integration.client.ui.structure.editor.db.TransformationDatabaseInfo;
import org.etlforge.integration.data.SourceTarget;

final class TreeMapperToDatabaseStructure implements Supplier<StructureToEdit> {
   private final TreeMapper treeMapper;
   private final SourceTarget sourceTarget;

   public TreeMapperToDatabaseStructure(TreeMapper var1, SourceTarget var2) {
      this.treeMapper = var1;
      this.sourceTarget = var2;
   }

   public StructureToEdit get() {
      TransformationDatabaseInfo var1 = this.getDatabaseInfo();
      return new StructureToEdit(this.treeMapper.m_Transformation, var1.getStructure(), var1.getDatabaseObjects(), this.sourceTarget);
   }

   private TransformationDatabaseInfo getDatabaseInfo() {
      TransformationDatabaseInfoFactory var1 = this.sourceTarget == SourceTarget.Source
         ? TransformationDatabaseInfoFactory.fromSourceTree(this.treeMapper)
         : TransformationDatabaseInfoFactory.fromTargetTree(this.treeMapper);
      return var1.createInfo();
   }
}
