#!/bin/bash
#
# Starts and stops the process specified by the second commandline argument.
# Optional third argument is the 'nice' to be used for the process.

umask 022

system=`uname`
if [ "$system" != "Linux" ] && [ "$system" != "SunOS" ];then
    echo "System not supported: $system"
    exit 1
fi

etlforge_user=etlforge
su_cmd="su -s /bin/sh"
if [ "$system" == "SunOS" ]; then
    etlforge_user=jitterb
    su_cmd=su
fi

check_etlforge_home() {
    if [ -z "$ETLFORGE_HOME" ]; then
	if [ -f /etc/etlforge ]; then
	    . /etc/etlforge
	elif [ -f /etc/sysconfig/etlforge ]; then
	    . /etc/sysconfig/etlforge
	fi

	if [ -z "$ETLFORGE_HOME" ]; then
	    if [ -f /usr/local/etlforge/etlforge.conf ]; then
		ETLFORGE_HOME=/usr/local/etlforge
	    elif [ -f /opt/etlforge/etlforge.conf ]; then
		ETLFORGE_HOME=/opt/etlforge
	    elif [ -f /usr/etlforge/etlforge.conf ]; then
		ETLFORGE_HOME=/usr/etlforge
	    fi

	    if [ -z "$ETLFORGE_HOME" ]; then
		echo "Operation failed:"
		echo "The environment variable ETLFORGE_HOME has not been set and no"
		echo "existing installation could be found."
		exit 1
	    fi
	    echo "Warning: The environment variable ETLFORGE_HOME has not been set,"
	    echo "however an installation was found at $ETLFORGE_HOME."
	    export ETLFORGE_HOME
	fi

	if [ ! -d $ETLFORGE_HOME ]; then
	    echo "Operation failed:"
	    echo "ETLFORGE_HOME was set to $ETLFORGE_HOME"
	    echo "but this directory does not exist."
	    echo "Please set ETLFORGE_HOME to the home directory"
	    echo "of your ETLForge installation."
	    exit 1
	fi
    fi
}

# sets PIDOF_PROG to the pid of the process we want to stop.
jgetpid() {
    PIDOF_PROG=`pgrep -u $etlforge_user -f $process`
}

# Returns 0 if wait was successful, 1 otherwise
wait_for_process() {
    echo -n "Waiting for the $process process to stop.."
    for t in 1 1 2 2 3 3 4 4 5 5 5 0
    do
      # Check if the process is running
      jgetpid
      if [ -z "$PIDOF_PROG" ]; then
	  echo " done"
	  return 0
      fi
      if [ $t -ne 0 ]; then
	  echo -n "."
	  sleep $t
      fi
    done
    echo
    return 1
}

start() {
    check_etlforge_home
    # Check if program is running    
    jgetpid
    if [ "$PIDOF_PROG" != "" ]; then
	echo -n "$process is already running with pid "
	echo $PIDOF_PROG
	return 0
    fi
    
    echo -n  "Starting $process... "
    KONGA_BIN=$ETLFORGE_HOME/bin
    KONGA_PROG=$KONGA_BIN/$process
    KONGA_LOG=$ETLFORGE_HOME/log
    PROG_OUT=$KONGA_LOG/$process.out
    PROG_ERR=$KONGA_LOG/$process.err

    if [ -z "$nice" ]; then
	SET_NICE=
    else
	SET_NICE="nice -n $nice "
    fi

    cd $KONGA_LOG
    COMMAND="LD_LIBRARY_PATH=$LD_LIBRARY_PATH; export LD_LIBRARY_PATH;  $SET_NICE$KONGA_PROG > /dev/null 2> $PROG_ERR &"
#    echo $COMMAND
    $su_cmd $etlforge_user -c "$COMMAND"
    retval=$?
    if [ $retval -eq 0 ]; then
	echo "done"
	return 0
    fi
    exit $retval
}

sig_int_impl() {
    # Check if program is running.
    jgetpid
    if [ -z "$PIDOF_PROG" ]; then
	echo "$process is not running"
	return 1
    fi

    kill -2 $PIDOF_PROG || exit 1
}

sig_int() {
    check_etlforge_home
    sig_int_impl
}

wait_proc() {
    check_etlforge_home
    wait_for_process
    return $?
}

wait_then_terminate() {
    check_etlforge_home
    wait_for_process
    [ $? -ne 0 ] || return 0

    echo "$process did not respond to SIGINT in a timely manner"
    echo "now trying to shut it down with SIGTERM"
    kill -15 $PIDOF_PROG || exit 1
    wait_for_process
    [ $? -ne 0 ] || return 0

    echo "$process did not respond to SIGTERM in a timely manner"
    echo "now trying to shut it down with SIGKILL"
    kill -9 $PIDOF_PROG || exit 1
    wait_for_process
    [ $? -ne 0 ] || return 0

    echo "Failed to stop $process"
    exit 1
}

stop() {
    check_etlforge_home
    sig_int_impl
    wait_then_terminate
}

status() {
    jgetpid
    if [ -z "$PIDOF_PROG" ]; then
	echo "$process is not running."
	exit 1
    else
	echo "$process is running with PID $PIDOF_PROG"
	exit 0
    fi
}

print_usage_and_exit() {
    echo "Usage: $0 {start|stop|sig_int|wait|restart|status} process_name [nice]"
    exit 1
}

# See how we were called.
if [ -z "$2" ]; then
    print_usage_and_exit
fi
process=$2
nice=$3

# wait_then_term is an internal option and should be used in conjunction with sig_int.

case "$1" in
  start)
	start
	;;
  stop)
	stop
	;;
  sig_int)
	sig_int
	;;
  wait)
	wait_proc
	;;
  restart)
	stop
	start
	;;
  status)
	status
	;;
  wait_then_term)
	wait_then_terminate
	;;
  *)
	print_usage_and_exit
esac
