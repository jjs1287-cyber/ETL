package org.etlforge.application.ui.window.editor;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.Action;
import javax.swing.KeyStroke;
import org.etlforge.ui.action.ActionUtils;
import org.etlforge.ui.util.PopupMenuRequestHandler;
import org.etlforge.ui.widget.popup.PopupMenuUtils;

public class DefaultEditorController implements EditorController {
   private final Map<Object, Action> actionMap = Maps.newHashMap();
   private PopupMenuRequestHandler popupRequestHandler;

   public final void setPopupMenuRequestHandler(PopupMenuRequestHandler var1) {
      this.popupRequestHandler = var1;
   }

   @Override
   public boolean processKeyStroke(Editor var1, KeyStroke var2) {
      Action var3 = this.getAction(var2);
      if (var3 != null && var3.isEnabled()) {
         ActionUtils.runLater(var3, var1, var2.toString());
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean processMouseEvent(Editor var1, MouseEvent var2) {
      switch (var2.getID()) {
         case 500:
            return this.handleMouseClicked(var1, var2);
         case 501:
            return this.handleMousePressed(var1, var2);
         case 502:
            return this.handleMouseReleased(var1, var2);
         case 503:
         default:
            return false;
         case 504:
            return this.handleMouseClicked(var1, var2);
         case 505:
            return this.handleMouseExited(var1, var2);
      }
   }

   protected boolean handleMouseEntered(Editor var1, MouseEvent var2) {
      return false;
   }

   protected boolean handleMouseExited(Editor var1, MouseEvent var2) {
      return false;
   }

   protected boolean handleMouseClicked(Editor var1, MouseEvent var2) {
      return false;
   }

   protected boolean handleMousePressed(Editor var1, MouseEvent var2) {
      return PopupMenuUtils.isPopupTrigger(var2) && this.popupRequestHandler != null
         ? this.popupRequestHandler.popupMenuRequested((Component)var2.getSource(), var2)
         : false;
   }

   protected boolean handleMouseReleased(Editor var1, MouseEvent var2) {
      return PopupMenuUtils.isPopupTrigger(var2) && this.popupRequestHandler != null
         ? this.popupRequestHandler.popupMenuRequested((Component)var2.getSource(), var2)
         : false;
   }

   public final void registerAction(Object var1, Action var2) {
      if (var2 != null) {
         this.actionMap.put(var1, var2);
      } else {
         this.actionMap.remove(var1);
      }
   }

   protected final Action getAction(Object var1) {
      return this.actionMap.get(var1);
   }

   protected final boolean triggerAction(Object var1, Object var2) {
      Action var3 = this.getAction(var1);
      if (var3 != null) {
         ActionUtils.runLater(var3, var2, var1.toString());
         return true;
      } else {
         return false;
      }
   }
}
