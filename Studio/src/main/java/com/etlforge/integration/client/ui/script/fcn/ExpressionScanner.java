package com.etlforge.integration.client.ui.script.fcn;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceDataElementCreator;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.grammar.FillDataElementKit;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils.Node;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.dataelement.ScriptDataElementsModel;
import org.etlforge.integration.data.entity.transformation.TransformationExpressionSource;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.data.script.de.DataElementReference;
import org.etlforge.integration.debug.client.de.DataElement;
import org.etlforge.integration.debug.client.de.DataElementScope;
import org.etlforge.ui.DelayedJob;
import org.etlforge.ui.input.InputChangeEvent;
import org.etlforge.ui.input.InputChangeListener;

public final class ExpressionScanner implements InputChangeListener, DocumentListener, Runnable {
   private final FunctionWizard wizard;
   private final ScriptDataElementsModel deModel;
   private FunctionTester tester;
   private ExpressionParser parser;
   private FormulaBuilderSourceTree sourceTree;
   private DelayedJob timer;
   private Set<DataElement> usedDataElements;

   public ExpressionScanner(FunctionWizard var1, ScriptDataElementsModel var2) {
      this.wizard = var1;
      this.deModel = var2;
   }

   public void setTester(FunctionTester var1) {
      this.tester = var1;
   }

   public void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   public void setSourceTree(FormulaBuilderSourceTree var1) {
      this.sourceTree = var1;
   }

   @Override
   public void inputChanged(InputChangeEvent var1) {
      this.handleChange();
   }

   protected void handleChange() {
      if (this.parser != null) {
         if (this.timer != null) {
            this.timer.cancel();
         }

         this.timer = DelayedJob.createAndSchedule(200, this);
      }
   }

   @Override
   public void insertUpdate(DocumentEvent var1) {
      this.handleChange();
   }

   @Override
   public void removeUpdate(DocumentEvent var1) {
      this.handleChange();
   }

   @Override
   public void changedUpdate(DocumentEvent var1) {
   }

   @Override
   public void run() {
      if (this.parser != null && this.tester != null) {
         Expression var1 = this.wizard.buildExpression();
         ExpressionParser.Result var2 = var1.parse(this.parser);
         this.usedDataElements = this.getGlobalDataElements(var2);
         if (this.sourceTree != null) {
            ExpressionSource var3 = this.parser.getSource();
            if (var3 instanceof TransformationExpressionSource) {
               Set var4 = this.getSourceDataElements(var2, (TransformationExpressionSource)var3);
               this.usedDataElements.addAll(var4);
            }
         }

         this.deModel.setDataElementsScannedFromScript(this.usedDataElements);
      }
   }

   private Set<DataElement> getGlobalDataElements(ExpressionParser.Result var1) {
      HashSet var2 = Sets.newHashSet();
      ImmutableSet var3 = var1.getGlobalDataElementReferences();
      if (var3 != null) {
         ScriptDataElementsModel var4 = this.tester.getDataElements();

         for (Object __etlItem105 : var3) {
            DataElementReference var6 = (DataElementReference) __etlItem105;
            DataElement var7 = new DataElement(var6.getDataElement(), DataElementScope.GLOBAL, null);
            DataElement var8 = var4.lookup(var7.getName(), DataElementScope.GLOBAL);
            if (var8 != null) {
               var7.setValue(var8.getValue());
            }

            var2.add(var7);
         }
      }

      return var2;
   }

   private Set<DataElement> getSourceDataElements(ExpressionParser.Result var1, TransformationExpressionSource var2) {
      HashSet var3 = Sets.newHashSet();
      ImmutableSet var4 = var1.getSourceDataElements();
      if (var4 != null) {
         Node var5 = this.sourceTree.getGenerator(var2.getTargetNode());
         FillDataElementKit var6 = new FillDataElementKit(this.sourceTree.m_DocReader, var5);

         for (Object __etlItem126 : var4) {
            String var8 = (String) __etlItem126;
            SourceDataElementCreator var9 = new SourceDataElementCreator(var8, this.sourceTree, var6);
            DataElement var10 = var9.build();
            if (var10 != null) {
               var3.add(var10);
            }
         }
      }

      return var3;
   }

   public boolean hasDataElementsWithoutValue() {
      if (this.usedDataElements != null) {
         for (DataElement var2 : this.usedDataElements) {
            if (StringUtils.isEmpty(var2.getValue())) {
               return true;
            }
         }
      }

      return false;
   }
}
