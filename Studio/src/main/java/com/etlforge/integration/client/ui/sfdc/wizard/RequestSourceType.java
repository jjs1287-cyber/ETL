package com.etlforge.integration.client.ui.sfdc.wizard;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SampleFile;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SourceFileStructure;
import com.etlforge.integration.client.ui.sfdc.wizard.file.SourceFileStructureWizardPageChain;
import java.util.HashSet;
import java.util.Set;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.client.ui.structure.editor.json.JsonPageChain;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapStructurePageChain;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.entity.SalesforceModifier;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.TextDocument;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;
import org.etlforge.system.ThreeValuedBool;

public enum RequestSourceType {
   NONE(DataStructureType.None) {
      @Override
      public WizardPageChain createPages(SalesforceWizard var1, Source var2) {
         return new WizardPageList();
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         return new DefaultRequestSource(new NoStructure());
      }

      @Override
      public String toString() {
         return "(None)";
      }
   },
   DATABASE(DataStructureType.Database) {
      @Override
      public WizardPageChain createPages(SalesforceWizard var1, Source var2) {
         DatabaseStructureWizardPageChain var3 = new DatabaseStructureWizardPageChain(var1, var2);
         var3.setUpdateSourceTableAvailable(ThreeValuedBool.FALSE);
         return var3;
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         DatabaseStructureWizardPageChain var2 = (DatabaseStructureWizardPageChain)var1;
         return new RequestSourceType.DbRequest((Source)var2.getDatabase(), var2.getDefinedStructure());
      }

      @Override
      public String toString() {
         return "Database";
      }
   },
   TEXT(DataStructureType.Text) {
      @Override
      public WizardPageChain createPages(SalesforceWizard var1, Source var2) {
         return new SourceFileStructureWizardPageChain(var1);
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         SourceFileStructure var2 = ((SourceFileStructureWizardPageChain)var1).getTextDocument();
         return new RequestSourceType.TextRequest(var2);
      }

      @Override
      public String toString() {
         return "Text";
      }
   },
   XML(DataStructureType.XML) {
      @Override
      public WizardPageChain createPages(SalesforceWizard<?> var1, Source var2) {
         XmlStructureWizardPageChain var3 = new XmlStructureWizardPageChain(var1, SourceTarget.Source, new XsdFileStore(var1.getFileStore()));
         Optional var4 = var1.getSampleFile();
         if (var4.isPresent()) {
            var3.setPreselectedSampleFile(((SampleFile)var4.get()).asFile());
         }

         return var3;
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         XmlStructure var2 = ((XmlStructureWizardPageChain)var1).getXmlStructure();
         return new DefaultRequestSource(var2);
      }

      @Override
      public String toString() {
         return "XML";
      }
   },
   JSON(DataStructureType.JSON) {
      @Override
      public WizardPageChain createPages(SalesforceWizard<?> var1, Source var2) {
         JsonPageChain var3 = new JsonPageChain(var1, new XsdFileStore(var1.getFileStore()));
         Optional var4 = var1.getSampleFile();
         if (var4.isPresent()) {
            var3.setPreselectedSampleFile(((SampleFile)var4.get()).asFile());
         }

         return var3;
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         JsonStructure var2 = ((JsonPageChain)var1).getDefinedStructure();
         return new DefaultRequestSource(var2);
      }

      @Override
      public String toString() {
         return "JSON";
      }
   },
   LDAP(DataStructureType.LDAP) {
      @Override
      public WizardPageChain createPages(SalesforceWizard var1, Source var2) {
         LdapStructurePageChain var3 = LdapStructurePageChain.newSourceChain(var1, var1.getProjectExplorer());
         LdapLocation var4 = (LdapLocation)var2.getLocation();
         var3.setFixedLocation(var4);
         return var3;
      }

      @Override
      public RequestSource createStructure(WizardPageChain var1) {
         LdapStructurePageChain var2 = (LdapStructurePageChain)var1;
         return new RequestSourceType.LdapRequest(var2.getDefinedStructure());
      }
   };

   private final DataStructureType type;

   RequestSourceType(DataStructureType var3) {
      this.type = var3;
   }

   public abstract WizardPageChain createPages(SalesforceWizard<?> var1, Source var2);

   public abstract RequestSource createStructure(WizardPageChain var1);

   public final boolean requiresSource() {
      return this != NONE;
   }

   @Override
   public String toString() {
      return this.type.toString();
   }

   private static final class DbRequest implements RequestSource {
      private final Source source;
      private final DatabaseStructure structure;

      public DbRequest(Source var1, DatabaseStructure var2) {
         this.source = var1;
         this.structure = var2;
      }

      @Override
      public void applyTo(Transformation var1) {
         var1.setInputStructure(this.structure);
         var1.setSourceId(this.source.getID());
      }

      @Override
      public void applyTo(SalesforceModifier var1) {
      }

      public Set<IntegrationEntity> getItemsCreatedByWizard() {
         return Sets.newHashSet();
      }
   }

   private static final class LdapRequest implements RequestSource {
      private final LdapStructure structure;

      public LdapRequest(LdapStructure var1) {
         this.structure = var1;
      }

      @Override
      public void applyTo(Transformation var1) {
         var1.setInputStructure(this.structure);
         var1.setSourceId(this.structure.getLdapLocation().getSourceId());
      }

      @Override
      public void applyTo(SalesforceModifier var1) {
      }

      public Set<IntegrationEntity> getItemsCreatedByWizard() {
         return Sets.newHashSet();
      }
   }

   private static final class TextRequest implements RequestSource {
      private final SourceFileStructure structure;

      public TextRequest(SourceFileStructure var1) {
         this.structure = var1;
      }

      @Override
      public void applyTo(Transformation var1) {
         TextDocument var2 = this.structure.getDocument();
         var1.setSourceTextDocumentId(var2.getID());
         TextStructure var3 = var2.getDataStructure();
         var1.setInputStructure(var3);
      }

      @Override
      public void applyTo(SalesforceModifier var1) {
         var1.setSourceFileHasHeader(this.structure.hasHeader());
      }

      public Set<IntegrationEntity> getItemsCreatedByWizard() {
         HashSet var1 = Sets.newHashSet();
         TextDocument var2 = this.structure.getDocument();
         if (var2.isTransient()) {
            var1.add(var2);
            var2.setTransient(false);
         }

         return var1;
      }
   }
}
