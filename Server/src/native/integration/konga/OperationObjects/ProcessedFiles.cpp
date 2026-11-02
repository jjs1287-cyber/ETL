/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#include <sstream>

#include <konga/Utils/DateUtils.h>
#include <konga/Utils/FileUtils.h>

#include "ProcessedFiles.h"
#include "OperationEngineException.h"

using namespace konga;
using std::vector;
using std::string;
using std::stringstream;
using std::map;

void ProcessedFile::setRemoteSourceLocation(const std::string& stRemoteSourceLocation) {
	m_stRemoteSourceLocation = stRemoteSourceLocation;
}

void ProcessedFile::setLocalSourceLocation(const std::string& stLocalSourceLocation,
										   bool bCheckExistance          )
{
	if ( bCheckExistance && !FileExists(stLocalSourceLocation) ) {
		throw OperationEngineException("The supposedly transferred file '" + stLocalSourceLocation
			+ "' does not exist.");
	}
	m_stLocalSourceLocation = stLocalSourceLocation;
	if ( bCheckExistance )
		setTransferedNow();
}

string ProcessedFile::getRemoteSourceLeafName() const {
 if (!archiveEntryName.empty()) return GetLeafPath(archiveEntryName);
	if ( m_stRemoteSourceLocation.empty () )
		return string();

	return GetLeafPath(m_stRemoteSourceLocation);
}

string ProcessedFile::getLocalSourceLeafName() const {
	if ( m_stLocalSourceLocation.empty() )
		return string();

	return GetLeafPath(m_stLocalSourceLocation);
}

void ProcessedFile::setTransferedNow() {
	m_transferTime = CrDateTime::GetCurrentTime().GetTime();
}

void ProcessedFile::setFileChangeTimeNow() {
	CrDateTime crnow( CrDateTime::GetCurrentTime() );
	m_iMonth = crnow.GetMonth();
	m_iDay = crnow.GetDay();
	stringstream sTime;
	sTime << crnow.GetHour() << crnow.GetMinute() << crnow.GetSecond();
	m_iTimeOrYear = atoi(sTime.str().c_str());
}

bool ProcessedFiles::hasErrors() const {
	for ( vector<ProcessedFile>::const_iterator it = m_vProcessedFiles.begin();
		  it != m_vProcessedFiles.end(); ++it ) {
		if ( it->hasError() )
			return true;
	}
	return false;
}

bool ProcessedFiles::hasWarnings() const {
	for ( vector<ProcessedFile>::const_iterator it = m_vProcessedFiles.begin();
		  it != m_vProcessedFiles.end(); ++it ) {
		if ( it->hasWarning() )
			return true;
	}
	return false;
}

bool ProcessedFiles::hasOnlyErrors() const {
	for ( vector<ProcessedFile>::const_iterator it = m_vProcessedFiles.begin();
		it != m_vProcessedFiles.end(); ++it ) {
			if ( !it->hasError() )
				return false;
	}
	return true;
}

int ProcessedFiles::filesInHistory() const {
	int iRetVal = 0;
	vector<ProcessedFile>::const_iterator it = m_vProcessedFiles.begin();
	for (; it != m_vProcessedFiles.end(); ++it ) {
		if ( it->isInHistory() ) ++iRetVal;
	}
	return iRetVal;
}

int ProcessedFiles::filesToProcess() const {
	return m_vProcessedFiles.size() - filesInHistory();
}

void ProcessedFiles::flagSourceFileAsProcessed(const ProcessedFile& file) {
	const string& key = file.getRemoteSourceLeafName();
	map<string, int>::const_iterator cit = m_sourceProcessedMap.find(key);
	if ( cit == m_sourceProcessedMap.end() ) {
		m_sourceProcessedMap.insert(map<string, int>::value_type(key, 1));
	}
}

bool ProcessedFiles::hasSourceFileBeenProcessed(const ProcessedFile& file) const {
	const string& key = file.getRemoteSourceLeafName();
	return ( m_sourceProcessedMap.find(key) != m_sourceProcessedMap.end() );
}

void ProcessedFiles::clearSourceProcessedFlags() {
	m_sourceProcessedMap.clear();
}

