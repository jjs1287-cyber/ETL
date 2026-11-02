package org.etlforge.integration.client.ui.script.builder;

public final class FunctionSelectorFactory implements ScriptBuilderSectionFactory {
   @Override
   public ScriptBuilderSection newSection(ScriptBuilderModel var1) {
      FunctionTree var2 = new FunctionTree();
      return new FunctionTreePanel(var2);
   }
}
