package org.etlforge.ui.widget.text.marker;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import org.apache.commons.lang.StringUtils;
import org.etlforge.ui.resources.UiResourceBundle;
import org.etlforge.ui.text.DocumentUtils;
import org.etlforge.ui.widget.menu.KongaMenu;

public final class TextMarker {
   private final JTextComponent textComponent;
   private final List<? extends MarkerColor> colors;
   private final Map<String, MarkerColor> markers = Maps.newHashMap();
   private final Multimap<String, Object> highlights = HashMultimap.create();
   private List<Action> markActions;
   private Action clearAction;
   private Action clearAllAction;

   public TextMarker(JTextComponent var1) {
      this(var1, getDefaultColors());
   }

   public TextMarker(JTextComponent var1, List<? extends MarkerColor> var2) {
      this.textComponent = var1;
      this.colors = Lists.newArrayList(var2);
      this.createActions();
      var1.addCaretListener(new TextMarker.ActionStateUpdater());
   }

   public void clearAllMarkers() {
      this.markers.clear();
      this.highlights.clear();
      Highlighter var1 = this.textComponent.getHighlighter();
      var1.removeAllHighlights();
   }

   public void clearMarker() {
      String var1 = this.textComponent.getSelectedText();
      if (StringUtils.isNotEmpty(var1)) {
         this.clearMarker(var1);
      }
   }

   public void clearMarker(String var1) {
      this.markers.remove(var1);
      Collection var2 = this.highlights.removeAll(var1);
      Highlighter var3 = this.textComponent.getHighlighter();

      for (Object var5 : var2) {
         var3.removeHighlight(var5);
      }
   }

   public void addMarker(MarkerColor var1) {
      String var2 = this.textComponent.getSelectedText();
      if (StringUtils.isNotEmpty(var2)) {
         this.addMarker(var2, var1);
      }
   }

   public void addMarker(String var1, MarkerColor var2) {
      if (!StringUtils.isEmpty(var1)) {
         this.clearMarker(var1);
         this.markers.put(var1, var2);
         this.highlightMark(var1, var2);
      }
   }

   private void highlightMark(String var1, MarkerColor var2) {
      Highlighter var3 = this.textComponent.getHighlighter();
      Document var4 = this.textComponent.getDocument();
      String var5 = DocumentUtils.getText(var4, 0, var4.getLength());

      try {
         int var6 = 0;

         while (var6 < var4.getLength()) {
            int var7 = var5.indexOf(var1, var6);
            if (var7 == -1) {
               break;
            }

            DefaultHighlightPainter var8 = new DefaultHighlightPainter(var2.color);
            int var9 = var7 + var1.length();
            Object var10 = var3.addHighlight(var7, var9, var8);
            this.highlights.put(var1, var10);
            var6 = var9;
         }
      } catch (BadLocationException var11) {
         var11.printStackTrace();
      }
   }

   public JMenu getMarkerMenu() {
      KongaMenu var1 = new KongaMenu(UiResourceBundle.getString("TextMarker.Menu"));
      var1.addActions(this.markActions);
      var1.addSeparator();
      var1.addActions(this.clearAction, this.clearAllAction);
      return var1;
   }

   public static List<MarkerColor> getDefaultColors() {
      return Arrays.asList(
         new MarkerColor(UiResourceBundle.getString("Color.Yellow"), new Color(255, 255, 153)),
         new MarkerColor(UiResourceBundle.getString("Color.Blue"), new Color(153, 204, 255)),
         new MarkerColor(UiResourceBundle.getString("Color.Red"), new Color(255, 153, 153)),
         new MarkerColor(UiResourceBundle.getString("Color.Green"), new Color(153, 255, 153))
      );
   }

   private void createActions() {
      this.clearAction = new TextMarker.ClearMarkerAction(this);
      this.clearAllAction = new TextMarker.ClearAllMarkersAction(this);
      this.markActions = new ArrayList<>();

      for (MarkerColor var2 : this.colors) {
         this.markActions.add(new TextMarker.MarkAction(this, var2));
      }
   }

   private class ActionStateUpdater implements CaretListener {
      private ActionStateUpdater() {
      }

      @Override
      public void caretUpdate(CaretEvent var1) {
         boolean var2 = var1.getDot() != var1.getMark();
         TextMarker.this.clearAction.setEnabled(var2);

         for (Action var4 : TextMarker.this.markActions) {
            var4.setEnabled(var2);
         }
      }
   }

   private static class ClearAllMarkersAction extends AbstractAction {
      private final TextMarker marker;

      public ClearAllMarkersAction(TextMarker var1) {
         super(UiResourceBundle.getString("ClearAll"));
         this.marker = var1;
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.marker.clearAllMarkers();
      }
   }

   private static class ClearMarkerAction extends AbstractAction {
      private final TextMarker marker;

      public ClearMarkerAction(TextMarker var1) {
         super(UiResourceBundle.getString("Clear"));
         this.marker = var1;
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.marker.clearMarker();
      }
   }

   private static class MarkAction extends AbstractAction {
      private final TextMarker marker;
      private final MarkerColor color;

      public MarkAction(TextMarker var1, MarkerColor var2) {
         super(var2.name);
         this.marker = var1;
         this.color = var2;
         this.setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         this.marker.addMarker(this.color);
      }
   }
}
