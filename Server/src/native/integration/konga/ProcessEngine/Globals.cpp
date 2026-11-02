/*
 * Copyright (c) 1998-2007 Jitterbit, Inc.
 *
 */
#include <string>

#include "Globals.h"

int g_log_type;

bool g_log_event, g_log_file, g_log_msgbox, g_log_debug;
bool g_log_smtp_cmds = false;

EngineCore	*g_engine;
Messenger	*g_msgr;

std::string	g_status_str;

