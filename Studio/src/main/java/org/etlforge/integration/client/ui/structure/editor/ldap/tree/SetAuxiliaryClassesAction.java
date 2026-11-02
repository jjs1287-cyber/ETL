package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import java.awt.Window;
import javax.swing.RootPaneContainer;
import org.etlforge.application.errorlog.ErrorLog;
import org.etlforge.application.ui.BusyWorker;
import org.etlforge.integration.client.ldap.ui.auxiliary.AuxiliaryClassSelector;
import org.etlforge.integration.client.ldap.ui.auxiliary.AuxiliaryClassSelectorCallback;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.wait.WaitService;
import org.etlforge.ui.wait.WindowWaitService;

class SetAuxiliaryClassesAction extends AbstractClassNodeAction {
   private LdapStructureServerCache ldapCache;

   public SetAuxiliaryClassesAction(AttributeSelectionTree var1) {
      super(var1, PackageResources.Tree.SetAuxClasses.NAME);
      this.putValue("ShortDescription", PackageResources.Tree.SetAuxClasses.TOOLTIP);
   }

   public void setLdapCache(LdapStructureServerCache var1) {
      this.ldapCache = var1;
   }

   @Override
   protected void runOnNode(final ClassNameNode var1) {
      this.setEnabled(false);
      final WaitService var2 = this.getWaitService();
      Runnable var3 = new Runnable() {
         @Override
         public void run() {
            SetAuxiliaryClassesAction.this.lanchAuxiliarySelector(var1, var2);
         }
      };
      new BusyWorker(var2, var3).run();
   }

   private WaitService getWaitService() {
      Window var1 = UiUtils.getActiveWindow();
      return var1 instanceof RootPaneContainer ? new WindowWaitService((RootPaneContainer)var1) : WaitService.NONE;
   }

   private void lanchAuxiliarySelector(ClassNameNode var1, WaitService var2) {
      AttributeSelectionModel var3 = this.getModel();
      NumericOid[] var4 = var3.getAuxiliaryClassesFor(var1.getOid());
      SetAuxiliaryClassesAction.SelectorCallback var5 = new SetAuxiliaryClassesAction.SelectorCallback(var1);
      AuxiliaryClassSelector var6 = var3.getType() == SourceTarget.Source
         ? AuxiliaryClassSelector.newAuxClassSelectorForSource()
         : AuxiliaryClassSelector.newAuxClassSelectorForTarget();
      var6.setLdapCache(this.ldapCache);
      String var7 = var1.getUserObject().getName();
      if (var7 == null) {
         var7 = var1.getOid().toString();
      }

      var6.select(var3.getLocation(), var7, var4, var5, var2);
   }

   private class SelectorCallback implements AuxiliaryClassSelectorCallback {
      private final NumericOid classOid;

      public SelectorCallback(ClassNameNode var2) {
         this.classOid = var2.getOid();
      }

      @Override
      public void cancelled() {
         SetAuxiliaryClassesAction.this.setEnabled(true);
      }

      @Override
      public void completed(ObjectClassStructure[] var1) {
         SetAuxiliaryClassesAction.this.setEnabled(true);
         SetAuxiliaryClassesAction.this.getModel().setAuxiliaryClasses(this.classOid, var1);
      }

      @Override
      public void failed(Exception var1) {
         SetAuxiliaryClassesAction.this.setEnabled(true);
         ErrorLog.attention(SetAuxiliaryClassesAction.class, "An error occurred", var1);
      }
   }
}
