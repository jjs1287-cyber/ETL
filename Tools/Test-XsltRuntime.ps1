$ErrorActionPreference='Stop'
$root='C:\ETL\Samples\XSLT\ExpandEmbeddedXml'
$classes='C:\ETL\Server\build-probe\xslt\classes'
New-Item -ItemType Directory -Force $classes | Out-Null
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -encoding UTF-8 -d $classes C:\ETL\Server\src\java\modules\etlforge-xslt\org\etlforge\integration\xslt\EtlForgeXsltRuntime.java
if($LASTEXITCODE -ne 0){throw 'XSLT compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp "$classes;C:\ETL\Dependencies\java\xslt\*" org.etlforge.integration.xslt.EtlForgeXsltRuntime "$root\transform.xsl" "$root\input.xml" "$root\actual.xml"
if($LASTEXITCODE -ne 0){throw 'XSLT execution failed'}
$expected=[xml][IO.File]::ReadAllText("$root\input.xml")
foreach($node in $expected.SelectNodes('/outbox/entry/xml')){
    $inner=[xml]$node.InnerText
    $node.InnerText=''
    [void]$node.AppendChild($expected.ImportNode($inner.DocumentElement,$true))
}
$actual=[xml][IO.File]::ReadAllText("$root\actual.xml")
function Normalize-Xml([xml]$doc){
    $nav=$doc.CreateNavigator()
    $it=$nav.Select('//text()[normalize-space(.)=""]')
    $remove=@(); while($it.MoveNext()){$remove+=,$it.Current.UnderlyingObject}
    foreach($n in $remove){[void]$n.ParentNode.RemoveChild($n)}
    $doc.DocumentElement.OuterXml
}
if((Normalize-Xml $expected) -cne (Normalize-Xml $actual)){throw 'Output differs from expected expanded XML'}
'PASS: XSLT 3.0 execution, embedded XML expansion, surrounding document preservation.'
& C:\ETL\Tools\jdk8u504-b01\bin\javac.exe -cp $classes -d $classes C:\ETL\Server\tests\java\XsltRuntimeContractTest.java
if($LASTEXITCODE){throw 'XSLT contract test compilation failed'}
& C:\ETL\Tools\jdk8u504-b01\bin\java.exe -cp "$classes;C:\ETL\Dependencies\java\xslt\*" XsltRuntimeContractTest
if($LASTEXITCODE){throw 'XSLT contract tests failed'}
