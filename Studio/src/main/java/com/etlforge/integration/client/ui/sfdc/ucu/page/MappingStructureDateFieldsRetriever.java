package com.etlforge.integration.client.ui.sfdc.ucu.page;

import com.google.common.base.Optional;
import java.util.List;
import org.etlforge.application.cache.DataCacheException;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.integration.client.project.ManagedProject;
import org.etlforge.integration.client.project.ProjectUtils;
import org.etlforge.integration.client.structure.cache.InputAndOutputMappingTreeStructureCache;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.salesforce.wsdl.partner.crom.SObjectFieldRetriever;
import org.etlforge.integration.structure.crom.CROM_Tree;
import org.etlforge.integration.structure.crom.CromTreeException;
import org.etlforge.integration.structure.mapping.MappingTreeStructure;

final class MappingStructureDateFieldsRetriever implements DateFieldsRetriever {
   private final Transformation tx;
   private final String sobjectName;

   public MappingStructureDateFieldsRetriever(Transformation var1, String var2) {
      this.tx = var1;
      this.sobjectName = var2;
   }

   @Override
   public Optional<DateFields> getDateFields() throws Exception {
      CROM_Tree var1 = this.loadCromTree();
      if (var1 == null) {
         return Optional.absent();
      }

      SObjectFieldRetriever var2 = new SObjectFieldRetriever(var1);
      List var3 = var2.listFields(this.sobjectName);
      if (var3.isEmpty()) {
         return Optional.absent();
      }

      DateFields var4 = DateFields.fromFieldList(var3);
      return Optional.of(var4);
   }

   private CROM_Tree loadCromTree() {
      ManagedProject var1 = ProjectUtils.getManagedProject(this.tx);
      if (var1 == null) {
         ErrorLog.log(this, null, new Exception("Asked to update Date Conversion mappins for a SFDC request Transformation that does not belong to a project."));
         return null;
      }

      try {
         InputAndOutputMappingTreeStructureCache var2 = var1.getTransformationMappingStructureCache();
         Optional var3 = var2.getOutputStructure(this.tx.getID());
         if (var3.isPresent()) {
            return CROM_Tree.parse((MappingTreeStructure)var3.get());
         }
      } catch (DataCacheException var4) {
         ErrorLog.log(this, null, var4);
      } catch (CromTreeException var5) {
         ErrorLog.log(this, null, var5);
      }

      return null;
   }
}
