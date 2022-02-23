# Openfire Pion TURN Plugin

This plugin provides a TURN/STUN Server for Openfire.

## CI Build Status

[![Build Status](https://github.com/igniterealtime/openfire-pionturn-plugin/workflows/Java%20CI/badge.svg)](https://github.com/igniterealtime/openfire-pionturn-plugin/actions)

<h2>Overview</h2>

<p>
This plugin uses the Pion Turn Project (https://github.com/pion/turn) to create a TURN/STUN server for Openfire.
</p>

<h2>Known Issues</h2>
This version has embedded binaries for only Linux 64, MacOS 64, Windows 64 and Windows 32.
<p>

</p>
<h2>Installation</h2>

<ol>
    <li>Copy the pionturn.jar file to the OPENFIRE_HOME/plugins directory.</li>
    <li>Configure the admin properties page.</li>
</ol>

<h2>Configuration</h2>

Under Server|Media Services settings -> PionTurn tab you can configure the parameters.
After you've set up the STUN and TURN service, you'll need to configure Openfire to expose the availability of this new service to your users. You can do this easily with the External Service Discovery plugin for Openfire.

<h2>How to use</h2>

<p>
See this blog - https://discourse.igniterealtime.org/t/preparing-openfire-for-audio-video-calls-with-conversations/87828
<p/>

## Reporting Issues

Issues may be reported to the [forums](https://discourse.igniterealtime.org) or via this repo's [Github Issues](https://github.com/igniterealtime/openfire-pionturn-plugin).
