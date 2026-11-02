/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_RESPONSE_H
#define KONGA_RESPONSE_H

#include <string>
#include <vector>

#include <konga/Utils/CrException.h>

namespace konga {
namespace networking {

class Result;

  
                                             
  
class Response {
public:
	typedef std::vector<Result*>::const_iterator const_iterator;
	typedef std::vector<Result*>::size_type size_type;
public:
	Response() : m_bExceptionOccurred(false) {}
	Response(const CrException& e) : m_bExceptionOccurred(true), m_exception(e) {}
	virtual ~Response();

	void AddResult(Result* pResult) { m_vResults.push_back(pResult); }
	const_iterator res_begin() const { return m_vResults.begin(); }
	const_iterator res_end() const { return m_vResults.end(); }
	size_type res_size() const { return m_vResults.size(); }

	bool HasExceptionOccurred() const { return m_bExceptionOccurred; }
	CrException GetException() const { return m_exception; }

private:
	bool m_bExceptionOccurred;
	CrException m_exception;
	std::vector<Result*> m_vResults;
};

}                   
}                        



#endif                    
