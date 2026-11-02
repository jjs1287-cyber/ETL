package org.etlforge.integration.uidelegate.entity.tree;

import com.google.common.collect.ComparisonChain;
import java.io.Serializable;
import java.util.Comparator;
import org.etlforge.integration.data.entity.Folder;
import org.etlforge.integration.data.entity.ETLForgeConnectNames;
import org.etlforge.ui.widget.tree.KongaTreeNode;

public final class ProjectTreeNodeOrder implements Comparator<KongaTreeNode>, Serializable {
   public int compare(KongaTreeNode var1, KongaTreeNode var2) {
      IntegrationEntityTreeNode var3 = (IntegrationEntityTreeNode)var1;
      IntegrationEntityTreeNode var4 = (IntegrationEntityTreeNode)var2;
      boolean var5 = var3.getUserObject() instanceof Folder;
      boolean var6 = var4.getUserObject() instanceof Folder;
      if (var5 && var6) {
         return this.compareFolderNodes(var3, var4);
      } else if (var5) {
         return -1;
      } else {
         return var6
            ? 1
            : ComparisonChain.start()
               .compare(var4.getSortOrdinal(), var3.getSortOrdinal())
               .compare(var3.getDisplayName(), var4.getDisplayName(), String.CASE_INSENSITIVE_ORDER)
               .result();
      }
   }

   private int compareFolderNodes(IntegrationEntityTreeNode var1, IntegrationEntityTreeNode var2) {
      Folder var3 = (Folder)var1.getUserObject();
      Folder var4 = (Folder)var2.getUserObject();
      if (var3.isEtlforgeConnectFolder()) {
         return -1;
      }

      if (var4.isEtlforgeConnectFolder()) {
         return 1;
      }

      boolean var5 = ETLForgeConnectNames.isConnectWizardFolder(var3);
      boolean var6 = ETLForgeConnectNames.isConnectWizardFolder(var4);
      if (var5 && var6) {
         boolean var7 = var3.isSalesforceFolder();
         boolean var8 = var4.isSalesforceFolder();
         if (var7 && var8) {
            if (var3.isSharedSalesforceItemsFolder()) {
               return 1;
            }

            if (var4.isSharedSalesforceItemsFolder()) {
               return -1;
            }
         } else {
            if (var7) {
               return 1;
            }

            if (var8) {
               return -1;
            }
         }
      } else {
         if (var5) {
            return -1;
         }

         if (var6) {
            return 1;
         }
      }

      return var1.getDisplayName().compareToIgnoreCase(var2.getDisplayName());
   }
}
