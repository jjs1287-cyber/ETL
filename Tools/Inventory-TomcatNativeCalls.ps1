$ErrorActionPreference='Stop'
$native='C:\ETL\Server\src\native\integration\konga'
$java='C:\ETL\Server\src\java\modules\etlforge-server-backend-ws'
$entries=@(
 @('JSON','JsonEngineWrapperCPP::startNewSession','startNewJsonSession','Session lifecycle'),
 @('JSON','JsonEngineWrapperCPP::jsonToXml','jsonToXml','Convert JSON source to intermediate XML'),
 @('JSON','JsonEngineWrapperCPP::xmlToJson','xmlToJson','Convert intermediate XML to JSON target'),
 @('JSON','JsonEngineWrapperCPP::closeSession','closeJsonSession','Session cleanup'),
 @('JDBC','JdbcEngineWrapper::Check','startNewSession','Lazy session creation'),
 @('JDBC','JdbcEngineWrapper::readSource','readSource','Read database source into engine files'),
 @('JDBC','JdbcEngineWrapper::writeTarget','writeTarget','Database target writes and insert/update behavior'),
 @('JDBC','JdbcEngineWrapper::dbLookup','dbLookup','Single-value database lookup'),
 @('JDBC','JdbcEngineWrapper::dbExecute','dbExecute','Execute SQL and marshal result rows'),
 @('JDBC','JdbcEngineWrapper::commitTransactions','commitTransactions','Commit JDBC session transactions'),
 @('JDBC','JdbcEngineWrapper::rollBackTransactions','rollbackTransactions','Rollback JDBC session transactions'),
 @('JDBC','JdbcEngineWrapper::~JdbcEngineWrapper','closeSession','Conditional destructor session cleanup'),
 @('Bulk','BulkLoadEngineWrapperCPP::startNewSession','startNewBulkLoadSession','Session creation'),
 @('Bulk','BulkLoadEngineWrapperCPP::runBulkLoad','runBulkLoad','Salesforce bulk upload, jobs, batch results and logs'),
 @('Bulk','BulkLoadEngineWrapperCPP::closeSession','closeBulkLoadSession','Session cleanup and cancellation path'),
 @('Salesforce','PrepareSalesforceOperationSession','loginToSalesforce','Resolve connector variables and obtain OAuth-backed Salesforce session'),
 @('JDBC','JdbcEngine::cachedDbLookup','cachedDbLookup','Generated RPC stub; no application caller found'),
 @('Groovy','ScriptEngine::runGroovyScript','runGroovyScript','Generated RPC stub; no application caller or current CMake source entry found')
)
$rows=foreach($entry in $entries){
 $group,$function,$operation,$purpose=$entry
 $status='Current build call path; usage depends on selected feature'
 switch($group){
  'JSON' {$file="$native\JavaBackend\JsonEngine\JsonEngineWrapperCPP.cpp"; $service='konga_json_engine'; $javaFile="$java\org\etlforge\integration\server\backend\webservice\json\Konga_json_engineSoapBindingImpl.java"; $priority='1 - Strong first candidate'}
  'JDBC' {$file="$native\JdbcUtils\JdbcEngineWrapper_AxisCPP.cpp"; $service='konga_jdbc_engine'; $javaFile="$java\org\etlforge\integration\server\backend\webservice\jdbc\Konga_jdbc_engineSoapBindingImpl.java"; $priority='2 - Prioritize frequently repeated lookups; driver-dependent'}
  'Bulk' {$file="$native\JavaBackend\BulkLoadEngine\BulkLoadEngineWrapperCPP.cpp"; $service='konga_bulkload_engine'; $javaFile="$java\com\etlforge\integration\server\implementation\webservice\bulkload\Konga_bulkload_engineSoapBindingImpl.java"; $priority='4 - More complex; external API time may dominate'}
  'Salesforce' {$file="$native\OperationEngine\SalesforceOperationSession.cpp"; $service='konga_salesforce_engine'; $javaFile="$java\org\etlforge\integration\server\backend\webservice\salesforce\Konga_salesforce_engineSoapBindingImpl.java"; $priority='3 - Feasible native OAuth/session implementation'}
  'Groovy' {$file="$native\JavaBackend\ScriptEngine\ScriptEngine.cpp"; $service='konga_script_engine'; $javaFile="$java\org\etlforge\integration\server\backend\webservice\scriptengine\Konga_script_engineSoapBindingImpl.java"; $priority='Do not port unless a supported use case is found'}
 }
 if($function -eq 'JdbcEngine::cachedDbLookup'){$file="$native\JavaBackend\JdbcEngine\JdbcEngine.cpp"; $status='Generated stub only; no higher-level C++ caller found';$priority='No current migration work justified'}
 if($group -eq 'Groovy'){$status='Generated stub only; not in current CMake sources'}
 $nativePattern=[regex]::Escape($function)+'\s*\('
 $source=Select-String -LiteralPath $file -Pattern $nativePattern | Select-Object -First 1
 $javaSource=Select-String -LiteralPath $javaFile -Pattern ('\b'+[regex]::Escape($operation)+'\s*\(') | Select-Object -First 1
 if(!$source -or !$javaSource){throw "Missing source evidence for $function"}
 $stubClass=switch($group){'JSON'{'JsonEngine'} 'JDBC'{'JdbcEngine'} 'Bulk'{'BulkLoadEngine'} 'Groovy'{'ScriptEngine'} 'Salesforce'{''}}
 if($stubClass){
  $stubFile="$native\JavaBackend\$stubClass\$stubClass.cpp"
  $stubFunction=$stubClass+'::'+$operation
  $stubLine=(Select-String -LiteralPath $stubFile -Pattern ([regex]::Escape($stubFunction)+'\s*\(') | Select-Object -First 1).LineNumber
 } else {$stubFile=$file;$stubFunction='post';$stubLine=(Select-String -LiteralPath $file -Pattern '^std::string post\(' | Select-Object -First 1).LineNumber}
 if(!$stubLine){throw "Missing generated/transport evidence for $function"}
 [pscustomobject]@{Group=$group;CppFunction=$function;SoapOperation=$operation;Endpoint="http://127.0.0.1:47112/axis/services/$service";Purpose=$purpose;Status=$status;MigrationPriority=$priority;CppFile=$file;CppLine=$source.LineNumber;TransportFunction=$stubFunction;TransportFile=$stubFile;TransportLine=$stubLine;JavaFile=$javaFile;JavaLine=$javaSource.LineNumber}
}
if($rows.Count -ne 18){throw 'Unexpected inventory size'}
$rows | Export-Csv C:\ETL\Reports\tomcat-native-call-inventory.csv -NoTypeInformation -Encoding UTF8
Write-Output 'Inventory saved: 16 current-build forwarding entry points and 2 generated/unused RPC methods.'
