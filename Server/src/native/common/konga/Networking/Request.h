/*
 * Copyright (c) 1998-2005 Jitterbit, Inc.
 *
 */
#ifndef KONGA_REQUEST_H
#define KONGA_REQUEST_H

#include <string>
#include <vector>

namespace konga {
namespace networking {

class Command;

  
                                             
  
class Request {
public:
	typedef std::vector<Command*>::const_iterator const_iterator;
	typedef std::vector<Command*>::size_type size_type;
public:
	Request(const std::string& sUserName, const std::string& sPassword) :
		m_sUserName(sUserName), m_sPassword(sPassword) {}
	virtual ~Request();

	void AddCommand(Command* pCommand) { m_vCommands.push_back(pCommand); }
	const_iterator cmd_begin() const { return m_vCommands.begin(); }
	const_iterator cmd_end() const { return m_vCommands.end(); }
	size_type cmd_size() const { return m_vCommands.size(); }

	std::string GetUserName() const { return m_sUserName; }
	std::string GetPassword() const { return m_sPassword; }

	void SetUserName(const std::string& sUserName) { m_sUserName = sUserName; }
	void SetPassword(const std::string& sPassword) { m_sPassword = sPassword; }
private:
	std::string m_sUserName, m_sPassword;
	std::vector<Command*> m_vCommands;
};

}                   
}                        



#endif                   
