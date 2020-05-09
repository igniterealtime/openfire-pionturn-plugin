# PionTurn Plugin for openfire

This plugin uses the Pion Turn Project (https://github.com/pion/turn) to create a TURN/STUN server for Openfire.

## Installation

Copy the pionturn.jar file to the OPENFIRE_HOME/plugins directory

## Configuration

Under Server|Media Services settings -> PionTurn tab you can configure the parameters.
After you've set up the STUN and TURN service, you'll need to configure Openfire to expose the availability of this new service to your users. You can do this easily with the External Service Discovery plugin for Openfire.

## How to use

See this blog - https://discourse.igniterealtime.org/t/preparing-openfire-for-audio-video-calls-with-conversations/87828

## Known Issues
This version has embedded binaries for only Linux 64 and Windows 64.

