package org.etlforge.integration.client.ui.interchange.transformation.wizard;

import org.etlforge.integration.client.ui.interchange.transformation.wizard.db.DatabaseSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.db.DatabaseTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.edi.EdiSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.edi.EdiTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.json.JsonSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.json.JsonTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.ldap.LdapSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.ldap.LdapTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.text.TextSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.text.TextTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice.RequestAsSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice.RequestAsTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice.ResponseAsSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.webservice.ResponseAsTargetPageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.xml.XmlSourcePageChainFactory;
import org.etlforge.integration.client.ui.interchange.transformation.wizard.xml.XmlTargetPageChainFactory;
import org.etlforge.integration.data.Locatable;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.location.DataLocation;
import org.etlforge.integration.data.location.DatabaseLocation;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.DatabaseStructure;
import org.etlforge.integration.data.structure.JsonStructure;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.NoStructure;
import org.etlforge.integration.data.structure.TextStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.data.structure.XmlStructure;

public final class WizardTypes {
   public static final WizardType DATABASE = new WizardType(
      "Database", "Database", WizardType.Nature.FLAT_OR_HIERARCHICAL, new DatabaseSourcePageChainFactory(), new DatabaseTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToLocatable(Locatable var1) {
         DataLocation var2 = var1.getLocation();
         return var2 instanceof DatabaseLocation;
      }

      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof DatabaseStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof DatabaseStructure;
      }
   };
   public static final WizardType TEXT = new WizardType(
      "Text", "Text", WizardType.Nature.FLAT_OR_HIERARCHICAL, new TextSourcePageChainFactory(), new TextTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof TextStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof TextStructure;
      }
   };
   public static final WizardType XML = new WizardType(
      "XML", "XML", WizardType.Nature.HIERARCHICAL, new XmlSourcePageChainFactory(), new XmlTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof XmlStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof XmlStructure;
      }
   };
   public static final WizardType WEBSERVICE_REQUEST = new WizardType(
      "WebServiceRequest", "Web Service Request", WizardType.Nature.HIERARCHICAL, new RequestAsSourcePageChainFactory(), new RequestAsTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof WebServiceRequestStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof WebServiceRequestStructure;
      }
   };
   public static final WizardType WEBSERVICE_RESPONSE = new WizardType(
      "WebServiceResponse",
      "Web Service Response",
      WizardType.Nature.HIERARCHICAL,
      new ResponseAsSourcePageChainFactory(),
      new ResponseAsTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof WebServiceResponseStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof WebServiceResponseStructure;
      }
   };
   public static final WizardType LDAP = new WizardType(
      "LDAP", "LDAP", WizardType.Nature.HIERARCHICAL, new LdapSourcePageChainFactory(), new LdapTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToLocatable(Locatable var1) {
         DataLocation var2 = var1.getLocation();
         return var2 instanceof LdapLocation;
      }

      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof LdapStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof LdapStructure;
      }
   };
   public static final WizardType JSON = new WizardType(
      "JSON", "JSON", WizardType.Nature.HIERARCHICAL, new JsonSourcePageChainFactory(), new JsonTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof JsonStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof JsonStructure;
      }
   };
   public static final WizardType EDI = new WizardType(
      "EDI", "EDI", WizardType.Nature.HIERARCHICAL, new EdiSourcePageChainFactory(), new EdiTargetPageChainFactory()
   ) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return false;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return false;
      }
   };
   public static final WizardType NONE = new WizardType("NONE", "(None)", WizardType.Nature.FLAT, new NoSourceStructurePageChainFactory(), null) {
      @Override
      public boolean isTiedToInput(Transformation var1) {
         return var1.getInputStructure() instanceof NoStructure;
      }

      @Override
      public boolean isTiedToOutput(Transformation var1) {
         return var1.getOutputStructure() instanceof NoStructure;
      }
   };

   private WizardTypes() {
   }
}
