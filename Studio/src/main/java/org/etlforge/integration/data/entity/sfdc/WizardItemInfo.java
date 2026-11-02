package org.etlforge.integration.data.entity.sfdc;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import org.etlforge.integration.data.entity.SalesforceBulkLoad;
import org.etlforge.integration.data.entity.SalesforceWizardEntity;
import org.etlforge.integration.data.entity.Transformation;
import org.etlforge.integration.data.structure.DataStructure;
import org.etlforge.integration.data.structure.DataStructurePredicates;
import org.etlforge.integration.data.structure.DataStructureType;

public final class WizardItemInfo {
   private final SalesforceWizardEntity item;

   public WizardItemInfo(SalesforceWizardEntity var1) {
      this.item = Preconditions.checkNotNull(var1);
   }

   public boolean readsFromDatabase() {
      return this.testRequest(DataStructurePredicates.ofType(DataStructureType.Database));
   }

   public boolean hasSourceData() {
      switch (this.item.getMethod()) {
         case QUERY:
         case QUERY_ALL:
            return false;
         case BULK_LOAD:
            return ((SalesforceBulkLoad)this.item).isLoad();
         default:
            return !this.testRequest(DataStructurePredicates.ofType(DataStructureType.None));
      }
   }

   public boolean writesToFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return this.testResponse(DataStructurePredicates.FILE_BASED);
         case BULK_LOAD:
            return ((SalesforceBulkLoad)this.item).isQuery();
         default:
            return false;
      }
   }

   private boolean testResponse(final Predicate<DataStructure> var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(this.item);
      Optional var3 = var2.mapResponse(new Function<Transformation, Boolean>() {
         public Boolean apply(Transformation var1x) {
            DataStructure var2x = var1x.getOutputStructure();
            return var1.apply(var2x);
         }
      });
      return var3.isPresent() && (Boolean)var3.get();
   }

   public boolean writesToTextFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return this.testResponse(DataStructurePredicates.TEXT_BASED);
         case BULK_LOAD:
            return ((SalesforceBulkLoad)this.item).isQuery();
         default:
            return false;
      }
   }

   public boolean readsFromFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return false;
         case BULK_LOAD:
            return !((SalesforceBulkLoad)this.item).isQuery();
         default:
            return this.testRequest(DataStructurePredicates.FILE_BASED);
      }
   }

   private boolean testRequest(final Predicate<DataStructure> var1) {
      WizardItemBrowser var2 = new WizardItemBrowser(this.item);
      Optional var3 = var2.mapRequest(new Function<Transformation, Boolean>() {
         public Boolean apply(Transformation var1x) {
            DataStructure var2x = var1x.getInputStructure();
            return var1.apply(var2x);
         }
      });
      return var3.isPresent() && (Boolean)var3.get();
   }

   public boolean readsFromTextFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return false;
         case BULK_LOAD:
            return !((SalesforceBulkLoad)this.item).isQuery();
         default:
            return this.testRequest(DataStructurePredicates.TEXT_BASED);
      }
   }

   public boolean readsFromFlatTextFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return false;
         case BULK_LOAD:
            return !((SalesforceBulkLoad)this.item).isQuery();
         default:
            return this.testRequest(DataStructurePredicates.ofType(DataStructureType.Text));
      }
   }

   public boolean writesToFlatTextFile() {
      switch (this.item.getMethod()) {
         case QUERY:
            return this.testResponse(DataStructurePredicates.ofType(DataStructureType.Text));
         case BULK_LOAD:
            return ((SalesforceBulkLoad)this.item).isQuery();
         default:
            return false;
      }
   }

   public Optional<DataStructureType> getResponseStructureType() {
      WizardItemBrowser var1 = new WizardItemBrowser(this.item);
      return var1.mapResponse(new Function<Transformation, DataStructureType>() {
         public DataStructureType apply(Transformation var1) {
            DataStructure var2 = var1.getOutputStructure();
            return var2 == null ? null : var2.getStructureType();
         }
      });
   }
}
