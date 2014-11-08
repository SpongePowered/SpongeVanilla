Granite
======

Granite - A different interface for Minecraft 1.8+.

The purpose of this project is to take a "hands off" approach to vanilla code.
That is, we _describe_ the code to our composite layer, without actually including any of it.
Access is gained by composite classes, reflection, proxy classes, interfaces, and classloaders.

**NOTE: These APIs may or may not be completely broken. They shouldn't be, but there's always a chance. Take care.**

Most development happens in the branches `patch`, `minor` and `major`, so don't think the project is inactive just because the last commit to `master` is a week old.

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

Usage
------
Granite is very easy to setup, assuming you have Git and Maven installed:

1. Clone our repository:

   `git clone https://github.com/GraniteTeam/Granite.git`

2. Change directory to your local copy of Granite:

   `cd Granite`

3. Build the entire project:

   `mvn install`

4. Run the test script:

   `./test.sh`

Windows users will have to do step 4 manually by obtaining a vanilla minecraft_server.jar, and launching the Granite jar from the command line.

You could also use one of our pre-built jars from [Jenkins](http://ci.flaten.it/view/Granite/).

To-do list
-----
See TODO.md

Want to Help?
------
Join us on IRC: irc.esper.net **#granite**

If you'd like to contribute, make a pull request- we read and consider them all very carefully.
Should you decide to contribute to the project, please read our [contribution guidelines](https://github.com/GraniteTeam/Granite/blob/master/CONTRIBUTING.md).

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
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
