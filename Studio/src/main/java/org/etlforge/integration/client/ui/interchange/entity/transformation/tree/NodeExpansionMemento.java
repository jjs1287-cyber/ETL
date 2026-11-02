package org.etlforge.integration.client.ui.interchange.entity.transformation.tree;

import com.google.common.collect.Sets;
import java.util.HashSet;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.SerializationUtils;
import org.etlforge.util.persist.Persistor;

public final class NodeExpansionMemento {
   private static final String PERSISTOR = "MappingTreeExpansion";
   private static final String SOURCE = "Source";
   private static final String TARGET = "Target";
   private final TreeMapper treeMapper;
   private final HashSet<String> expandedSourceNodes;
   private final HashSet<String> expandedTargetNodes;

   public NodeExpansionMemento(TreeMapper var1) {
      this.treeMapper = var1;
      this.expandedSourceNodes = Sets.newHashSet();
      this.expandedTargetNodes = Sets.newHashSet();
      var1.m_sourceTreeComponent.m_root.addExpandedNodes(this.expandedSourceNodes);
      var1.m_targetTreeComponent.m_root.addExpandedNodes(this.expandedTargetNodes);
   }

   public NodeExpansionMemento(TreeMapper var1, Persistor var2) throws Exception {
      this.treeMapper = var1;
      Persistor var3 = var2.getFirstChild("MappingTreeExpansion");
      this.expandedSourceNodes = this.readExpansionFromPersistor(var3, "Source");
      this.expandedTargetNodes = this.readExpansionFromPersistor(var3, "Target");
   }

   public void restore() {
      this.treeMapper.m_sourceTreeComponent.m_root.restoreExpandedNodes(this.expandedSourceNodes, 0);
      this.treeMapper.m_targetTreeComponent.m_root.restoreExpandedNodes(this.expandedTargetNodes, 0);
      this.treeMapper.m_sourceTreeComponent.refresh();
      this.treeMapper.m_targetTreeComponent.refresh();
   }

   public void persist(Persistor var1) {
      var1.removeChildren("MappingTreeExpansion");
      Persistor var2 = var1.createChild("MappingTreeExpansion");
      var2.putText("Source", this.serializeToBase64String(this.expandedSourceNodes));
      var2.putText("Target", this.serializeToBase64String(this.expandedTargetNodes));
   }

   private String serializeToBase64String(HashSet<String> var1) {
      byte[] var2 = SerializationUtils.serialize(var1);
      return new String(Base64.encodeBase64(var2));
   }

   private HashSet<String> deserializeFromBase64String(String var1) throws Exception {
      byte[] var2 = Base64.decodeBase64(var1.getBytes());
      return (HashSet<String>)SerializationUtils.deserialize(var2);
   }

   private HashSet<String> readExpansionFromPersistor(Persistor var1, String var2) throws Exception {
      String var3 = var1 != null ? var1.getText(var2) : null;
      return var3 != null ? this.deserializeFromBase64String(var3) : new HashSet<>();
   }
}
