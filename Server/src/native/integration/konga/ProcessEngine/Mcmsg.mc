;#ifndef _MCMSG_
;#define _MCMSG_
;
MessageIdTypedef=DWORD
;
SeverityNames=(
	Note=0x0:APP_SEVERITY_NOTE
	Informational=0x1:APP_SEVERITY_INFORMATIONAL
	Warning=0x2:APP_SEVERITY_WARNING
	Error=0x3:APP_SEVERITY_ERROR
)
;
;

MessageId=0x0
Severity=Note
SymbolicName=NTSRV_NOTE
Language=English
%1
.

MessageId=0x1 
Severity=Informational
SymbolicName=NTSRV_INFO
Language=English
%1
.

MessageId=0x2
Severity=Warning
SymbolicName=NTSRV_WARN
Language=English
%1
.

MessageId=0x3 
Severity=Error 
SymbolicName=NTSRV_ERROR
Language=English
%1
.

MessageId=0x8 
Severity=Informational 
SymbolicName=NTSRV_AUDIT_SUCCESS
Language=English
%1
.

MessageId=0x4 
Severity=Informational
SymbolicName=CR_SERVER_STARTING
Language=English
%1
.

MessageId=0x5 
Severity=Informational
SymbolicName=CR_SERVER_STOPPING
Language=English
%1
.

MessageId=0x6
Severity=Informational
SymbolicName=CR_SERVER_SUCCESSFULLY_STOPPED
Language=English
%1
.

;#endif // _MCMSG_
;