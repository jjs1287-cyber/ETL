package org.etlforge.integration.client.ui.script.text;

import javax.swing.Icon;
import javax.swing.text.Document;
import org.etlforge.integration.data.entity.IntegrationEntity;
import org.etlforge.integration.data.script.ScriptConstants;
import org.etlforge.integration.uidelegate.entity.IconStore;
import org.etlforge.integration.uidelegate.entity.tooltip.ToolTipStringFactory;
import org.etlforge.ui.text.contentassist.CompletionToken;
import org.etlforge.ui.text.contentassist.DefaultCompletionToken;
import org.etlforge.util.Interval;

final class ProjectItemCompletionToken implements CompletionToken {
   private final IntegrationEntity item;
   private final DefaultCompletionToken impl;

   public ProjectItemCompletionToken(IntegrationEntity var1, int var2) {
      this.item = var1;
      this.impl = new DefaultCompletionToken(ScriptConstants.getProjectItemTagString(var1), var2, var2);
      this.impl.setIcon(IconStore.defaultIcon(var1));
   }

   @Override
   public void complete(Document var1) {
      this.impl.complete(var1);
   }

   @Override
   public String getDisplayValue() {
      return this.item.getName();
   }

   @Override
   public Icon getIcon() {
      return this.impl.getIcon();
   }

   @Override
   public String getInfo() {
      return ToolTipStringFactory.getToolTip(this.item);
   }

   @Override
   public Interval getInterval(Document var1) {
      return this.impl.getInterval(var1);
   }
}
