package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.FillDataElementKit;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementFactory;
import org.etlforge.integration.debug.client.de.DataElementScope;

final class DataElementFactoryImpl implements DataElementFactory {
   private final TreeMapperSourceTree sourceTree;
   private final FillDataElementKit fillDataElementKit;

   public DataElementFactoryImpl(TreeMapperSourceTree var1, TransformationExpressionSource var2) {
      this.sourceTree = var1;
      Node var3 = var1.getGenerator(var2.getTargetNode());
      this.fillDataElementKit = new FillDataElementKit(var1.m_DocReader, var3);
   }

   @Override
   public DataElement create(String var1, DataElementScope var2) {
      switch (var2) {
         case GLOBAL:
            return DataElementFactory.TRIVIAL.create(var1, var2);
         case SOURCE:
            SourceDataElementCreator var3 = new SourceDataElementCreator(var1, this.sourceTree, this.fillDataElementKit);
            return var3.build();
         default:
            throw new RuntimeException("Unexpected scope: " + var2);
      }
   }
}
