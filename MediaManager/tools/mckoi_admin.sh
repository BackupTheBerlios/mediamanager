#!/bin/sh
#
# MediaManager
# ------------
# Opens gui interface to the Mckoi SQL database.
#
# USAGE: tools/mckoi_admin.sh
# 
# author: crac
# $Id: mckoi_admin.sh,v 1.1 2004/06/21 21:45:16 crac Exp $

MM_SQL_URL="jdbc:mckoi:local://./conf/mckoi.conf"
MM_SQL_USERNAME="mediamanager"
MM_SQL_PWD="mgr"
MM_LIBS="lib"

java -cp $MM_LIBS/mckoidb.jar com.mckoi.tools.JDBCQueryTool \
-url $MM_SQL_URL \
-u $MM_SQL_USERNAME -p $MM_SQL_PWD
