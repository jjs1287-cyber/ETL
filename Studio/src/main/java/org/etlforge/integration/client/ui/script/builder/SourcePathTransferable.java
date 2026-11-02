package org.etlforge.integration.client.ui.script.builder;
import java.awt.datatransfer.*;
public final class SourcePathTransferable extends StringSelection {
   public static final DataFlavor FLAVOR=new DataFlavor("application/x-etlforge-source-path;class=java.lang.String","ETLForge source path");
   private final String path;
   public SourcePathTransferable(String path){super(path);this.path=path;}
   @Override public DataFlavor[] getTransferDataFlavors(){return new DataFlavor[]{FLAVOR,DataFlavor.stringFlavor};}
   @Override public boolean isDataFlavorSupported(DataFlavor flavor){return FLAVOR.equals(flavor)||super.isDataFlavorSupported(flavor);}
   @Override public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException,java.io.IOException{if(FLAVOR.equals(flavor))return path;return super.getTransferData(flavor);}
}
