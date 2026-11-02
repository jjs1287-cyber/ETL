/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_COMMAND_H
#define KONGA_COMMAND_H

#include <string>
#include <vector>

namespace konga {
namespace networking {

class Command {
public:
	typedef std::vector<std::string>::const_iterator const_iterator;
	typedef std::vector<std::string>::size_type size_type;
public:
	explicit Command(const std::string& sCommand) : m_sCommand(sCommand) {}
	virtual ~Command() {}

	void AddArgument(const std::string& sArg) { m_vsArgs.push_back(sArg); }

	void clear() { m_vsArgs.clear(); }

	size_type arg_size() const { return m_vsArgs.size(); }
	const_iterator arg_begin() const { return m_vsArgs.begin(); }
	const_iterator arg_end() const { return m_vsArgs.end(); }

	std::string GetCommandName() const { return m_sCommand; }
	void SetCommandName(const std::string& sCommand) { m_sCommand = sCommand; }
private:
	std::string m_sCommand;
	std::vector<std::string> m_vsArgs;
};

}                   
}                        

#endif                   
