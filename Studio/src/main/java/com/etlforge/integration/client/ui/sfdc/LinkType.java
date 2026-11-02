package com.etlforge.integration.client.ui.sfdc;

import com.etlforge.integration.client.ui.sfdc.page.EditableLink;
import com.etlforge.integration.client.ui.sfdc.page.EditableLinkRestrictions;
import com.etlforge.integration.client.ui.sfdc.page.LinkToImplementingObject;
import com.etlforge.integration.client.ui.sfdc.page.StaticLink;
import org.etlforge.integration.client.ui.entity.EntityExplorerSupport;
import org.etlforge.integration.data.entity.EntityType;
import org.etlforge.integration.data.entity.Operation;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.Source;
import org.etlforge.integration.data.entity.SystemSource;
import org.etlforge.integration.data.entity.Target;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.entity.sfdc.WizardItemBrowser;
import org.etlforge.integration.salesforce.bulkload.BulkAction;

public enum LinkType {
   OPERATION("Operation:") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         Operation var4 = var2.getImplementingOperation();
         return var4 == null ? StaticLink.emptyLink(EntityType.Operation, var3) : new StaticLink(var1, var4, var3);
      }
   },
   SOURCE("Read data from:") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         Source var4 = var2.getOperationSource();
         EditableLinkRestrictions var5 = EditableLinkRestrictions.forSource(var2);
         return var5.isEditable() ? this.createEditableLink(var1, var4, var5, var3) : this.createStaticLink(var1, var4, var3);
      }

      private LinkToImplementingObject createEditableLink(EntityExplorerSupport var1, Source var2, EditableLinkRestrictions var3, String var4) {
         if (var2 == null) {
            return EditableLink.emptyLink(var1, Source.class, EntityType.Source, var3, var4);
         } else {
            return var2 instanceof SystemSource ? this.createStaticLink(var1, var2, var4) : new EditableLink<>(var1, var2, var3, var4);
         }
      }

      private LinkToImplementingObject createStaticLink(EntityExplorerSupport var1, Source var2, String var3) {
         return var2 == null ? StaticLink.emptyLink(EntityType.Source, var3) : new StaticLink(var1, var2, var3);
      }
   },
   TARGET("Write data to:") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         Target var4 = var2.getOperationTarget();
         EditableLinkRestrictions var5 = EditableLinkRestrictions.forTarget(var2);
         return var5.isEditable() ? this.createEditableLink(var1, var4, var5, var3) : this.createStaticLink(var1, var4, var3);
      }

      private LinkToImplementingObject createEditableLink(EntityExplorerSupport var1, Target var2, EditableLinkRestrictions var3, String var4) {
         return var2 == null ? EditableLink.emptyLink(var1, Target.class, EntityType.Target, var3, var4) : new EditableLink<>(var1, var2, var3, var4);
      }

      private LinkToImplementingObject createStaticLink(EntityExplorerSupport var1, Target var2, String var3) {
         return var2 == null ? StaticLink.emptyLink(EntityType.Target, var3) : new StaticLink(var1, var2, var3);
      }
   },
   REQUEST("&Mappings:") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         Transformation var4 = var2.getRequestTransformation();
         return var4 == null ? StaticLink.emptyLink(EntityType.Transformation, var3) : new StaticLink(var1, var4, var3);
      }
   },
   RESPONSE("&Mappings:") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         Transformation var4 = var2.getResponseTransformation();
         return var4 == null ? StaticLink.emptyLink(EntityType.Transformation, var3) : new StaticLink(var1, var4, var3);
      }
   },
   BULKLOAD_FILE("") {
      @Override
      protected LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3) {
         SalesforceBulkLoad var4 = (SalesforceBulkLoad)var2.getWizardItem();
         BulkAction var5 = var4.getAction();
         return var5.isLoad() ? SOURCE.getLink(var1, var2) : TARGET.getLink(var1, var2);
      }
   };

   private final String label;

   LinkType(String var3) {
      this.label = var3;
   }

   public final LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2) {
      return this.getLink(var1, var2, this.label);
   }

   protected abstract LinkToImplementingObject getLink(EntityExplorerSupport var1, WizardItemBrowser var2, String var3);
}
