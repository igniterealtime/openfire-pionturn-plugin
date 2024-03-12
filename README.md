# Openfire Pion TURN Plugin

This plugin provides a TURN/STUN Server for Openfire.

## CI Build Status

[![Build Status](https://github.com/igniterealtime/openfire-pionturn-plugin/workflows/Java%20CI/badge.svg)](https://github.com/igniterealtime/openfire-pionturn-plugin/actions)

## Overview

This plugin uses the Pion Turn Project (https://github.com/pion/turn) to create a TURN/STUN server for Openfire. This plugin is used to help users who are on uncooperative networks to connect their calls/video calls.


## Known Issues
This version has embedded binaries for only Linux 64 and Windows 64.
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
See this guide - https://discourse.igniterealtime.org/t/guide-pionturn-external-service-discovery-usage-for-people-trying-to-set-up-calls/93699
<p/>

## Reporting Issues

Issues may be reported to the [forums](https://discourse.igniterealtime.org) or via this repo's [Github Issues](https://github.com/igniterealtime/openfire-pionturn-plugin).
