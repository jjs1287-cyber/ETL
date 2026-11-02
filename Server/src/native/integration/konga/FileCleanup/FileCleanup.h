/*
 * Copyright (c) 1998-2009 Jitterbit, Inc.
 *
 */

#if !defined(AFX_FILECLEANUPRULE_H__A3E99324_67A0_4535_A3A0_64049F4BFB23__INCLUDED_)
#define AFX_FILECLEANUPRULE_H__A3E99324_67A0_4535_A3A0_64049F4BFB23__INCLUDED_

#include <vector>
#include <string>

#include "CleanupRule.h"

namespace konga {

typedef std::vector<std::string> StringArray;

enum Comparator {
	Comparator_EQ = 0,
	Comparator_LT = 1,
	Comparator_GT = 2,
	Comparator_LE = 3,
	Comparator_GE = 4,
	Comparator_NE = 5
};

enum SizeUnit {
	SizeUnit_BYTE = 0,
	SizeUnit_KB = 1,
	SizeUnit_MB = 2,
	SizeUnit_GB = 3
};

struct FileAge {
	FileAge(void) : nDays(0), comparator(Comparator_GE) {}
	long nDays;
	Comparator comparator;
};

struct FileSize {
	FileSize(void) : sizeBytes(0), comparator(Comparator_GE) {}
	unsigned long sizeBytes;
	Comparator comparator;
};

struct MatchPattern {
	MatchPattern() : bApplyToSubdir(false) {}
	std::string stPattern;
	bool bApplyToSubdir;
};

struct FileCompression {
	FileCompression(void) : enabled(false), maxCompressed(0) {}
	bool enabled;
	long maxCompressed;
};

class FileCleanup : public CleanupRule
{
public:
	static SizeUnit GetSizeUnit(const std::string& sSizeUnit);
	static long GetSizeInBytes(long lSize, SizeUnit unit);
	static Comparator GetComparator(const std::string& sComparator);

public:
	FileCleanup(void) : m_valid(-1) { Initialize(); }
	virtual ~FileCleanup(void) { Initialize(); }

	std::string getName() const {
		return "File Cleanup (" + GetDirectory() +")";
	}

	void Initialize() {
		m_valid = -1;

		m_fileAge = FileAge();
		m_fileSize = FileSize();

		m_patternList.clear();
		m_notPatternList.clear();

		m_dirName.erase();
		m_lastErrorNum = 0;
		m_searchSubDir = false;
	}

	int GetLastErrorNumber() const { return m_lastErrorNum; }

	bool Execute(std::vector<std::string>& warnings);

	void SetFileAge(const FileAge& fAge) {
		m_fileAge = fAge;
	}

	FileAge GetFileAge() const {
		return m_fileAge;
	}

	void SetFileSize(const FileSize& fSize) {
		m_fileSize = fSize;
	}

	FileSize GetFileSize() const {
		return m_fileSize;
	}

	void SetFileCompression(const FileCompression& compression) {
		m_fileCompression = compression;
	}

	FileCompression GetFileCompression() const {
		return m_fileCompression;
	}

	                                    
	void AddPattern(const MatchPattern& Pattern) {
		m_patternList.push_back(Pattern);
	}

	std::vector<MatchPattern>::size_type GetPatternListSize() const {
		return m_patternList.size();
	}

	std::vector<MatchPattern> GetPatternList() const {
		return m_patternList;
	}

	MatchPattern GetPatternAtIndex(const std::vector<MatchPattern>::size_type& index) const;

	MatchPattern GetNotPatternAtIndex(const std::vector<MatchPattern>::size_type& index) const;

	                                        
	void AddNotPattern(const MatchPattern& NotPattern) {
		m_notPatternList.push_back(NotPattern);
	}

	std::vector<MatchPattern>::size_type GetNotPatternListSize() const {
		return m_notPatternList.size();
	}

	std::vector<MatchPattern> GetNotPatternList() {
		return m_notPatternList;
	}

	                                                 
	void AddIgnoreSubDir(const std::string& strDirName) {
		m_ignoreSubDirList.push_back(strDirName);
	}

	StringArray::size_type GetIgnoreSubDirListSize() const {
		return m_ignoreSubDirList.size();
	}

	StringArray GetIgnoreSubDirList() const {
		return m_ignoreSubDirList;
	}

	std::string GetIgnoreSubDirAtIndex(const StringArray::size_type& index) const;

	void SetDirectory(const std::string& strDir);

	void ConvertToAbsolutePath();

	std::string GetDirectory() const {
		return m_dirName;
	}

	void SetSearchOption(bool searchSubdir) {
		m_searchSubDir = searchSubdir;
	}

	bool GetSearchOption() const {
		return m_searchSubDir;
	}

	bool ValidateRule(std::vector<std::string>& warnings);

private:
	short m_valid;
	FileAge m_fileAge;
	FileSize m_fileSize;
	FileCompression m_fileCompression;

	std::vector<MatchPattern> m_patternList, m_notPatternList;
	StringArray m_ignoreSubDirList;
	std::string m_dirName;
	bool m_searchSubDir;
	int m_lastErrorNum;

	bool SearchDirectoryAndClean(const std::string& strDirName,
		std::vector<std::string>& warnings,
		bool recurseSubdir = true,
		bool isSubdir = false);

	void CompressFile(const std::string& stFilePath);

	bool MatchesFileAge(time_t fileWriteTime);
	bool MatchesFileSize(size_t file_size);
	bool MatchIgnoreDirList(const std::string& dir_name);
	bool match_pattern(const std::string&file_name, bool isSubdir);
	bool match_not_pattern(const std::string&file_name, bool isSubdir);
	static bool specialEqual(const std::string& path1, const std::string& path2);
	bool PathMatchSpec(const std::string& stPath, const std::string& stSpec);
};

}                   

#endif                                                                                    
