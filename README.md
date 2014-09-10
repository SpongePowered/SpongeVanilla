Granite
======

Granite - A different interface for Minecraft 1.8+.

Note: This is _not_ Sponge!

The purpose of this project is to take a "hands off" approach to vanilla code.
That is, we _describe_ the code to our composite layer, without actually including any of it.
Access is gained by composite classes, reflection, proxy classes and interfaces, and classloaders.

Goals
------
The primary objective of the Granite project is simple: provide an open-source plugin interface for Minecraft that does not
contain or distribute any proprietary source code or other assets that belong to Mojang AB.  All access to server
internals is obtained via Java reflection and similar techniques.

The secondary objective is to provide a plugin system and API that uses the underlying interface to allow server
administrators to extend their server in any fashion they see fit.  The API will be as versatile and capable as we can
manage.  The topic of Bukkit compatibility comes up frequently - we are not established enough to make formal statements
as to the viability of Bukkit compatibility.  It is not a top priority at this stage of development.

This project is licensed under MIT, and takes a very different approach from present API and implementation options. As
such, it is slower to develop as there is very little reference material to base our work off of.  However, we have
already made good progress, and at this pace, will continue to do so.  We have implemented a unique model and mapping
system in order to minimize the amount of work to implement updates; so once the project reaches a more finished state,
maintenance should be a relatively simple affair.  Our hope is that this approach is a valid "future-proofed" option.

To-do list
------
- ~~Set-up~~
- ~~Command proxy~~
- **Plugin loader**
- **Command API**
- **Event system**
- API for worlds, items, players
- Essentials-style example plugin
- ??? (More to come)

Want to Help?
------
Join us in irc, on esper.net in channel **#granite**.

If you'd like to contribute, make a pull request- we read and consider them all very carefully.
Should you decide to contribute to the project, please read our contribution guidelines.

You can also visit our website at http://www.granitemc.org/ .

License (MIT)
-------
Copyright (c) 2014 Granite Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.