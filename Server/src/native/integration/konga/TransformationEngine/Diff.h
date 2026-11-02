/*
 * Copyright (c) 1998-2011 Jitterbit, Inc.
 *
 */

#ifndef _TRANSFORMATION_ENGINE_DIFFSNAPSHOT_H
#define _TRANSFORMATION_ENGINE_DIFFSNAPSHOT_H

#include <vector>
#include <string>

#include <konga/Utils/AutoPtrCleanup.h>		                                   

#include "DiffConstants.h"

class TreeMapper;

namespace konga {
namespace diff {

class DiffSnapShot
{
public:
	DiffSnapShot(TreeMapper*pTreeMapper);
	~DiffSnapShot();

	void Initialize(const std::string& stDiffId);

	void Reset(const std::string& stDiffId, DiffResetAction action);

	                                               
	bool ProcessSnapShot(std::string& newFileName);
	void DiffComplete();

	DiffStatus getDiffStatus() const;

	bool hasProcessed() const;
	bool hasOldSnapshot() const;

	std::string getCurrentFile() const;

	std::string getAddFileName() const;
	std::string getDeleteFileName() const;
	std::string getUpdateFileName() const;

	std::string getOldSnapshotFileName() const;
	std::string getNewSnapshotFileName() const;

private:
	void init();

	bool ProcessSnapShot_chunked();
	bool ProcessSnapShot_ordered();

	std::string getDiffGuid() const;

	std::string getBaseDirectoryWithSlash() const;
	std::string getTempFileName() const;

	                             
	bool parseRecord(konga::AutoArrayDelete<char>& buf, int& buf_len, FILE*in, const std::vector<konga::DataElement*>& ids, std::string& line);

	                                                                                                                        
	int compareKeys();

	std::string getPKString(const std::vector<konga::DataElement*>& delist) const;

private:
	TreeMapper* pTreeMapper;
	unsigned char strQual, delimiter, numQual;

	bool bIsCsv, bIsDB;
	std::vector<konga::DataElement*> old_delist, new_delist;
	std::vector<konga::DataElement*> old_pklist, new_pklist;

	std::vector<bool> order_list;

	mutable std::string m_addFileName, m_updateFileName, m_deleteFileName, m_oldFileName, m_newFileName, m_tmpFileName;
	mutable std::string diff_guid, base_dir;
};

}
}
#endif                                         

