package org.etlforge.ui.layout;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.border.Border;
import org.etlforge.ui.UiCanvas;
import org.etlforge.ui.UiProvider;
import org.etlforge.ui.widget.panel.KongaPanel;

public class CardPanel implements UiProvider {
   private final JPanel panel;
   private final CardLayout layout;
   private final Map<String, Component> names;
   private String currentCard;

   public CardPanel() {
      this(new KongaPanel());
   }

   public CardPanel(JPanel var1) {
      this.panel = var1;
      var1.putClientProperty(this.getClass().getSimpleName(), Boolean.TRUE);
      this.layout = new CardLayout();
      var1.setLayout(this.layout);
      this.names = Maps.newHashMap();
   }

   public CardPanel add(Component var1, String var2) {
      this.panel.add(var1, var2);
      this.names.put(var2, var1);
      if (this.currentCard == null) {
         this.currentCard = var2;
      }

      return this;
   }

   public CardPanel add(UiProvider var1, final String var2) {
      var1.decorate(new UiCanvas() {
         @Override
         public void display(Component var1) {
            CardPanel.this.add(var1, var2);
         }
      });
      return this;
   }

   public Collection<Component> removeAll() {
      HashSet var1 = Sets.newHashSet(this.names.values());

      for (String var4 : Sets.newHashSet(this.names.keySet())) {
         this.remove(var4);
      }

      return var1;
   }

   public void removeAllBut(String var1) {
      for (String var4 : Sets.newHashSet(this.names.keySet())) {
         if (!var4.equals(var1)) {
            this.remove(var4);
         }
      }
   }

   public void remove(String var1) {
      Component var2 = this.names.remove(var1);
      if (var2 != null) {
         this.layout.next(this.panel);
         this.panel.remove(var2);
         this.layout.removeLayoutComponent(var2);
         this.layout.previous(this.panel);
         this.findNewCurrentCard(var1);
         var2.setVisible(true);
         this.panel.revalidate();
         this.panel.repaint();
      }
   }

   private void findNewCurrentCard(String var1) {
      if (var1.equals(this.currentCard)) {
         for (Entry var3 : this.names.entrySet()) {
            if (((Component)var3.getValue()).isVisible()) {
               this.currentCard = (String)var3.getKey();
               return;
            }
         }
      }

      this.currentCard = null;
   }

   public void show(String var1) {
      this.layout.show(this.panel, var1);
      this.currentCard = var1;
   }

   public boolean contains(String var1) {
      return this.names.containsKey(var1);
   }

   public String getCurrentCard() {
      return this.currentCard;
   }

   @Override
   public void decorate(UiCanvas var1) {
      var1.display(this.panel);
   }

   @Override
   public void requestFocus() {
      this.panel.requestFocusInWindow();
   }

   public CardPanel setBorder(Border var1) {
      this.panel.setBorder(var1);
      return this;
   }

   public CardPanel setOpaque(boolean var1) {
      this.panel.setOpaque(var1);
      return this;
   }

   public CardPanel setBackground(Color var1) {
      this.panel.setBackground(var1);
      return this;
   }

   public JPanel container() {
      return this.panel;
   }
}
