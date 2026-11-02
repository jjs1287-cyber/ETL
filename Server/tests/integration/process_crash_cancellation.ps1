$ErrorActionPreference='Stop'
$source=[IO.File]::ReadAllText('C:\ETL\Server\src\native\integration\konga\ProcessEngine\Engine\EngineCore.cpp',[Text.Encoding]::UTF8)
$start=$source.IndexOf('void EngineCore::CheckForTasksToReRun()')
$end=$source.IndexOf('void EngineCore::MoveExistingMessagesToErrorQueue()',$start)
if($start -lt 0 -or $end -le $start){throw 'Could not isolate startup recovery code'}
$recovery=$source.Substring($start,$end-$start)
if($recovery -match '\bReRunTask\s*\('){throw 'Startup recovery still automatically reruns interrupted work'}
if($recovery -notmatch 'CancelInterruptedOperation\s*\(pmsg\)'){throw 'Startup recovery does not cancel the interrupted operation'}
if($recovery -notmatch 'MoveMessageToErrorQueue\s*\('){throw 'Startup recovery does not remove interrupted work from the runnable queue'}
if($source -notmatch 'OperationLogStatus_Cancelled'){throw 'Canceled operation-log status is not written'}
'PASS interrupted operations are canceled, removed from the runnable queue, and never automatically replayed'
