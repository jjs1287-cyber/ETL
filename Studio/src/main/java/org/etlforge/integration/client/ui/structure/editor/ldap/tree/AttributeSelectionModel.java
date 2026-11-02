package org.etlforge.integration.client.ui.structure.editor.ldap.tree;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.tree.TreePath;
import org.etlforge.collections.IterableEnumeration;
import org.etlforge.integration.client.ldap.ui.SelectedObjectStructures;
import org.etlforge.integration.data.SourceTarget;
import org.etlforge.integration.data.location.LdapLocation;
import org.etlforge.integration.data.structure.LdapStructure;
import org.etlforge.integration.data.structure.ldap.AttributeContributions;
import org.etlforge.integration.data.structure.ldap.LdapStructureTreeNodeFactory;
import org.etlforge.integration.ldap.client.cache.LdapStructureServerCache;
import org.etlforge.integration.ldap.structure.LdapStructureAttribute;
import org.etlforge.integration.ldap.structure.LdapStructureEntry;
import org.etlforge.integration.ldap.structure.LdapStructureEntryNode;
import org.etlforge.integration.ldap.structure.NumericOid;
import org.etlforge.integration.ldap.structure.ObjectClassDefinition;
import org.etlforge.integration.ldap.structure.ObjectClassStructure;
import org.etlforge.ui.util.UiUtils;
import org.etlforge.ui.widget.tree.DefaultKongaTreeModel;
import org.etlforge.ui.widget.tree.DefaultKongaTreeNode;
import org.etlforge.ui.widget.tree.KongaTreeNode;
import org.etlforge.util.name.Nameables;

public final class AttributeSelectionModel extends DefaultKongaTreeModel {
   private final LdapLocation ldapLocation;
   private final SelectedObjectStructures selectedStructures;
   private final Map<NumericOid, ClassNameNode> classNodes = Maps.newHashMap();
   private final Map<NumericOid, AttributeContributions> attributeContributions = Maps.newHashMap();
   private final AuxiliaryClassSelection auxClassSelection;
   private final SourceTarget type;
   private final LdapStructureServerCache ldapCache;

   public AttributeSelectionModel(SourceTarget var1, LdapStructureServerCache var2) {
      super(new DefaultKongaTreeNode("root", false));
      this.ldapCache = var2;
      this.ldapLocation = null;
      this.selectedStructures = null;
      this.type = var1;
      this.auxClassSelection = new AuxiliaryClassSelection(this.attributeContributions);
   }

   public AttributeSelectionModel(LdapLocation var1, SourceTarget var2, SelectedObjectStructures var3, LdapStructure var4, LdapStructureServerCache var5) {
      super(new DefaultKongaTreeNode("root", true));
      this.ldapLocation = var1;
      this.selectedStructures = var3;
      this.ldapCache = var5;
      this.type = var2;
      this.auxClassSelection = new AuxiliaryClassSelection(this.attributeContributions);
      this.buildModel(var3, var4);
   }

   private void buildModel(SelectedObjectStructures var1, LdapStructure var2) {
      Map var3 = this.translateAuxClassInfo(var2);

      for (Entry var5 : var1.getStructuresAlphabetically()) {
         ObjectClassDefinition var6 = (ObjectClassDefinition)var5.getKey();
         ClassNameNode var7 = new ClassNameNode(var6, this.getRoot());
         this.classNodes.put(var6.getOid(), var7);
         ObjectClassStructure var8 = (ObjectClassStructure)var5.getValue();
         if (var8 != null) {
            Collection var9 = this.askStructureForExtendedAttributes(var2, var8);
            AttributeContributions var10 = this.addAttributeContributions(var6.getOid(), var8, var9);
            ObjectClassStructure[] var11 = (ObjectClassStructure[])var3.get(var6.getOid());
            if (var11 != null) {
               this.auxClassSelection.setAuxiliaryClasses(var6.getOid(), var11);
            }

            this.buildTree(var10, var7);
         } else {
            new UnknownStructureNode(var7);
         }
      }

      InitialSelectionState var12 = new InitialSelectionState(var2, this.classNodes.values(), this.type);
      var12.apply();
   }

   private Collection<LdapStructureAttribute> askStructureForExtendedAttributes(LdapStructure var1, ObjectClassStructure var2) {
      return var1 == null ? Collections.emptyList() : var1.getExtendedAttributeInfo().get(var2.getOid());
   }

   private Map<NumericOid, ObjectClassStructure[]> translateAuxClassInfo(LdapStructure var1) {
      if (var1 == null) {
         return Maps.newHashMap();
      }

      AuxClassInfoTranslator var2 = new AuxClassInfoTranslator(var1.getLdapLocation(), this.ldapCache);
      Map var3 = var1.getAuxClassInfo();
      return var2.translate(var3);
   }

   private KongaTreeNode buildTree(AttributeContributions var1, ClassNameNode var2) {
      for (LdapStructureAttribute var4 : var1.getEffectiveAttributes()) {
         AttributeSelectionNode.create(var4, this.type, var2);
      }

      return var2;
   }

   private AttributeContributions addAttributeContributions(NumericOid var1, ObjectClassStructure var2, Collection<LdapStructureAttribute> var3) {
      AttributeContributions var4 = this.attributeContributions.get(var1);
      if (var4 == null) {
         var4 = new AttributeContributions();
         this.attributeContributions.put(var1, var4);
      }

      var4.add(var2);
      var4.setExtendedAttributes(var3.toArray(new LdapStructureAttribute[var3.size()]));
      return var4;
   }

   public LdapStructureServerCache getLdapCache() {
      return this.ldapCache;
   }

   public SourceTarget getType() {
      return this.type;
   }

