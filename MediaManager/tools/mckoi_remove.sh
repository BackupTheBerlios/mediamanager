#!/bin/sh
#
# MediaManager
# ------------
# Removes the hole Mckoi SQL Database.
#
# USAGE: tools/mckoi_create.sh
# 
# author: crac
# $Id: mckoi_remove.sh,v 1.1 2004/06/21 21:45:16 crac Exp $

MM_SQL_PATH="data"

rm -rf $MM_SQL_PATH
