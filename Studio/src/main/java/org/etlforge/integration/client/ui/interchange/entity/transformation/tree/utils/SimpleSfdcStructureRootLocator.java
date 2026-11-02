package org.etlforge.integration.client.ui.interchange.entity.transformation.tree.utils;

import org.apache.commons.lang.StringUtils;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.transformation.SalesforceWizardRole;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.WebServiceRequestStructure;
import org.etlforge.integration.data.structure.WebServiceResponseStructure;
import org.etlforge.integration.salesforce.SfdcMethod;
import org.etlforge.integration.salesforce.config.SalesforceConfig;
import org.etlforge.integration.salesforce.config.SfdcMethodConfig;
import org.etlforge.integration.salesforce.wsdl.SalesforceWsdlConstants;
import org.etlforge.integration.structure.crom.CROM;
import org.etlforge.xml.ExpandedName;

public final class SimpleSfdcStructureRootLocator {
   private final TreeComponent sourceTree;
   private final DataStructure sourceStructure;
   private final TreeComponent targetTree;
   private final DataStructure targetStructure;
   private final ExpandedName objectName;
   private final boolean partnerWsdl;
   private SalesforceWizardRole role;
   private SfdcMethodConfig methodConfig;
   private TreeComponent currentTree;
   private SalesforceWizardRole.Position currentPosition;

   public SimpleSfdcStructureRootLocator(Transformation var1, TreeComponent var2, TreeComponent var3) {
      this.sourceTree = var2;
      this.sourceStructure = var1.getInputStructure();
      this.targetTree = var3;
      this.targetStructure = var1.getOutputStructure();
      this.role = var1.getSalesforceWizardRole();
      if (this.role != null) {
         this.methodConfig = this.lookupMethodConfig();
      }

      this.objectName = var1.getSalesforceObject();
      this.partnerWsdl = this.isPartnerWsdl();
   }

   private SfdcMethodConfig lookupMethodConfig() {
      SalesforceConfig var1 = SalesforceConfig.getInstance();
      return var1.getMethodConfig(this.role.method);
   }

   private boolean isPartnerWsdl() {
      return this.objectName != null && SalesforceWsdlConstants.isPartnerWsdlObject(this.objectName);
   }

   public void locateSimpleRepresentationRoot() {
      this.checkSourceTree();
      this.checkTargetTree();
   }

   private void checkSourceTree() {
      if (this.isSourceWebServiceResponse()) {
         this.checkBackwardsCompatibility();
         this.checkTree(this.sourceTree, SalesforceWizardRole.Position.RESPONSE);
      }
   }

   private boolean isSourceWebServiceResponse() {
      return this.sourceStructure instanceof WebServiceResponseStructure;
   }

   private void checkBackwardsCompatibility() {
      if (this.role == null) {
         this.role = SalesforceWizardRole.response(SfdcMethod.QUERY);
         this.methodConfig = this.lookupMethodConfig();
      }
   }

   private void checkTree(TreeComponent var1, SalesforceWizardRole.Position var2) {
      assert this.role != null;
      assert this.methodConfig != null;
      if (!this.partnerWsdl || this.role.position != SalesforceWizardRole.Position.RESPONSE || this.role.method == SfdcMethod.QUERY) {
         this.currentPosition = var2;
         this.currentTree = var1;
         String var3 = this.getParentPathForSObject();
         if (var3 != null) {
            var3 = this.applyPartnerWsdlRequestHack(var2, var3);
            Node var4 = this.getParent(var3);
            if (var4 != null) {
               var4.expandNode();
               if (this.objectName == null) {
                  if (this.hasSimplifiedMappingTree()) {
                     var1.setSimpleSfdcRepresentationRoot(var4);
                  }
               } else {
                  if (this.isObjectAlreadyThere(var4)) {
                     return;
                  }

                  if (this.partnerWsdl) {
                     this.handlePartnerWsdlWizard(var1, var4);
                  } else {
                     this.handleEnterpriseWsdlWizard(var1, var4);
                  }
               }
            }
         }
      }
   }

