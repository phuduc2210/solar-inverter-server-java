#!/bin/bash

SERVICE='com.hazelcast.core.server.StartServer'

function start {
   if ps ax | grep -v grep | grep $SERVICE > /dev/null
   then
      echo "$SERVICE service running, need stop before start"
      return 1;
   fi

   cd /opt/hazelcast
   rm -f /opt/hazelcast/hazelcast.pid
   #javaCmd = "java -server -cp hazelcast.jar:apache-log4j-2.0-beta9.jar -Dhazelcast.config=/opt/hazelcast/bin/hazelcast.xml -Dlog4j.configurationFile=/opt/hazelcast/log4j2.xml com.hazelcast.examples.StartServer"
   javaCmd="java -server -Xms4G -Xmx4G -cp /opt/hazelcast/lib/hazelcast-all-3.7.8.jar -Dhazelcast.config=/opt/hazelcast/bin/hazelcast.xml com.hazelcast.core.server.StartServer"
   cmd="nohup $javaCmd >> /opt/hazelcast/service.log 2>&1 & echo \$! >/opt/hazelcast/hazelcast.pid"
   su -c "$cmd"
   echo "$SERVICE service started"
   return 0; }


function stop {
   pid="$(</opt/hazelcast/hazelcast.pid)"
   kill -s KILL $pid || return 1
   rm -f /opt/hazelcast/hazelcast.pid
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

