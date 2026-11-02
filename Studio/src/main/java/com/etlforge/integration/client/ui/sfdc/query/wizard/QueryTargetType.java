package com.etlforge.integration.client.ui.sfdc.query.wizard;

import org.etlforge.application.ui.wizard.WizardPage;
import org.etlforge.application.ui.wizard.WizardPageChain;
import org.etlforge.application.ui.wizard.WizardPageList;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.client.ui.structure.editor.db.DatabaseStructureWizardPageChain;
import org.etlforge.integration.client.ui.structure.editor.json.JsonPageChain;
import org.etlforge.integration.client.ui.structure.editor.ldap.LdapStructurePageChain;
import org.etlforge.integration.client.ui.structure.editor.xml.wizard.XmlStructureWizardPageChain;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.entity.SalesforceQuery;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.DataStructureType;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.XmlStructure;
import org.etlforge.integration.filetransfer.client.upload.xsd.XsdFileStore;

public enum QueryTargetType {
   DATABASE(DataStructureType.Database) {
      @Override
      public WizardPageChain createPages(QueryWizard var1, Target var2) {
         return new DatabaseStructureWizardPageChain(var1, var2);
      }

      @Override
      public QueryTarget createStructure(QueryWizard var1, WizardPageChain var2) {
         return new QueryTargetType.DatabaseQueryTarget((DatabaseStructureWizardPageChain)var2);
      }
   },
   TEXT(DataStructureType.Text) {
      @Override
      public WizardPageChain createPages(QueryWizard var1, Target var2) {
         WizardPageList var3 = new WizardPageList() {
            @Override
            public boolean canFinish(WizardPage var1) {
               return var1.canContinue() && var1 == this.getPageAt(0);
            }
         };
         ResponseTextStructureFactory var4 = var1.getResponseStructureFactory();
         EntityExplorerSupport var5 = var1.getProjectExplorer();
         TextTargetPage var6 = new TextTargetPage(var1, var5, var4, var1.getWindow());
         var3.addPage(var6);
         return var3;
      }

      @Override
      public QueryTarget createStructure(QueryWizard var1, WizardPageChain var2) {
         return new QueryTargetType.TextQueryTarget(var1.getItemToConfigure(), (WizardPageList)var2);
      }
   },
   XML(DataStructureType.XML) {
      @Override
      public WizardPageChain createPages(QueryWizard var1, Target var2) {
         XsdFileStore var3 = new XsdFileStore(var1.getFileStore());
         return new XmlStructureWizardPageChain(var1, SourceTarget.Target, var3);
      }

      @Override
      public QueryTarget createStructure(QueryWizard var1, WizardPageChain var2) {
         return new QueryTargetType.XmlQueryTarget((XmlStructureWizardPageChain)var2);
      }
   },
   JSON(DataStructureType.JSON) {
      @Override
      public WizardPageChain createPages(QueryWizard var1, Target var2) {
         XsdFileStore var3 = new XsdFileStore(var1.getFileStore());
         return new JsonPageChain(var1, var3);
      }

      @Override
      public QueryTarget createStructure(QueryWizard var1, WizardPageChain var2) {
         return new QueryTargetType.JsonQueryTarget((JsonPageChain)var2);
      }
   },
   LDAP(DataStructureType.LDAP) {
      @Override
      public WizardPageChain createPages(QueryWizard var1, Target var2) {
         LdapStructurePageChain var3 = LdapStructurePageChain.newTargetChain(var1, var1.getProjectExplorer());
         LdapLocation var4 = (LdapLocation)var2.getLocation();
         var3.setFixedLocation(var4);
         return var3;
      }

      @Override
      public QueryTarget createStructure(QueryWizard var1, WizardPageChain var2) {
         return new QueryTargetType.LdapQueryTarget((LdapStructurePageChain)var2);
      }
   };

   private final DataStructureType type;

   QueryTargetType(DataStructureType var3) {
      this.type = var3;
   }

   public abstract WizardPageChain createPages(QueryWizard var1, Target var2);

   public abstract QueryTarget createStructure(QueryWizard var1, WizardPageChain var2);

   @Override
   public final String toString() {
      return this.type.toString();
   }

   private static class DatabaseQueryTarget implements QueryTarget {
      private final DatabaseStructureWizardPageChain pages;

      public DatabaseQueryTarget(DatabaseStructureWizardPageChain var1) {
         this.pages = var1;
      }

      @Override
      public void applyTo(Transformation var1) {
         DatabaseStructure var2 = this.pages.getDefinedStructure();
         var1.setOutputStructure(var2);
         this.pages.applyInsertMode(var1);
         Target var3 = (Target)this.pages.getDatabase();
         var1.setTargetId(var3.getID());
      }
   }

   private static class JsonQueryTarget implements QueryTarget {
      private final JsonStructure json;

      public JsonQueryTarget(JsonPageChain var1) {
         this.json = var1.getDefinedStructure();
      }

      @Override
      public void applyTo(Transformation var1) {
         var1.setOutputStructure(this.json);
      }
   }

   private static class LdapQueryTarget implements QueryTarget {
      private final LdapStructurePageChain pages;

      public LdapQueryTarget(LdapStructurePageChain var1) {
         this.pages = var1;
      }

      @Override
      public void applyTo(Transformation var1) {
         LdapStructure var2 = this.pages.getDefinedStructure();
         var1.setOutputStructure(var2);
         var1.setTargetId(var2.getLdapLocation().getTargetId());
      }
   }

   private static class TextQueryTarget implements QueryTarget {
      private final SalesforceQuery query;
      private final TextTargetPage page;

      public TextQueryTarget(SalesforceQuery var1, WizardPageList var2) {
         this.query = var1;
         this.page = (TextTargetPage)var2.getFirstPage();
      }

      @Override
      public void applyTo(Transformation var1) {
         this.page.applyTo(this.query, var1);
      }
   }

   private static class XmlQueryTarget implements QueryTarget {
      private final XmlStructure xml;

      public XmlQueryTarget(XmlStructureWizardPageChain var1) {
         this.xml = var1.getXmlStructure();
      }

      @Override
      public void applyTo(Transformation var1) {
         var1.setOutputStructure(this.xml);
      }
   }
}
