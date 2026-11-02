package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;

final class Memento {
   private final SelectedAttributes selection;
   private final Map<NumericOid, NumericOid[]> auxClassInfo;

   public Memento(AttributeSelectionModel var1) {
      this.selection = new SelectedAttributes(var1);
      this.auxClassInfo = Maps.newHashMap();

      for (NumericOid var3 : var1.getClassOids()) {
         this.auxClassInfo.put(var3, var1.getAuxiliaryClassesFor(var3));
      }
   }

   public void restore(AttributeSelectionModel var1) {
      this.selection.applyTo(var1);
      if (this.hasAuxiliaryClassesToRestore()) {
         this.setAuxiliaryClassInfo(var1, this.auxClassInfo);
      }
   }

   private boolean hasAuxiliaryClassesToRestore() {
      for (NumericOid[] var2 : this.auxClassInfo.values()) {
         if (var2.length > 0) {
            return true;
         }
      }

      return false;
   }

   private void setAuxiliaryClassInfo(AttributeSelectionModel var1, Map<NumericOid, NumericOid[]> var2) {
      for (Entry var4 : var2.entrySet()) {
         Memento.AuxClassInfoRestorer var5 = new Memento.AuxClassInfoRestorer(var1, (NumericOid)var4.getKey(), (NumericOid[])var4.getValue());
         var5.execute();
      }
   }

   private static class AuxClassInfoRestorer extends SwingWorker<ObjectClassStructure[], Void> {
      private final AttributeSelectionModel model;
      private final NumericOid classOid;
      private final NumericOid[] auxClassOids;

      public AuxClassInfoRestorer(AttributeSelectionModel var1, NumericOid var2, NumericOid[] var3) {
         this.model = var1;
         this.classOid = var2;
         this.auxClassOids = var3;
      }

      protected ObjectClassStructure[] doInBackground() throws Exception {
         AuxClassInfoTranslator var1 = new AuxClassInfoTranslator(this.model.getLocation(), this.model.getLdapCache());
         return var1.toStructure(this.auxClassOids);
      }

      @Override
      protected void done() {
         try {
            ObjectClassStructure[] var1 = this.get();
            this.model.setAuxiliaryClasses(this.classOid, var1);
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
         } catch (ExecutionException var3) {
            var3.printStackTrace();
         }
      }
   }
}
