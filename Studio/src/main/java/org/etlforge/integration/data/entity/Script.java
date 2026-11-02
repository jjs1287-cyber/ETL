package org.etlforge.integration.data.entity;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.etlforge.integration.data.entity.id.ScriptId;
import org.etlforge.integration.data.entity.link.EntityLink;
import org.etlforge.integration.data.entity.operation.flow.OperationFlowActivity;
import org.etlforge.integration.data.entity.script.ScriptReferenceReplacer;
import org.etlforge.integration.data.entity.script.ScriptedLink;
import org.etlforge.integration.data.entity.script.ScriptedLinkSource;
import org.etlforge.integration.data.entity.script.ScriptedLinkSourceEventSupport;
import org.etlforge.integration.data.entity.script.ScriptedLinkSourceListener;
import org.etlforge.integration.data.functors.EntityFilter;
import org.etlforge.integration.data.project.IntegrationProject;
import org.etlforge.integration.data.project.ProjectUtils;
import org.etlforge.integration.data.script.ExpressionSource;
import org.etlforge.integration.data.script.GrammarUtils;
import org.etlforge.integration.data.script.ScriptStatus;
import org.etlforge.integration.data.script.ScriptValidator;
import org.etlforge.integration.data.script.Transform;
import org.etlforge.integration.data.validation.ValidationMessageCollector;
import org.etlforge.integration.data.validation.ValidationStatus;
import org.etlforge.util.persist.Persistor;

public final class Script extends IntegrationEntity implements ScriptedLinkSource, ExpressionSource, OperationFlowActivity {
   public static final String SCRIPT_ID = "script_id";
   public static final String SCRIPT_NAME = "script_name";
   public static final String SCRIPT_PROPERTY = "script";
   private static String[] PROPERTIES = new String[]{"script_id", "script_name", "script", "salesforce_wizard_object"};
   private List<ScriptedLink> scriptedLinks;
   private ScriptedLinkSourceEventSupport scripedLinksEventSupport = new ScriptedLinkSourceEventSupport(this);

   public Script(String var1) {
      this(null, var1);
   }

   public Script(ScriptId var1, String var2) {
      super(EntityType.Script, var1, var2);
      this.setProperty("script", "<trans>\n//App script goes between trans tags static txt is outside of any tag.\n\n</trans><javascript>\n//java script goes hear\n\n</javascript>");
   }

   public ScriptId getID() {
      return (ScriptId)super.getID();
   }

   @Override
   public String[] getPropertyKeys() {
      return PROPERTIES;
   }

   public String getScript() {
      return this.getProperty("script", "");
   }

   public void setScript(String var1) {
      String var2 = var1 != null ? var1 : "";
      synchronized (this.getDataLock()) {
         String var3 = this.getScript();
         if (var2.equals(var3)) {
            return;
         }

         IntegrationProject var5 = ProjectUtils.getProject(this);
         if (var5 != null) {
            this.updateScriptedLinks(var5.getItemLookup(), var2);
         }
      }

      this.setStringProperty("script", var2);
      this.scripedLinksEventSupport.notifyListeners();
   }

   private void updateScriptedLinks(IntegrationEntityLookup var1, String var2) {
      Set var3 = GrammarUtils.getAllIdsFromScript(new Transform(), var2, "The Script " + this.getName());
      List var4 = IdToObjectUtils.getEntitiesFromIds(var3, var1, IntegrationEntity.class);
      this.scriptedLinks = Lists.newArrayList();

      for (Object __etlItem80 : var4) {
         IntegrationEntity var6 = (IntegrationEntity) __etlItem80;
         if (var6 != null) {
            this.scriptedLinks.add(new ScriptedLink(var6, this));
         } else {
            System.err.println("Null entity encountered in script '" + this.getName() + "'");
         }
      }
   }

   public boolean isPartOfSalesforceWizard() {
      return SalesforceUtils.isSalesforceWizardObject(this);
   }

   public void setPartOfSalesforceWizard(boolean var1) {
      SalesforceUtils.setSalesforceWizardObject(this, var1);
   }

   @Override
   public List<? extends EntityLink> getLinks(IntegrationEntityLookup var1) {
      return this.getScriptedLinks(var1);
   }

   @Override
   public List<ScriptedLink> getScriptedLinks(IntegrationEntityLookup var1) {
      synchronized (this.getDataLock()) {
         if (this.scriptedLinks == null) {
            this.updateScriptedLinks(var1, this.getScript());
         }

         return Lists.newArrayList(this.scriptedLinks);
      }
   }

   @Override
   public void doPostImportProcessing(IntegrationEntityLookup var1) {
      this.rebuildScriptedLinks(var1);
   }

   private void rebuildScriptedLinks(IntegrationEntityLookup var1) {
      synchronized (this.getDataLock()) {
         this.scriptedLinks = null;
         this.getScriptedLinks(var1);
      }
   }

   @Override
   protected void restoreAdditionalData(Persistor var1) {
      String var2 = var1.getText("script");
      this.setProperty("script", var2 != null ? var2 : "");
   }

   @Override
   protected void writeAdditionalData(Persistor var1) {
      var1.putText("script", this.getScript());
   }

   public Script clone() {
      synchronized (this.getDataLock()) {
         Script var2 = (Script)super.clone();
         var2.scriptedLinks = null;
         var2.scripedLinksEventSupport = new ScriptedLinkSourceEventSupport(var2);
         return var2;
      }
   }

   @Override
   protected void validateOtherPropertiesThanTheName(ValidationMessageCollector var1) {
      ScriptStatus var2 = new ScriptValidator().validate(this.getScript());
      switch (var2.getState()) {
         case INVALID:
            var1.addMessage("The script has syntax errors", ValidationStatus.INVALID);
            break;
         case WARNING:
            var1.addMessage(var2.getMessage(), ValidationStatus.WARNING);
         case VALID:
      }
   }

   @Override
   protected Set<String> getKeysToSkipWhenPersisting() {
      return Collections.singleton("script");
   }

   @Override
   public void replaceReferences(ReferenceReplacementService var1) {
      synchronized (this.getDataLock()) {
         if (this.scriptedLinks == null) {
            this.updateScriptedLinks(var1.getItemLookup(), this.getScript());
         }

         ScriptReferenceReplacer var3 = new ScriptReferenceReplacer(this, var1, this.scriptedLinks);
         if (var3.replaceReferences()) {
            this.updateScriptedLinks(var1.getItemLookup(), this.getScript());
         }
      }
   }

   @Override
   public void importAdditionalDataFrom(IntegrationEntity var1) {
      this.scriptedLinks = null;
   }

   @Override
   public boolean originatesIn(IntegrationEntity var1) {
      return var1 == this;
   }

   @Override
   public boolean isDeepCopyable() {
      return true;
   }

   @Override
   public boolean matches(EntityFilter var1) {
      return var1.apply(this);
   }

   @Override
   public void addScriptedLinkSourceListener(ScriptedLinkSourceListener var1) {
      this.scripedLinksEventSupport.addListener(var1);
   }

   @Override
   public void removeScriptedLinkSourceListener(ScriptedLinkSourceListener var1) {
      this.scripedLinksEventSupport.removeListener(var1);
   }
}