   public LdapLocation getLocation() {
      return this.ldapLocation;
   }

   public NumericOid[] getAuxiliaryClassesFor(NumericOid var1) {
      if (var1 == null) {
         throw new NullPointerException("classOid");
      } else if (!this.classNodes.containsKey(var1)) {
         throw new IllegalArgumentException("Unknown class OID: " + var1);
      } else {
         return this.auxClassSelection.getAuxiliaryClassesFor(var1);
      }
   }

   public void setAuxiliaryClasses(final NumericOid var1, final ObjectClassStructure[] var2) {
      if (var1 == null) {
         throw new NullPointerException("classOid");
      }

      if (var2 == null) {
         throw new NullPointerException("auxStructs");
      }

      final ClassNameNode var3 = this.classNodes.get(var1);
      if (var3 != null) {
         EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               AttributeSelectionModel.this.auxClassSelection.setAuxiliaryClasses(var1, var2);
               AttributeSelectionModel.this.rebuildClassNodeAttributes(var1, var3);
            }
         });
      }
   }

   private void rebuildClassNodeAttributes(NumericOid var1, ClassNameNode var2) {
      Map var3 = var2.getAttributeNodeMap();
      var2.removeAllChildren();
      AttributeContributions var4 = this.attributeContributions.get(var1);

      for (LdapStructureAttribute var6 : var4.getEffectiveAttributes()) {
         AttributeSelectionNode var7 = AttributeSelectionNode.create(var6, this.type, var2);
         AttributeSelectionNode var8 = (AttributeSelectionNode)var3.get(var6.getOid());
         if (var8 != null) {
            var7.setSelected(var8.isSelected());
         }
      }

      this.fireTreeStructureChanged(this, new TreePath(new Object[]{this.getRoot(), var2}));
   }

   public LdapStructureAttribute[] getExtendedAttributes(NumericOid var1) {
      Preconditions.checkNotNull(var1, "classOid");
      AttributeContributions var2 = this.attributeContributions.get(var1);
      if (var2 == null) {
         throw new IllegalArgumentException("Unknown class OID: " + var1);
      } else {
         return var2.getExtendedAttributes();
      }
   }

   public KongaTreeNode[] getExtendedAttributeNodes(NumericOid var1) {
      ArrayList var2 = Lists.newArrayList();
      LdapStructureAttribute[] var3 = this.getExtendedAttributes(var1);
      if (var3.length > 0) {
         Set var4 = Nameables.getUniqueNames(var3);
         ClassNameNode var5 = this.classNodes.get(var1);

         for (KongaTreeNode var7 : var5.children()) {
            LdapStructureAttribute var8 = (LdapStructureAttribute)var7.getUserObject();
            if (var4.contains(var8.getName())) {
               var2.add(var7);
            }
         }
      }

      return (KongaTreeNode[]) var2.toArray(new KongaTreeNode[var2.size()]);
   }

   public void setExtendedAttributes(final NumericOid var1, final LdapStructureAttribute[] var2) {
      Preconditions.checkNotNull(var1, "classOid");
      final ClassNameNode var3 = this.classNodes.get(var1);
      UiUtils.runOnEventThread(new Runnable() {
         @Override
         public void run() {
            AttributeContributions var1x = AttributeSelectionModel.this.attributeContributions.get(var1);
            var1x.setExtendedAttributes(var2);
            AttributeSelectionModel.this.rebuildClassNodeAttributes(var1, var3);
         }
      });
   }

   public KongaTreeNode[] getRequiredAttributes() {
      ArrayList var1 = Lists.newArrayList();
      IterableEnumeration var2 = this.getRoot().depthFirstEnumeration();

      while (var2.hasMoreElements()) {
         KongaTreeNode var3 = (KongaTreeNode)var2.nextElement();
         if (var3 instanceof AttributeSelectionNode && ((AttributeSelectionNode)var3).isRequired()) {
            var1.add(var3);
         }
      }

      return (KongaTreeNode[]) var1.toArray(new KongaTreeNode[var1.size()]);
   }

   public LdapStructureEntry[] getDefinedEntries() {
      SelectedAttributes var1 = new SelectedAttributes(this);
      return var1.getDefinedEntries();
   }

   public LdapStructureEntryNode[] getDefinedEntryNodes() {
      LdapStructureEntry[] var1 = this.getDefinedEntries();
      ObjectClassStructure[] var2 = new ObjectClassStructure[var1.length];

      for (int var3 = 0; var3 < var1.length; var3++) {
         var2[var3] = this.selectedStructures.getStructure(var1[var3].getOid());
      }

      ObjectClassStructure[] var6 = this.auxClassSelection.getAuxiliaryClassStructures();
      LdapStructureTreeNodeFactory var4 = new LdapStructureTreeNodeFactory(var1, var2, var6);
      List var5 = var4.getEntryNodes();
      return (LdapStructureEntryNode[]) var5.toArray(new LdapStructureEntryNode[var5.size()]);
   }

   public Object getMemento() {
      return this.getRoot().getChildCount() == 0 ? new AttributeSelectionModel.EmptyMemento() : new Memento(this);
   }

   public void restore(Object var1) {
      if (!(var1 instanceof AttributeSelectionModel.EmptyMemento)) {
         if (!(var1 instanceof Memento)) {
            throw new IllegalArgumentException("Invalid memento");
         }

         ((Memento)var1).restore(this);
      }
   }

   AttributeContributions getAttributeContributions(NumericOid var1) {
      return this.attributeContributions.get(var1);
   }

   Iterable<NumericOid> getClassOids() {
      return this.classNodes.keySet();
   }

   private static class EmptyMemento {
      private EmptyMemento() {
      }
   }
}
