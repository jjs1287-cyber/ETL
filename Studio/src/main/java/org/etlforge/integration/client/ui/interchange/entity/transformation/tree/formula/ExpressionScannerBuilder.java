package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula;

import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.TreeMapperSourceTree;
import org.etlforge.integration.client.ui.script.builder.ExpressionScanner;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.de.GlobalDataElements;

final class ExpressionScannerBuilder {
   private final TransformationExpressionSource source;
   private final TreeMapperSourceTree sourceTree;
   private final GlobalDataElements globalDataElements;
   private final ExpressionParser parser;

   public ExpressionScannerBuilder(TreeMapperSourceTree var1, TransformationExpressionSource var2, GlobalDataElements var3, ExpressionParser var4) {
      this.sourceTree = var1;
      this.globalDataElements = var3;
      this.source = var2;
      this.parser = var4;
   }

   public ExpressionScanner buildScanner() {
      ExpressionScanner var1 = new ExpressionScanner(this.source, this.globalDataElements, this.parser);
      var1.setDataElementFactory(new DataElementFactoryImpl(this.sourceTree, this.source));
      return var1;
   }
}
