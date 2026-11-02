package org.etlforge.integration.client.ui.script.builder;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import org.etlforge.integration.client.ui.script.resources.ScriptResources;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.text.LineMarker;
import org.etlforge.ui.widget.text.LineNumberBar;

public class BreakLocationMarker implements LineMarker {
   private final int rowNumber;
   private final ExpressionArea expressionArea;
   private final Position position;
   private final Icon icon;

   public BreakLocationMarker(int var1, ExpressionArea var2) {
      this.rowNumber = var1;
      this.expressionArea = var2;
      this.position = this.createPosition(var1, var2);
      this.icon = ScriptResources.ICONS.getIcon("BreakLocationMarker");
   }

   @Override
   public Icon getIcon() {
      return this.icon;
   }

   @Override
   public Position getPosition() {
      return this.position;
   }

   @Override
   public boolean isAlive() {
      return this.rowNumber == DocumentUtils.getRowNumber(this.expressionArea.getDocument(), this.position.getOffset());
   }

   @Override
   public String getToolTip() {
      return null;
   }

   @Override
   public void handleEvent(LineNumberBar var1, MouseEvent var2) {
   }

   private Position createPosition(int var1, ExpressionArea var2) {
      try {
         int var3 = DocumentUtils.getRowStart(var2.getDocument(), var1);
         return var2.getDocument().createPosition(var3);
      } catch (BadLocationException var4) {
         return var2.getDocument().getEndPosition();
      }
   }

   @Override
   public String toString() {
      return "BreakLocationMarker: row " + this.rowNumber;
   }
}
