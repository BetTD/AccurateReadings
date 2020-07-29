# Accurate**Readings**
#### **Connects to a Pterodactyl panel to get the actual readings on resource usage.**
## What's this plugin?
AccurateReadings aims to provide accurate readings on resource usage by communicating with a Pterodactyl panel using the [Ptero4J](https://github.com/stanjg/Ptero4J) wrapper made by stanjg, and of course using the Pterodactyl panel API.
## Requirements
- **A Pterodactyl panel.** If you're using a shared hosting plan and the panel is powered by Pterodactyl, then you are able to use this plugin. If you're selfhosting your server (home server, your PC, a VPS or dedicated server...), make sure you use the Pterodactyl panel, not only it's really easy to install, but is an excellent panel software. Please read the wiki for more information on how to check if your panel is powered by Pterodactyl.
- **A CraftBukkit server.** This plugin is coded using the Spigot 1.8.8 API, but I have tested it on Paper 1.8.8 and 1.12.2, and have been told it also works on CraftBukkit 1.14.4 due to the use of simple API calls. Whatever you use, make sure it's CraftBukkit or one of its forks (e.g. Spigot, or one of its forks: Paper, BeerSpigot...)
## How to install
1. Go to [your Pterodactyl panel](https://control.sparkedhost.us/account/api) and, on the left sidebar, click on 'Account API'.
2. On the top right corner, click on Create New. Add a description (e.g. "AccurateReadings") and leave the 'Allowed Connection IPs' empty unless you're **100% sure** what IP will your server be using to connect to the API (this is hard to determine on shared hosting environments as it may use the node's main IP address to communicate).
3. Copy the API key for later.
4. Upload the plugin JAR (you can grab it from the [releases page](https://github.com/BetTD/AccurateReadings/releases/)) to the plugins directory on your server.
5. Restart your server in order to generate the config file. The plugin will then disable itself, please stop the server after it has fully loaded and navigate to `plugins/AccurateReadings`, then open the config.yml file.
6. Then fill in the rest of the options: `panelUrl` is the URL of your Pterodactyl panel; `serverId` is your server ID and you can find it on the main page of your panel, at the very left of the server list; `apiKey` is the API key we copied earlier. Make sure you don't remove the quotation marks from any of the options.
7. Save the changes and start the server. Take a look at the console and carefully read through the output. If you see this: `Ptero4J loaded! Successfully made contact with the panel.` It means you're completely good to go and the plugin is ready to go. Otherwise, double-check the config.yml file. If it still doesn't work, please read "Contact me" at the end of this document.
## How to use
**The main command is `/perf`**, although there are aliases: `/performance` and `/lag`. This may override the Essentials' `/lag` command, but you can fix this in the `commands.yml` file on your server or by using `/elag`.
## Known issues
- `Command Dispatched Async: /spigot:tps`. This will be fixed in the full release.
- `[00:20:31 WARN]: [okhttp3.OkHttpClient] A connection to <insert panel link here> was leaked. Did you forget to close a response body? To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);`. Unfortunately I cannot find a solution to this problem no matter what I try. I will contact the Ptero4J author in order to hopefully find a solution to this.
- **The restart feature kills the server completely.** I have looked at the entire code and couldn't find a reason as to why would this happen.
## Disclaimer
I am new to the programming world and my code may not look particularly good. **Any help is appreciated however**, so feel free to contribute to the code and make it more awesome!
And please read the `LICENSE` document on this repository.
## Credits
A big thank you to Square for providing the [OkHttp](https://github.com/square/okhttp) and [Okio](https://github.com/square/okio) libraries used by [Ptero4J](https://github.com/stanjg/Ptero4J), which is also a huge part of AccurateReadings by providing a wrapper to easily communicate with any Pterodactyl panel API. Thanks to the Sparked Host LLC community for giving ideas, suggestions and bug reports.
## Version history
- **1.0.0-dev01**. First version of the plugin that actually worked. It made the main thread hang while the plugin communicated with the panel.
- **1.0.0-dev02**. Fixed the main thread freezing issue. Added new options to the config.yml
## Contact me
- Through Discord: `Raúl#1234` or the [Sparked Host Discord server](https://discord.gg/sparked)
- Through e-mail: `between13131@gmail.com`
- Through Twitter DMs: `@BetTD_ES` *(my profile here is in Spanish, but don't get scared by that)*
