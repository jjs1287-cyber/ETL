package org.etlforge.integration.client.ui.interchange.entity.script;

import org.etlforge.application.ui.window.ApplicationWindow;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.ui.interchange.component.page.entity.IntegrationEntityPage;
import org.etlforge.integration.client.ui.interchange.component.page.entity.PageConfig;
import org.etlforge.integration.client.ui.interchange.debug.InterchangeDebugService;
import org.etlforge.integration.client.ui.interchange.entity.AbstractEntityConfig;
import org.etlforge.integration.client.ui.interchange.entity.script.page.ScriptPage;
import org.etlforge.integration.client.ui.interchange.entity.script.page.ScriptPageConfig;
import org.etlforge.integration.client.ui.interchange.entity.script.search.ScriptSearchAndReplaceProperties;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUi;
import org.etlforge.integration.client.ui.script.builder.ScriptBuilderUiFactory;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.Script;
import org.etlforge.integration.data.persist.ProjectPersistor;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.uidelegate.entity.tooltip.IntegrationEntityToolTip;

public final class ScriptConfig extends AbstractEntityConfig {
   private final InterchangeDebugService debugService;
   private final ScriptBuilderUiFactory scriptBuilderUiFactory;

   public ScriptConfig(InterchangeDebugService var1, ScriptBuilderUiFactory var2) {
      super(EntityType.Script);
      this.debugService = var1;
      this.scriptBuilderUiFactory = var2;
      this.setSearchAndReplaceMetaData(new ScriptSearchAndReplaceProperties());
   }

   @Override
   protected IntegrationEntityPage createNewPage(IntegrationEntity var1) {
      ScriptBuilderUi var2 = this.createScriptBuilder((Script)var1);
      return new ScriptPage((Script)var1, var2);
   }

   private ScriptBuilderUi createScriptBuilder(Script var1) {
      IntegrationProject var2 = ProjectUtils.getProject(var1);
      return this.scriptBuilderUiFactory.newScriptBuilder(var1, var2);
   }

   @Override
   protected PageConfig createPageConfig(ApplicationWindow var1, IntegrationEntity var2, ProjectPersistor var3) {
      return new ScriptPageConfig(var1, var3, this.debugService, this.scriptBuilderUiFactory.getDebugActions());
   }

   @Override
   public IntegrationEntityToolTip getToolTip(IntegrationEntity var1) {
      return new ScriptToolTip((Script)var1);
   }
}
