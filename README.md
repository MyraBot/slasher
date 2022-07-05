# 🗡️ Slasher

A simple slash command handler written in Kotlin. Annotation based command mapping with a simple support for subcommands.  
Because this handler is for a private Discord wrapper this code won't work. Feel free to port it though. This shouldn't take
too long!

![Warning](https://raw.githubusercontent.com/MyraBot/.github/main/code-advise.png)

## 🏗️ Set up

Before registering commands you have to set up the command handler. The handler is based on reflection which makes
registering commands easier. At least I feel so.  
This means that you are not able to register commands by a function or so. To register an entire package assign
the `commandPackage` variable to the package, separated by dots.  
The `handler` function return the actual `Handler` class which implements `EventListener`. The `EventListener` interface
makes it possible to listen for events in my Discord Wrapper. So store the result of the `handler` function, since we will
later register the handler as an event listener. To finish the configuration of the handler load the [cogs](#cogs) by
using `Handler#loadCogs`.

To make the handler actually handle commands, we have to register the stored handler variable as an `EventListener`
from `diskord#addEventListener`.

```kotlin
fun main() {

    val commandHandler = handler {
        commandPackage = "com.example.myApp.commands"
    }.also { it.loadCogs() }

    diskord {
        token = "YOUR_TOKEN"
        addListeners(commandHandler)
        connectGateway()
    }

}
```
Now you are ready to write your command functions. Let's take a look at 2 examples.  
So we want to make function handling the following 2 commands:
* `/help`
* `/ban temp [user] [timeInMinutes]`

The first command is simple:
```kotlin
package com.example.myApp.commands.General

object General {
    
    @Command("help")
    fun onHelpCommand(ctx: CommandContext) {
        /* Handle help command */
    }
    
}
```

The second command is a subcommand, and it contains arguments. To handle such case you do the following:

```kotlin
package com.example.myApp.commands.Moderation

object Moderation {
    
    @Command("ban temp")
    fun onBanTempCommand(ctx: CommandContext, user: User, timeInMinutes: Int) {
        /* Handle temp ban command */
    }
    
}
```
Note that the parameters of `onBanTempCommand` must match to 100% with the argument names of the command. Read more about arguments [here](#command-arguments)

## 🧬 Structure

### Cogs

Commands are grouped together in kotlin objects. You **can't use classes** for this! Because the library is annotation based,
you shouldn't use an object for a single command function.  
For example: You have an `info user`, `info guild` and `info emoji` command then you would put them all in one object (or called Cog).

### Commands

As already said commands are annotation based. I prefer the way of having multiple commands in a single file/class/object.
With the `Command` annotation you can make any function which is in a [Cog](#cogs), a command function. Always remember to
make the **command function public**, otherwise it won't work.

### Command Arguments

Arguments are handled as command function parameters. Pretty cool, huh?  
So let's take a look at an example.  
Imagine you have a command like this one: `/ban [user] [reason]`  
So your command function will probably look something like this:

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext) {
    // How do I get the user and reason argument?
}
```

In order to use the arguments, add their datatype with the exact argument name as a parameter in the function:

#### Valid ✔️

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext, user: Member, reason: String) {
    /* Handle ban */
}
```

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext, user: User, reason: String) {
    /* Handle ban */
}
```

#### Invalid ❌

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext, user: String, reason: String) {
    /* Handle ban */
}
```

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext, user: User, reason: Reason) {
    /* Handle ban */
}
```

```kotlin
@Command("ban")
fun onBanCommand(ctx: CommandContext, member: User, reason: String) {
    /* Handle ban */
}
```

### Command context

The `CommandContext` is a replacement for the `GuildSlashCommandEvent`. Actually it is a `GuildSlashCommandEvent`, because
it extends this class. So this is basically just your event parameter with some extra fields. Use this to get other information
than the parameters such as the current guild, the member who executed the command or the channel in which the command got
executed.