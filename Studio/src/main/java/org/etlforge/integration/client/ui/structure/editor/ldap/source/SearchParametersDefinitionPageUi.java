package org.etlforge.integration.client.ui.structure.editor.ldap.source;

import com.google.common.collect.Maps;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.etlforge.collections.Iterables2;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.ldap.LdapSearchParameters;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.system.Disposable;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.undo.UndoRedoInstaller;
import org.etlforge.ui.util.UiUtils;

final class SearchParametersDefinitionPageUi implements UiProvider, Disposable {
   private final JComponent displayer = new JPanel(new BorderLayout());
   private final DefinerListLayout layout;
   private final PropertyChangeListener ldapSourceListener;
   private LdapLocation ldapSource;
   private Map<NumericOid, SearchParametersDefiner> definers;
   private SearchParametersDefiner giveFocusTo;
   private UndoRedoInstaller undoRedoInstaller;

   public SearchParametersDefinitionPageUi() {
      this.displayer.setOpaque(false);
      this.layout = new TabPaneLayout(this.displayer, "Center");
      this.ldapSourceListener = new SearchParametersDefinitionPageUi.BasePathListener();
   }

   @Override
   public void dispose() {
      if (this.ldapSource != null) {
         this.ldapSource.removePropertyChangeListener("path", this.ldapSourceListener);
      }
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.displayer);
   }

   @Override
   public void requestFocus() {
      this.giveFocusTo.requestFocus();
   }

   public void applyTo(LdapStructure var1) {
      var1.clearSourceParameters();

      for (NumericOid var5 : var1.getStructuralObjectClassOids()) {
         SearchParametersDefiner var6 = this.definers.get(var5);
         if (var6 != null) {
            var6.applyTo(var1);
         }
      }
   }

   public void setUndoRedoInstaller(UndoRedoInstaller var1) {
      this.undoRedoInstaller = var1;
      this.installUndoRedo();
   }

   public void setStructureEntries(LdapLocation var1, LdapStructureEntryNode[] var2) {
      this.listenToLdapSourceLocation(this.ldapSource, var1);
      this.ldapSource = var1;
      this.definers = this.createDefiners(var1, var2);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            SearchParametersDefinitionPageUi.this.layoutDefiners();
            SearchParametersDefinitionPageUi.this.installUndoRedo();
         }
      });
   }

   public void setStructureToEdit(LdapStructure var1) {
      for (NumericOid var5 : var1.getStructuralObjectClassOids()) {
         LdapSearchParameters var6 = var1.getSearchParameters(var5);
         if (var6 != null) {
            SearchParametersDefiner var7 = this.definers.get(var5);
            if (var7 != null) {
               var7.setSearchParameters(var6);
            }
         }
      }
   }

   private void layoutDefiners() {
      List var1 = Iterables2.sortedCopy(this.definers.values(), new Comparator<SearchParametersDefiner>() {
         public int compare(SearchParametersDefiner var1, SearchParametersDefiner var2) {
            return var1.getClassName().compareToIgnoreCase(var2.getClassName());
         }
      });
      this.layout.layoutParameterDefiners(var1);
   }

   private Map<NumericOid, SearchParametersDefiner> createDefiners(LdapLocation var1, LdapStructureEntryNode[] var2) {
      HashMap var3 = Maps.newHashMap();

      for (LdapStructureEntryNode var7 : var2) {
         SearchParametersDefiner var8 = this.createDefiner(var1, var7);
         var3.put(var7.getEntry().getOid(), var8);
         this.giveFocusTo = var8;
      }

      return var3;
   }

   private SearchParametersDefiner createDefiner(LdapLocation var1, LdapStructureEntryNode var2) {
      SearchParametersDefiner var3 = new SearchParametersDefiner(var2, var1.getBaseName());
      if (this.definers != null) {
         SearchParametersDefiner var4 = this.definers.get(var2.getEntry().getOid());
         if (var4 != null) {
            var3.setSearchParameters(var4.getSearchParameters());
         }
      }

      return var3;
   }

   private void installUndoRedo() {
      if (this.undoRedoInstaller != null) {
         this.undoRedoInstaller.installIntoComponentTree(this.displayer);
      }
   }

   private void listenToLdapSourceLocation(LdapLocation var1, LdapLocation var2) {
      if (var1 != var2) {
         if (var1 != null) {
            var1.removePropertyChangeListener("path", this.ldapSourceListener);
         }

         var2.addPropertyChangeListener("path", this.ldapSourceListener);
      }
   }

   private class BasePathListener implements PropertyChangeListener {
      private BasePathListener() {
      }

      @Override
      public void propertyChange(PropertyChangeEvent var1) {
         String var2 = SearchParametersDefinitionPageUi.this.ldapSource.getBaseName();

         for (SearchParametersDefiner var4 : SearchParametersDefinitionPageUi.this.definers.values()) {
            var4.setSourceBasePath(var2);
         }
      }
   }
}