   private String applyPartnerWsdlRequestHack(SalesforceWizardRole.Position var1, String var2) {
      if (this.partnerWsdl) {
         String var3 = "sObjects.";
         if (var1 == SalesforceWizardRole.Position.REQUEST && var2.endsWith(var3)) {
            var2 = StringUtils.substringBeforeLast(var2, var3);
         }
      }

      return var2;
   }

   private boolean hasSimplifiedMappingTree() {
      switch (this.currentPosition) {
         case REQUEST:
            return this.methodConfig.requestHasSimpleMappingTreeRepresentation();
         case RESPONSE:
            return this.methodConfig.responseHasSimpleMappingTreeRepresentation();
         default:
            throw new RuntimeException("Unexpected position: " + this.currentPosition);
      }
   }

   private String getParentPathForSObject() {
      switch (this.currentPosition) {
         case REQUEST:
            return this.methodConfig.getSimpleRootPathInRequest();
         case RESPONSE:
            return this.methodConfig.getSimpleRootPathInResponse();
         default:
            throw new RuntimeException("Unexpected position: " + this.currentPosition);
      }
   }

   private Node getParent(String var1) {
      Node var2 = this.currentTree.findNode_expandTreeIfNotExist("[" + var1 + "]");
      if (var2 == null) {
         this.reportMissingParent(var1);
      }

      return var2;
   }

   private void reportMissingParent(String var1) {
      Exception var2 = new Exception("Could not locate the path " + var1 + " in the " + this.role.position.toString().toLowerCase() + " tree. This is a bug.");
      ErrorLog.attention(this, null, var2);
   }

   private boolean isObjectAlreadyThere(Node var1) {
      for (Node var3 : var1.getChildren()) {
         CROM var4 = var3.getCROM();
         if (var4.isTypeDefinition() && var4.getName().equals(this.objectName.getLocalName())) {
            if (this.hasSimplifiedMappingTree()) {
               this.currentTree.setSimpleSfdcRepresentationRoot(var3);
            }

            return true;
         }
      }

      return false;
   }

   private void handlePartnerWsdlWizard(TreeComponent var1, Node var2) {
      Node var3 = null;

      for (int var4 = 0; var4 < var2.getChildCount(); var4++) {
         Node var5 = var2.getChildAt(var4);
         if (this.objectName.getLocalName().equals(var5.getName())) {
            var3 = var5;
            break;
         }
      }

      if (var3 != null && this.hasSimplifiedMappingTree()) {
         var3.expandNode();
         var3.setShowMode(1);
         var1.setSimpleSfdcRepresentationRoot(var3);
      }
   }

   private void handleEnterpriseWsdlWizard(TreeComponent var1, Node var2) {
      CROM var3 = this.getTypeDef();
      if (var3 != null) {
         Node var4 = var2.insertDerivedTypeNode(var3, null);
         if (var4 != null && this.hasSimplifiedMappingTree()) {
            var1.setSimpleSfdcRepresentationRoot(var4);
         }
      }
   }

   private CROM getTypeDef() {
      assert this.objectName != null;
      CROM var1 = this.currentTree.get_element_type(this.objectName.toString());
      if (var1 == null) {
         this.reportMissingObjectType();
      }

      return var1;
   }

   private void reportMissingObjectType() {
      Exception var1 = new Exception("Could not insert the salesforce object " + this.objectName.getLocalName() + " into the mapping tree. This is a bug.");
      ErrorLog.attention(this, null, var1);
   }

   private void checkTargetTree() {
      if (this.isTargetWebServiceRequest()) {
         this.checkTree(this.targetTree, SalesforceWizardRole.Position.REQUEST);
      }
   }

   private boolean isTargetWebServiceRequest() {
      return this.targetStructure instanceof WebServiceRequestStructure;
   }
}
