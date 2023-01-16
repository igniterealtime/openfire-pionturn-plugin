# Openfire Pion TURN Plugin

This plugin provides a TURN/STUN Server for Openfire.

## CI Build Status

[![Build Status](https://github.com/igniterealtime/openfire-pionturn-plugin/workflows/Java%20CI/badge.svg)](https://github.com/igniterealtime/openfire-pionturn-plugin/actions)

## Overview

This plugin uses the Pion Turn Project (https://github.com/pion/turn) to create a TURN/STUN server for Openfire.

This plugin is only useful when Openfire is in a network location that is **not** behind restrictive constructs like NAT (one of it's main purposes is to *help* clients circumvent NATs in the first place). The STUN service itself needs to see the 'public' IP address of the client (and possibly vice versa). That works best if the server is itself in a public network segment. The TURN service is more elaborate: instead of just being used to report on the observed client IP/port, it will act as a proxy. That means that all data flows over the TURN server (which isn't true for a STUN server). In any case: make your Openfire server have a public IPs, not something behind a NAT.

When your Openfire is behind a NAT, it probably **makes less sense** to run a STUN/TURN server embedded in Openfire.
When this is the case, a valid alternative might be to position a stand-alone STUN/TURN server somewhere 'public' (not NATted), and tie that to Openfire using the external service discovery plugin for Openfire.

When you have clients from outside your network wanting to do STUN/TURN, then it's likely that placing your openfire server in a DMZ will make it easier for them to connect to it. Please note that there probably are drawbacks with regards to security policies etc.

## Known Issues
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
