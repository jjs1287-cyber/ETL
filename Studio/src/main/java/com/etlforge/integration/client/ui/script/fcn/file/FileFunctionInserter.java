package com.etlforge.integration.client.ui.script.fcn.file;

import com.google.common.base.Supplier;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.etlforge.integration.client.ui.script.fcn.CompletionTokenFactory;
import com.etlforge.integration.client.ui.script.fcn.Expression;
import com.etlforge.integration.client.ui.script.fcn.SimpleFunctionWizard;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.etlforge.application.ui.Alert;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.FormulaBuilderSourceTree;
import org.etlforge.integration.client.ui.interchange.entity.transformation.tree.formula.SourceTreeAwareFunctionInserter;
import org.etlforge.integration.client.ui.script.builder.FunctionTester;
import org.etlforge.integration.client.ui.script.builder.TestableFunctionInserter;
import org.etlforge.integration.client.ui.script.text.FunctionCompletionToken;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.script.DataElementAwareFunctionInserter;
import org.etlforge.integration.data.script.ExpressionParser;
import org.etlforge.integration.data.script.Function;
import org.etlforge.integration.data.script.FunctionInserter;
import org.etlforge.integration.data.script.ProjectItemFunction;
import org.etlforge.integration.data.script.ProjectItemFunctionInserter;
import org.etlforge.integration.data.script.de.GlobalDataElements;
import org.etlforge.ui.text.contentassist.CompletionToken;

public class FileFunctionInserter<T extends IntegrationEntity>
   implements FunctionInserter,
   DataElementAwareFunctionInserter,
   SourceTreeAwareFunctionInserter,
   TestableFunctionInserter,
   ProjectItemFunctionInserter {
   private final Function function;
   private final Class<T> itemType;
   private IntegrationProject project;
   private Supplier<FormulaBuilderSourceTree> sourceTreeFactory;
   private GlobalDataElements globalVariables;
   private FunctionTester tester;
   private ExpressionParser parser;

   public FileFunctionInserter(Function var1, Class<T> var2) {
      this.function = var1;
      this.itemType = var2;
   }

   @Override
   public final void setProject(IntegrationProject var1) {
      this.project = var1;
   }

   @Override
   public final void setFunctionTester(FunctionTester var1) {
      this.tester = var1;
   }

   @Override
   public final void setParser(ExpressionParser var1) {
      this.parser = var1;
   }

   @Override
   public final void setSourceTree(Supplier<FormulaBuilderSourceTree> var1) {
      this.sourceTreeFactory = var1;
   }

   @Override
   public final void setDataElements(GlobalDataElements var1) {
      this.globalVariables = var1;
   }

   @Override
   public final void insertFunction(JTextComponent var1) {
      FileFunctionWizard var2 = this.createWizard();
      if (var2 == null) {
         this.doDefaultInsertion(var1);
      } else {
         this.startWizard(var1, var2);
      }
   }

   private FileFunctionWizard createWizard() {
      List var1 = this.getAvailableLocations();
      if (var1.isEmpty()) {
         this.showNoLocationsMessage();
         return null;
      }

      FileFunctionWizard var2 = new FileFunctionWizard<>(this.function, this.itemType, this.project);
      if (this.globalVariables != null) {
         var2.setGlobalVariables(this.globalVariables);
      }

      if (this.sourceTreeFactory != null) {
         var2.setSourceTree(this.sourceTreeFactory.get());
      }

      if (this.tester != null) {
         var2.setExpressionParser(this.parser);
         var2.setFunctionTester(this.tester);
      }

      return var2;
   }

   private List<Locatable> getAvailableLocations() {
      if (this.project != null) {
         List var1 = ((ProjectItemFunction)this.function).getApplicableProjectItems(this.project);
         return translate(var1);
      } else {
         return Lists.newArrayList();
      }
   }

   private static List<Locatable> translate(List<IntegrationEntity> var0) {
      return Lists.newArrayList(Iterables.filter(var0, Locatable.class));
   }

   private void showNoLocationsMessage() {
      String var1 = this.itemType.equals(Source.class) ? "Sources" : "Targets";
      Alert.info("The project has no " + var1 + " that can be used by this function.", "No " + var1 + " Available");
   }

   private void doDefaultInsertion(JTextComponent var1) {
      int var2 = var1.getCaretPosition();
      FunctionCompletionToken var3 = new FunctionCompletionToken(this.function, var2, var2, var1);
      var3.complete(var1.getDocument());
   }

   private void startWizard(JTextComponent var1, FileFunctionWizard var2) {
      Expression var3 = var2.run();
      if (var3 != null) {
         var3.insertInto(var1);
      }
   }

   @Override
   public final CompletionToken getCompletionToken(int var1, int var2, JTextComponent var3) {
      FileFunctionInserter.TokenFactoryImpl var4 = new FileFunctionInserter.TokenFactoryImpl();
      return var4.getCompletionToken(var1, var2, var3);
   }

   private class TokenFactoryImpl extends CompletionTokenFactory {
      public TokenFactoryImpl() {
         super(FileFunctionInserter.this.function);
      }

      @Override
      protected SimpleFunctionWizard createWizard() {
         return FileFunctionInserter.this.createWizard();
      }
   }
}
