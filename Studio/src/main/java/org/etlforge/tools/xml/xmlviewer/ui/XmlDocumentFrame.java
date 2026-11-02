package org.etlforge.tools.xml.xmlviewer.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.etlforge.tools.xml.xmlviewer.XmlFile;
import org.etlforge.ui.util.find.SearchAndReplaceTextComponentTarget;
import org.etlforge.ui.util.find.SearchTarget;
import org.etlforge.ui.widget.text.KongaTextPane;
import org.etlforge.ui.widget.tree.TreeSearchTarget;
import org.etlforge.ui.xml.TreeController;
import org.etlforge.ui.xml.XmlTextViewer;
import org.etlforge.ui.xml.XmlTree;

public final class XmlDocumentFrame extends JInternalFrame {
   public static final int TEXT_MODE = 0;
   public static final int TREE_MODE = 1;
   private static int currentDefaultMode = 1;
   private static Set<Integer> frameInstanceNumbers = new HashSet<>();
   private static final int X_OFFSET = 30;
   private static final int Y_OFFSET = 30;
   private final int instanceNumber;
   private final XmlFile xmlFile;
   private JTextPane textArea;
   private XmlTree tree;
   private TreeController treeController;
   private int currentMode;
   private CopyAction copyAction;
   private JPanel panel;

   public int getCurrentDisplayMode() {
      return this.currentMode;
   }

   public String getDisplayedText() {
      return this.textArea == null ? "" : this.textArea.getText();
   }

   public void setTextSelection(int var1, int var2) {
      if (this.textArea != null) {
         this.textArea.setSelectionStart(var1);
         this.textArea.setSelectionEnd(var2);
         this.textArea.getCaret().setSelectionVisible(true);
      }
   }

   public int getCurrentTextCaretPosition() {
      return this.textArea == null ? -1 : this.textArea.getCaretPosition();
   }

   public void setTextCaretPosition(int var1) {
      if (this.textArea != null) {
         this.textArea.setCaretPosition(Math.max(0, var1));
      }
   }

   public String getCurrentTextSelection() {
      return this.textArea == null ? "" : this.textArea.getSelectedText();
   }

   public int getCurrentTextSelectionStart() {
      return this.textArea == null ? -1 : this.textArea.getSelectionStart();
   }

   public int getCurrentTextSelectionEnd() {
      return this.textArea == null ? -1 : this.textArea.getSelectionEnd();
   }

   public TreeController getTreeController() {
      return this.treeController;
   }

   public SearchTarget getSearchTarget() {
      return this.getCurrentDisplayMode() == 0 ? new SearchAndReplaceTextComponentTarget(this.textArea) : new TreeSearchTarget(this.tree);
   }

   public XmlDocumentFrame(final MainWindow var1, XmlFile var2) {
      super(var2.getName(), true, true, true, true);
      this.setDefaultCloseOperation(2);
      this.instanceNumber = getInstanceNumber();
      this.addInternalFrameListener(new InternalFrameAdapter() {
         @Override
         public void internalFrameClosed(InternalFrameEvent var1x) {
            XmlDocumentFrame.frameInstanceNumbers.remove(XmlDocumentFrame.this.instanceNumber);
            var1.onInternalFrameClosed(XmlDocumentFrame.this);
         }
      });
      this.xmlFile = var2;
      this.initUi();
   }

   private static int getInstanceNumber() {
      int var0 = 1;

      while (true) {
         Integer var1 = var0;
         if (!frameInstanceNumbers.contains(var1)) {
            frameInstanceNumbers.add(var1);
            return var1;
         }

         var0++;
      }
   }

   private void initUi() {
      this.setFrameIcon(null);
      Container var1 = this.getContentPane();
      var1.setLayout(new BorderLayout());
      this.treeController = new TreeController();
      var1.add(new XmlDocumentFrame.ModePanel(), "North");
      this.panel = new JPanel(new BorderLayout());
      this.currentMode = currentDefaultMode;
      if (currentDefaultMode == 0) {
         this.displayText();
      } else {
         this.displayTree();
      }

      var1.add(this.panel, "Center");
      this.mapKeyStrokes();
      this.pack();
      this.setSize(600, 600);
      this.setLocation(30 * this.instanceNumber, 30 * this.instanceNumber);
   }

