# Music Config
Configure how much a music track should be played in Minecraft.

## Features
- Set frequency of each music track grouped by context
- Search through every track by name and artist
- Preview randomly selected, context-specific, music
- Support for additional music from resource packs or other mods!

## How it works
Usually, when Minecraft plays background music, it selects a random music track to play. These tracks exist in a pool based on the current context (things such as your location, your gamemode, whether you're in a world at all, etc.). For example, when you visit a Swamp Biome Minecraft attempts to play music, it picks a random track from a list of three (_Aerie_, _Firebugs_ and _Labyrinthine_). (This is internally known as the sound event `minecraft:music.overworld.swamp` and is accessible with the /playsound command.)

**Music Config** allows you to set how often each track should be played in each context. You can select the frequency of each track, so that some tracks play more often than others, or you can disable some tracks entirely. In the previous example, the context would be "_overworld: swamp._" You could set the frequency of _Aerie_ to `Often`, _Firebugs_ to `Sometimes` and _Labyrinthine_ to `Never`. This would mean that _Aerie_ plays almost every time Minecraft attempts to play music in a Swamp, _Firebugs_ plays occasionally and _Labyrinthine_ never plays.

## How to use
1. Install the mod & start Minecraft
2. Go to the config screen (Options > Music & Sounds > Music Config)
3. Optionally, search for tracks by name or artist and/or collapse unimportant contexts
4. In a context, set the frequency of each track by adjusting the slider
5. Test the randomness of the music from this context, by clicking the play button (▶️) next to the context name
6. Click "Done" to save your changes (writes to file `.minecraft/music_config.json`)

Now you can enjoy your custom music experience in Minecraft!

![](assets\screenshot.png)
**Example:** setting play frequency per track from the "nether: crimson forest" context.


Here's a short summary of what every context in the game exactly represents:

| Context                              | Description                                                                    |
|--------------------------------------|--------------------------------------------------------------------------------|
| A context prefixed by "_overworld:_" | Represents a biome in the Overworld. Music plays when player is in that biome. |
| A context prefixed by "_nether:_"    | Represents a biome in the Nether. Music plays when player is in that biome.    |
| "_end_"                              | Music plays when player is in the End.                                         |
| "_creative_"                         | Music plays when player is in Creative mode.                                   |
| "_credits_"                          | Music plays during the game's credits sequence.                                |
| "_dragon_"                           | Music plays when fighting the Ender Dragon.                                    |
| "_game_"                             | A sort of fallback; music plays when no other context requirement is met.      |
| "_menu_"                             | Music plays on the Main Menu.                                                  |
| "_under water_"                      | Music plays when the player is submerged in water.                             |

## Report an Issue
Use GitHub's Issue Tracker to report Issues such as requests or bugs.