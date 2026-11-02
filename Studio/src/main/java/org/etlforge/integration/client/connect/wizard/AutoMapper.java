package org.etlforge.integration.client.connect.wizard;

import org.etlforge.integration.client.connect.mapper.AbstractAutoMappingJob;
import org.etlforge.integration.client.ui.interchange.entity.transformation.page.TransformationPage;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapper;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.ComplexTextStructure;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.XmlStructure;

final class AutoMapper extends AbstractAutoMappingJob {
   public AutoMapper(TransformationPage var1) {
      super(var1);
   }

   @Override
   protected Node getSourceNode(TreeMapper var1) {
      return this.getStructureRootNode(var1.m_sourceTreeComponent);
   }

   @Override
   protected Node getTargetNode(TreeMapper var1) {
      Node var2 = this.getStructureRootNode(var1.m_targetTreeComponent);
      if (this.isTargetComplexText(var1)) {
         var2 = this.getComplexTextRoot(var2);
      } else if (this.isDbToXml(var1)) {
         var2 = this.getXmlNodeToAutoMapFromDb(var2);
      }

      return var2;
   }

   private boolean isTargetComplexText(TreeMapper var1) {
      Transformation var2 = var1.getTransformation();
      return var2.getOutputStructure() instanceof ComplexTextStructure;
   }

   private Node getComplexTextRoot(Node var1) {
      return var1.getChildCount() == 1 ? var1.getChildAt(0) : var1;
   }

   private boolean isDbToXml(TreeMapper var1) {
      Transformation var2 = var1.getTransformation();
      return var2.getInputStructure() instanceof DatabaseStructure && var2.getOutputStructure() instanceof XmlStructure;
   }

   private Node getXmlNodeToAutoMapFromDb(Node var1) {
      if (var1.getChildCount() == 1) {
         Node var2 = var1.getFirstChild();
         if (var2.getCROM().isElementNode()) {
            return var2;
         }
      }

      return var1;
   }
}
