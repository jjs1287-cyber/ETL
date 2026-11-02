import org.etlforge.integration.xslt.EtlForgeXsltRuntime;
public class XsltRuntimeContractTest {
 private static final String HEAD="<xsl:stylesheet version='3.0' xmlns:xsl='http://www.w3.org/1999/XSL/Transform'>";
 private static void require(boolean condition,String message){if(!condition)throw new AssertionError(message);}
 private static void rejected(String style,String input)throws Exception{
  try{EtlForgeXsltRuntime.executeStylesheet(style,input);throw new AssertionError("Unsafe or invalid transform was accepted");}
  catch(javax.xml.transform.TransformerException expected){}
 }
 public static void main(String[] args)throws Exception{
  String style=HEAD+"<xsl:template name='start_here'><merged><xsl:copy-of select=\"doc('param1')/* | doc('param2')/*\"/></merged></xsl:template></xsl:stylesheet>";
  String[] out=EtlForgeXsltRuntime.executeStylesheet(style,"<customers/>","<orders/>");
  require(out.length==1 && out[0].contains("<customers/>") && out[0].contains("<orders/>"),"Multiple input merge failed");
  rejected(HEAD+"<xsl:template match='/'><xsl:sequence select=\"doc('file:///C:/Windows/win.ini')\"/></xsl:template></xsl:stylesheet>","<root/>");
  rejected(HEAD+"<xsl:template match='/'><xsl:value-of select=\"unparsed-text('file:///C:/Windows/win.ini')\"/></xsl:template></xsl:stylesheet>","<root/>");
  rejected(HEAD+"<xsl:template match='/'><xsl:copy-of select='.'/></xsl:template></xsl:stylesheet>","<broken>");
  rejected(HEAD+"<xsl:include href='another-project-asset.xsl'/></xsl:stylesheet>","<root/>");
  rejected(HEAD+"<xsl:import href='file:///C:/Windows/win.ini'/></xsl:stylesheet>","<root/>");
  rejected(HEAD+"<xsl:template match='/'><xsl:result-document href='urn:etlforge:result'><result/></xsl:result-document></xsl:template></xsl:stylesheet>","<root/>");
  System.out.println("PASS: multiple XML inputs, array output, filesystem access denied, malformed XML rejected");
 }
}
