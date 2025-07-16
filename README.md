[![PasswordWhitelist: Build](https://github.com/Strokkur424/PasswordWhitelist/actions/workflows/build.yml/badge.svg)](https://github.com/Strokkur424/PasswordWhitelist/actions/workflows/build.yml)

# PasswordWhitelist

A Paper and Folia plugin to block unauthorized players from joining your server.

## Compiling the project

Compiling the project is as simple as cloning the repository and running the following command:

```bash
$ ./gradlew build
```

The build jar will be available under `build/libs/PasswordWhitelist-1.X.X.jar`.

## Server compatibility

This plugin should work on all Paper servers on version 1.21.7 (build #17) and higher.
Folia compatibility is a goal, which at the time of writing might not be reached yet,
as no public builds of Folia 1.21.7 have been released so far.

It is planned to expand the target server compatibilities onto most other platforms
as well. This will happen once Adventure releases their dialog API.

## Permissions

Below is a table of all permissions, their default values, and their descriptions:

| Permission                              | Default | Description                                                                  |
|-----------------------------------------|---------|------------------------------------------------------------------------------|
| passwordwhitelist.exclude               | false   | Excludes a user from having to enter the password                            |
| passwordwhitelist.command.use           | op      | Main permission required to run the /passwordwhitelist command               |
| passwordwhitelist.command.help          | true    | Permission required to run the '/passwordwhitelist help' subcommand          |
| passwordwhitelist.command.reload        | true    | Permission required to run the '/passwordwhitelist reload' subcommand        |
| passwordwhitelist.command.enable        | true    | Permission required to run the '/passwordwhitelist enable' subcommand        |
| passwordwhitelist.command.disable       | true    | Permission required to run the '/passwordwhitelist disable' subcommand       |
| passwordwhitelist.command.attempts      | true    | Permission required to run the '/passwordwhitelist attempts' subcommand      |
| passwordwhitelist.command.password      | true    | Permission required to run the '/passwordwhitelist password' subcommand      |
| passwordwhitelist.command.password.set  | op      | Permission required to run the '/passwordwhitelist password set' subcommand  |
| passwordwhitelist.command.password.show | op      | Permission required to run the '/passwordwhitelist password show' subcommand |

If you are confused about the `true` values for some of the subcommands: The only reason they are set to `true` by
default is to make quick permission setups easier. All players require to `passwordwhitelist.command.use` permission
in order to use any command. The sub-permissions merely allow for fine-grained control for server owners.

### Note regarding the exclude permission

Because of the exclude permission being checked during the configuration phase of a player's server connection, it is
not possible
to check the player's permission through Paper's API. Therefore, this feature requires
that [LuckPerms](https://luckperms.net)
is installed on the server.

## Commands

The server defines only one command: `/passwordwhitelist`. You have the option to instead use one of the
two aliases: `/passw` and `/pw`.

| Command                                      | Description                                    |
|----------------------------------------------|------------------------------------------------|
| `/passwordwhitelist help`                    | Show a help message                            |
| `/passwordwhitelist reload [<file>]`         | Reload a plugin data file                      |
| `/passwordwhitelist enable`                  | Enable the password whitelist                  |
| `/passwordwhitelist disable`                 | Disable the password whitelist                 |
| `/passwordwhitelist password set <password>` | Set a new password                             |
| `/passwordwhitelist password show`           | Show the current password                      |
| `/passwordwhitelist attempt reset <player>`  | Reset a player's failed password attempt count |

## Configuration files

PasswordWhitelist comes with two configuration files for you to modify:

- `plugins/PasswordWhitelist/config.yml`

This file contains general configuration on the plugin and the dialogs that get send to the players if
password auth is required.

- `plugins/PasswordWhitelist/messages.yml`

This file contains message-related configuration. Almost every message is configurable. We use
the [MiniMessage Format](https://docs.advntr.dev/minimessage/format.html).