   private void mapKeyStrokes() {
      InputMap var1 = this.getInputMap(1);
      var1.put(KeyStroke.getKeyStroke(49, 128), "expand");
      var1.put(KeyStroke.getKeyStroke(50, 128), "collapse");
      ActionMap var2 = this.getActionMap();
      var2.put("expand", this.treeController.getExpandAction());
      var2.put("collapse", this.treeController.getCollapseAction());
   }

   private void displayTree() {
      if (this.tree == null) {
         this.tree = XmlTree.createTree(this.xmlFile.getRoot());
      }

      this.treeController.setTree(this.tree);
      this.panel.removeAll();
      this.panel.add(new JScrollPane(this.tree), "Center");
      this.revalidate();
   }

   private void displayText() {
      (new Thread() {
         @Override
         public void run() {
            if (XmlDocumentFrame.this.textArea == null) {
               XmlDocumentFrame.this.textArea = new KongaTextPane();
               XmlTextViewer var1 = new XmlTextViewer(XmlDocumentFrame.this.textArea);
               var1.view(XmlDocumentFrame.this.xmlFile.getFileObject());
               XmlDocumentFrame.this.textArea.setEditable(false);
               XmlDocumentFrame.this.copyAction = new CopyAction(XmlDocumentFrame.this.textArea);
            }

            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  XmlDocumentFrame.this.treeController.setTree(null);
                  XmlDocumentFrame.this.panel.removeAll();
                  XmlDocumentFrame.this.panel.add(new JScrollPane(XmlDocumentFrame.this.textArea), "Center");
                  XmlDocumentFrame.this.copyAction.setEnabled(true);
                  XmlDocumentFrame.this.revalidate();
               }
            });
         }
      }).start();
   }

   public void saveFile() throws IOException {
      this.saveFileAs(this.xmlFile.getFileObject());
   }

   public void saveFileAs(File var1) throws IOException {
      String var2 = this.textArea == null ? this.xmlFile.toFormattedText() : this.textArea.getText();
      this.xmlFile.saveFileAs(var2, var1);
   }

   private final class ModePanel extends JPanel implements ActionListener {
      private final JRadioButton m_btnTree;
      private final JRadioButton m_btnText;

      public ModePanel() {
         this.setLayout(new BoxLayout(this, 0));
         this.m_btnTree = new JRadioButton("Tree", XmlDocumentFrame.currentDefaultMode == 1);
         this.m_btnText = new JRadioButton("Text", XmlDocumentFrame.currentDefaultMode == 0);
         this.m_btnTree.addActionListener(this);
         this.m_btnText.addActionListener(this);
         ButtonGroup var2 = new ButtonGroup();
         var2.add(this.m_btnTree);
         var2.add(this.m_btnText);
         this.add(this.m_btnTree);
         this.add(this.m_btnText);
         this.add(Box.createHorizontalGlue());
         this.add(XmlDocumentFrame.this.treeController.getControlPanel());
      }

      @Override
      public void actionPerformed(ActionEvent var1) {
         if (var1.getSource() == this.m_btnTree) {
            if (this.m_btnTree.isSelected() && XmlDocumentFrame.this.currentMode != 1) {
               XmlDocumentFrame.this.currentMode = 1;
               XmlDocumentFrame.this.displayTree();
               if (XmlDocumentFrame.this.copyAction != null) {
                  XmlDocumentFrame.this.copyAction.setEnabled(false);
               }

               XmlDocumentFrame.currentDefaultMode = 1;
            }
         } else if (var1.getSource() == this.m_btnText && this.m_btnText.isSelected() && XmlDocumentFrame.this.currentMode != 0) {
            XmlDocumentFrame.this.currentMode = 0;
            XmlDocumentFrame.this.displayText();
            XmlDocumentFrame.currentDefaultMode = 0;
         }
      }
   }
}
