package org.etlforge.integration.client.ui.script.text;

import com.google.common.collect.Maps;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;
import org.apache.commons.lang.StringUtils;
import org.etlforge.integration.client.ui.script.partition.ScriptPartitioner;
import org.etlforge.system.Disposable;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.text.partition.PartitionWordHoverer;
import org.etlforge.ui.text.partition.TextPartitioner;
import org.etlforge.ui.text.partition.Token;

public final class ScriptMouseHoverer extends PartitionWordHoverer {
   private static final int HOVER_DELAY_IN_MILLIS = 500;
   private final ScriptPartitioner partitioner;
   private Disposable popup;
   private final Map<Token, TokenHovererDisplayer> displayers;

   public ScriptMouseHoverer(JTextComponent var1, ScriptPartitioner var2) {
      super(var1, 500);
      this.partitioner = var2;
      this.displayers = Maps.newHashMap();
      this.setResetOnMouseClick(true);
   }

   public void setDisplayerForToken(Token var1, TokenHovererDisplayer var2) {
      this.displayers.put(var1, var2);
   }

   @Override
   protected TextPartitioner.Partition getPartitionAt(MouseEvent var1) {
      Point var2 = var1.getPoint();
      int var3 = this.getTarget().viewToModel(var2);
      return this.partitioner.getPartitionAt(var3);
   }

   @Override
   protected void handleDocumentChange(DocumentEvent var1) {
      this.removePopup();
      super.handleDocumentChange(var1);
   }

   @Override
   protected void mouseMoved(MouseEvent var1) {
      this.removePopup();
      super.mouseMoved(var1);
   }

   @Override
   protected void handlePartition(TextPartitioner.Partition var1, MouseEvent var2) {
      Token var3 = var1.token;
      TokenHovererDisplayer var4 = this.displayers.get(var3);
      if (var4 != null) {
         String var5 = DocumentUtils.getText(this.getTarget().getDocument(), var1);
         if (StringUtils.isNotEmpty(var5)) {
            this.popup = var4.display(this.getTarget(), var5, var1, var2);
         }
      }
   }

   private void removePopup() {
      if (this.popup != null) {
         this.popup.dispose();
         this.popup = null;
      }
   }
}
