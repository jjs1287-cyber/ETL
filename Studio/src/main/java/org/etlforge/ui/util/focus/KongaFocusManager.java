package org.etlforge.ui.util.focus;

import com.google.common.collect.Maps;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KongaFocusManager implements PropertyChangeListener {
   static final String PERMANENT_FOCUS_OWNER = "permanentFocusOwner";
   private final List<Component> listRoots = new ArrayList<>();
   private final Set<Component> transientComponents = new HashSet<>();
   private final Map<Component, FocusListener> focusRoots = Maps.newHashMap();
   private Component lastFocusRootOwner;

   public void register() {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner", this);
   }

   public void unregister() {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this);
   }

   public synchronized void addFocusRoot(Component var1, FocusListener var2) {
      if (var1 != null) {
         this.listRoots.add(var1);
         this.focusRoots.put(var1, var2);
      }
   }

   public synchronized void addTransientComponent(Component var1) {
      if (var1 != null) {
         this.transientComponents.add(var1);
      }
   }

   public synchronized void removeTransientComponent(Component var1) {
      if (var1 != null) {
         this.transientComponents.remove(var1);
      }
   }

   public synchronized void removeFocusRoot(Component var1) {
      this.listRoots.remove(var1);
      this.focusRoots.remove(var1);
   }

   @Override
   public void propertyChange(PropertyChangeEvent var1) {
      this.propertyChangeImpl(var1);
   }

   private void propertyChangeImpl(PropertyChangeEvent var1) {
      Object var2 = var1.getOldValue();
      Object var3 = var1.getNewValue();
      if (var2 != null || var3 != null) {
         Component var4 = this.lastFocusRootOwner;
         if (var3 instanceof Component) {
            FocusListener var5 = null;
            boolean var6 = false;

            Component var7;
            for (var7 = (Component)var3; var7 != null; var7 = var7.getParent()) {
               if (this.focusRoots.containsKey(var7)) {
                  if (var7 == this.lastFocusRootOwner) {
                     return;
                  }

                  var5 = this.focusRoots.get(var7);
                  this.lastFocusRootOwner = var7;
                  break;
               }

               if (this.transientComponents.contains(var7)) {
                  var5 = this.focusRoots.get(this.lastFocusRootOwner);
                  var6 = true;
                  break;
               }
            }

            if (!var6) {
               this.lastFocusRootOwner = var7;
            }

            FocusEvent var8 = null;

            for (FocusListener var10 : this.focusRoots.values()) {
               if (var10 == var5) {
                  var10.focusGained(new FocusEvent((Component)var3, 1004, false, (Component)var2));
               } else {
                  if (var8 == null && var4 != null) {
                     var8 = new FocusEvent(var4, 1005, false, (Component)var3);
                  }

                  var10.focusLost(var8);
               }
            }
         }
      }
   }
}
