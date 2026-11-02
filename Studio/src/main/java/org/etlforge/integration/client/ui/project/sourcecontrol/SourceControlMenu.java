package org.etlforge.integration.client.ui.project.sourcecontrol;

import java.util.*;
import javax.swing.*;
import org.etlforge.application.ui.action.ApplicationAction;
import org.etlforge.application.ui.menu.*;
public final class SourceControlMenu implements Menu {
   private final java.util.List<ApplicationAction> actions;
   public SourceControlMenu(java.util.List<ApplicationAction> actions){this.actions=actions;}
   public String getID(){return "menu.SourceControl";}
   public boolean isSupported(){return true;}
   public void addExtension(MenuExtensionPoint p,MenuContribution c){}
   public void removeExtension(MenuExtensionPoint p,String id){}
   public void removeAllExtensions(){}
   public JMenu createJMenu(MenuDictionary dictionary){JMenu menu=new JMenu("Source Control");populate(menu);return menu;}
   public void populate(JMenu menu){menu.removeAll();for(ApplicationAction action:actions)menu.add(new JMenuItem(action));}
   public void populate(JPopupMenu menu){menu.removeAll();for(ApplicationAction action:actions)menu.add(new JMenuItem(action));}
}
