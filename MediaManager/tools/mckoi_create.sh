#!/bin/sh
#
# MediaManager
# ------------
# Creates Mckoi SQL Database with $MM_USERNAME 
# and $MM_PWD.
#
# USAGE: tools/mckoi_create.sh
# 
# author: crac
# $Id: mckoi_create.sh,v 1.1 2004/06/21 21:45:16 crac Exp $

MM_SQL_USERNAME="mediamanager"
MM_SQL_PWD="mgr"
MM_SQL_CONF="conf/mckoi.conf"
MM_LIBS="lib"

java -jar $MM_LIBS/mckoidb.jar -conf $MM_SQL_CONF -create $MM_SQL_USERNAME $MM_SQL_PWD