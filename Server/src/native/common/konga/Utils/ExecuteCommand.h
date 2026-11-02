/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */

#ifndef EXECUTE_COMMAND_H
#define EXECUTE_COMMAND_H

#ifdef WIN32
	#include <windows.h>
#endif

#include <string>

#ifdef notyetWIN32
	#include "CrInetBase.h"

	class ExecuteCommand : public CrInetBase                                                                                                                           
#else
	#include <stdio.h>
	class ExecuteCommand
#endif
{
public:
	ExecuteCommand() : m_ShowOutput(false), m_lExitCode(0), m_dwProcessId(0), m_hStdIn(0), m_hStdOut(0), m_hStdErr(0),
		m_hProcess(0) {}
	virtual ~ExecuteCommand() {}

	long Exec(std::string stCmd, long nSecondsTimeOut);
	long ExecString(std::string stCmd, long nSecondsTimeOut, std::string &lastOutput);
	long HandleError(long err, bool append          );

	bool ShowOutput() const { return m_ShowOutput; }
	void ShowOutput(bool bShow) { m_ShowOutput = bShow; }
	long KillChildProcesses();

protected:
	long WaitForProcess(int nSecondsTimeOut, bool bTerminateIfTimedOut=true);
	long WaitForProcessInputIdle(int nSecondsTimeOut, bool bTerminateIfTimedOut=true);
	virtual void InteractWithProcess() {}

	std::string ReadFromStdout();
	std::string ReadFromStderr();
	void WriteToStdin(std::string);

	std::string m_CMD;

private:
	bool m_ShowOutput;
	unsigned long m_lExitCode, m_dwProcessId;

	#ifdef WIN32
		HANDLE m_hStdIn, m_hStdOut, m_hStdErr, m_hProcess;
	#else
		FILE *m_hStdIn, *m_hStdOut, *m_hStdErr;
		unsigned long m_hProcess;
	#endif
};

#endif                     
