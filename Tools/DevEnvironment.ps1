$script:ETLRoot = Split-Path -Parent $PSScriptRoot
if (!$env:JAVA_HOME) { $env:JAVA_HOME = Join-Path $script:ETLRoot 'Tools\jdk8u504-b01' }
if (!$env:ANT_HOME) { $env:ANT_HOME = Join-Path $script:ETLRoot 'Tools\apache-ant-1.10.15' }
if (!$env:ETLFORGE_PYTHON) {
    $candidates = @(Get-ChildItem 'C:\Tools\Python\cpython-3.12.*-windows-x86_64-none\python.exe' -ErrorAction SilentlyContinue | Sort-Object FullName -Descending | ForEach-Object FullName)
    $candidates += 'C:\Tools\aider-env\Scripts\python.exe'
    $command = Get-Command python.exe -ErrorAction SilentlyContinue
    if ($command) { $candidates += $command.Source }
    $env:ETLFORGE_PYTHON = $candidates | Where-Object { Test-Path -LiteralPath $_ -PathType Leaf } | Select-Object -First 1
}
if (!$env:ETLFORGE_PYTHON -or !(Test-Path -LiteralPath $env:ETLFORGE_PYTHON -PathType Leaf)) {
    throw 'Set ETLFORGE_PYTHON to an installed Python 3 interpreter.'
}
$env:PYTHONUTF8 = '1'
