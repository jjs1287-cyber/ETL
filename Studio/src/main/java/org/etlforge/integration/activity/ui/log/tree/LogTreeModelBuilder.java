package org.etlforge.integration.activity.ui.log.tree;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.etlforge.integration.activity.client.log.LogEntryComparator;
import org.etlforge.integration.activity.client.log.OperationLogEntry;
import org.etlforge.integration.activity.ui.log.OperationLogTableColumn;

final class LogTreeModelBuilder {
   private final Map<Long, OperationNode> nodes = Maps.newHashMap();
   private final RootNode root = new RootNode();
   private final OperationLogTableColumn[] columns;

   public LogTreeModelBuilder(OperationLogTableColumn[] var1) {
      this.columns = var1;
   }

   public OperationLogTreeModel build(List<OperationLogEntry> var1) {
      Collections.sort(var1, LogEntryComparator.INSTANCE_ID);
      this.createNodes(var1);
      this.addTopLevelNodesToRoot();
      return new OperationLogTreeModel(this.root, this.columns);
   }

   private void createNodes(List<OperationLogEntry> var1) {
      for (OperationLogEntry var3 : var1) {
         OperationNode var4 = new OperationNode(var3, this.columns);
         this.addNodeToParent(var3, var4);
         this.nodes.put(var3.getOpInstanceId(), var4);
      }
   }

   private void addNodeToParent(OperationLogEntry var1, OperationNode var2) {
      Long var3 = var1.getParentOpInstanceId();
      if (var3 != null) {
         OperationNode var4 = this.nodes.get(var3);
         if (var4 != null) {
            var4.add(var2);
         }
      }
   }

   private void addTopLevelNodesToRoot() {
      for (OperationNode var3 : this.getTopLevelNodes()) {
         this.root.add(var3);
      }
   }

   private List<OperationNode> getTopLevelNodes() {
      Predicate var1 = new Predicate<OperationNode>() {
         public boolean apply(OperationNode var1) {
            return var1.getParent() == null;
         }
      };
      return FluentIterable.from(this.nodes.values()).filter(var1).toImmutableList().reverse();
   }
}
