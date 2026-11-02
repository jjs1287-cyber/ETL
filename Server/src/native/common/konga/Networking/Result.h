/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_RESULT_H
#define KONGA_RESULT_H

#include <string>
#include <vector>

namespace konga {
namespace networking {

  
                                                 
  
class Result {
public:
	typedef std::vector<std::string>::const_iterator const_iterator;
	typedef std::vector<std::string>::size_type size_type;
public:
	Result(const std::string& sCommand) : m_sCommand(sCommand) {}
	virtual ~Result() {}

	void AddReturnValue(const std::string& sRetVal) { m_vReturnValues.push_back(sRetVal); }
	const_iterator ret_val_begin() const { return m_vReturnValues.begin(); }
	const_iterator ret_val_end() const { return m_vReturnValues.end(); }
	size_type ret_val_size() const { return m_vReturnValues.size(); }

	std::string GetCommandName() const { return m_sCommand; }

private:
	std::string m_sCommand;
	std::vector<std::string> m_vReturnValues;
};

}                        
}                   

#endif                  
