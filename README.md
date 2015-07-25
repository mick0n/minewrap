# Minewrap
A set-and-forget minecraft server wrapper which enables scheduled restarts and backups of your server. It is meant to be kept as simple as possible yet robust.

### Requirements
The built in backup capability uses 7zip for compressing and storing backuped data. This must be installed on the machine where Minewrap runs and also it must be added to the PATH environment variable.

### Usage
Setup the minewrap.properties file according to your system setup, run the provided scripts or run the jar file directly and you're done.

### Planned features for version 1.0
* Integrated compression using zip/gzip.
* A few more options for customization, for example setting scheduling intervals manually.
* Logging to console and file and thus replacing writing directly to stdout.

**DISCLAIMER: This is still a work-in-progress application!**