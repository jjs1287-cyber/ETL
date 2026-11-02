/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */
#ifndef KONGA_OPERATION_PROCESSED_FILES_H
#define KONGA_OPERATION_PROCESSED_FILES_H

#include <time.h>

#include <string>
#include <vector>
#include <map>

namespace konga {

	class ProcessedFile {
	public:
		ProcessedFile(void)
		: m_bInHistory(false), m_llSourceSize(0), m_llTargetSize(0), m_llSourceRecords(0), m_llTargetRecords(0),
		  m_iMonth(0), m_iDay(0), m_iTimeOrYear(0), m_transferTime(0), m_lTransformationTime(0) {
		}

		                                                                                     
		ProcessedFile(const std::string& stRemoteSourceLocation, long long size, int month, int day, int time_or_year)
			: m_bInHistory(false), m_llSourceSize(size), m_llTargetSize(0), m_llSourceRecords(0), m_llTargetRecords(0),
			  m_iMonth(month), m_iDay(day), m_iTimeOrYear(time_or_year), m_transferTime(0), m_lTransformationTime(0) {
			setRemoteSourceLocation(stRemoteSourceLocation);
		}

		~ProcessedFile(void) {};

		                                                    
		std::string getRemoteSourceLocation() const {
			return m_stRemoteSourceLocation;
		}

		                                                                                                      
		std::string getRemoteSourceLeafName() const;

		                                                                                      
		std::string getLocalSourceLocation() const {
			return m_stLocalSourceLocation;
		}

		                                                               
		std::string getLocalSourceLeafName() const;

		                                                                         
		std::string getLocalTargetLocation() const {
			return m_stLocalTargetLocation;
		}

		                                                    
		std::string getRemoteTargetLocation() const {
			return m_stRemoteTargetLocation;
		}

		                                   
		void setRemoteSourceLocation(const std::string& stRemoteSourceLocation);

		                                                
		                                                                                         
		                                  
		void setLocalSourceLocation(const std::string& stLocalSourceLocation, bool bCheckExistance = true);

		                                                              
		void setLocalTargetLocation(const std::string& stLocalTargetLocation) {
			m_stLocalTargetLocation = stLocalTargetLocation;
		}

		void setRemoteTargetLocation(const std::string& stRemoteTargetLocation) {
			m_stRemoteTargetLocation = stRemoteTargetLocation;
		}

		void setInHistory(bool bInHistory) {
			m_bInHistory = bInHistory;
		}

		bool isInHistory() const {
			return m_bInHistory;
		}

		long long getTotalSize() const {
			return m_llSourceSize + m_llTargetSize;
		}

		long long getTargetSize() const {
			return m_llTargetSize;
		}

		void setTargetSize(long long lSize) {
			m_llTargetSize = lSize < 0 ? 0 : lSize;
		}

		long long getSourceSize() const {
			return m_llSourceSize;
		}

		void setSourceSize(long long lSize) {
			m_llSourceSize = lSize < 0 ? 0 : lSize;
		}

		void setSourceRecords(long long llRecords) {
			m_llSourceRecords = llRecords;
		}

		long long getSourceRecords() const {
			return m_llSourceRecords;
		}

		void setTargetRecords(long long llRecords) {
			m_llTargetRecords = llRecords;
		}

		long long getTargetRecords() const {
			return m_llTargetRecords;
		}

		int day() const {
			return m_iDay;
		}

		void day(int d) {
			m_iDay = d;
		}

		int month() const {
			return m_iMonth;
		}

		void month(int m) {
			m_iMonth = m;
		}

		int time_or_year() const {
			return m_iTimeOrYear;
		}

		void time_or_year(int ity) {
			m_iTimeOrYear = ity;
		}

		time_t transferedTime() const {
			return m_transferTime;
		}

		void setTransferedTime(const time_t& t) {
			m_transferTime = t;
		}

		long getTransformationTime() const {
			return m_lTransformationTime;
		}

		void setTransformationTime(long lMilliseconds) {
			m_lTransformationTime = lMilliseconds;
		}

		                                                
		void setTransferedNow();

		                                                
		void setFileChangeTimeNow();

		void addError(const std::string& stError) {
			if ( !m_stError.empty() )
				m_stError += "\n";
			m_stError += stError;
		}

		std::string getError() const {
			return m_stError;
		}

		void addWarning(const std::string& stWarning){
			if ( stWarning.empty() )
				return;

			if ( !m_stWarning.empty() )
				m_stWarning += "\n";
			m_stWarning += stWarning;
		}

		std::string getWarning() const {
			return m_stWarning;
		}

		std::string archiveEntryName;
        std::string archiveTargetName;
        bool hasError() const {
			return !m_stError.empty();
		}

		bool hasWarning() const {
			return !m_stWarning.empty();
		}

	private:
		bool m_bInHistory;

		long long m_llSourceSize;
		long long m_llTargetSize;

		long long m_llSourceRecords;
		long long m_llTargetRecords;

		int m_iMonth;
		int m_iDay;
		int m_iTimeOrYear;

		time_t m_transferTime;

		long m_lTransformationTime;

		std::string m_stRemoteSourceLocation;
		std::string m_stLocalSourceLocation;

		std::string m_stLocalTargetLocation;
		std::string m_stRemoteTargetLocation;

		std::string m_stError, m_stWarning;
	};

	class ProcessedFiles {
	public:
		ProcessedFiles() { }
		~ProcessedFiles(void) { }

		void add(const ProcessedFile& file) {
			m_vProcessedFiles.push_back(file);
		}

		bool empty() const {
			return m_vProcessedFiles.empty();
		}

		                                                                       
		bool hasErrors() const;

		                                                                         
		bool hasWarnings() const;

		                                                  
		bool hasOnlyErrors() const;

		                                                                                                 
		int filesInHistory() const;

		                                                                                               
		int filesToProcess() const;

		                                             
		                                                                                   
		std::vector<ProcessedFile> getProcessedFiles() const {
			return m_vProcessedFiles;
		}

		                                                                                                              
		                                                                      
		std::vector<ProcessedFile>& getProcessedFilesByRef() {
			return m_vProcessedFiles;
		}

		                                               
		const std::vector<ProcessedFile>& getProcessedFilesByRef() const {
			return m_vProcessedFiles;
		}

		int getNumberOfProcessedFiles() const {
			return (int)m_vProcessedFiles.size();
		}

		                                                                   
		void clearSourceProcessedFlags();
		void flagSourceFileAsProcessed(const ProcessedFile& file);

		bool hasSourceFileBeenProcessed(const ProcessedFile& file) const;

	private:
		std::vector<ProcessedFile> m_vProcessedFiles;
		std::map<std::string, int> m_sourceProcessedMap;
	};
}
#endif                                     

