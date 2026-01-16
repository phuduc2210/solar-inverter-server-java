#!/bin/bash

SERVICE='mancenter-3.7.8.war'

function start {
   if ps ax | grep -v grep | grep $SERVICE > /dev/null
   then
      echo "$SERVICE service running, need stop before start"
      return 1;
   fi

   cd /opt/hazelcast
   rm -f /opt/hazelcast/mancenter.pid
   javaCmd="java -jar /opt/hazelcast/mancenter/mancenter-3.7.8.war 8080 mancenter"
   cmd="nohup $javaCmd >> /opt/hazelcast/mancenter.log 2>&1 & echo \$! >/opt/hazelcast/mancenter.pid"
   su -c "$cmd"
   echo "$SERVICE service started"
   return 0; }


function stop {
   pid="$(</opt/hazelcast/mancenter.pid)"
   kill -s KILL $pid || return 1
   rm -f /opt/hazelcast/mancenter.pid
   echo "$SERVICE service stopped"
   return 0; }

function restart {
   stop
   start
   return 0; }

function status {
   ps ax | grep -v grep | grep $SERVICE
   return 0; }

function main {
   RETVAL=0
   case "$1" in
      start)
         start
         ;;
      stop)
         stop
         ;;
      restart)
         restart
         ;;
      status)
         status
         ;;
      *)
         echo "Usage: $0 {start|stop}"
         exit 1
         ;;
      esac
   exit $RETVAL
}


main $1

