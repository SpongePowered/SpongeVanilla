Granite
======

An implementation of SpongeAPI directly on top of the vanilla Minecraft server.

Granite uses bytecode manipulation to make Minecraft's internal classes implement interfaces that describe those classes.
This lets us construct composites that stitch together functionality from multiple Minecraft classes without having
to depend on Mojang's code. The relations between Granite's interfaces and Minecraft's classes are defined in a configuration
file outside the code. This lets us potentially maintain compatibility with multiple Minecraft versions without having to
alter our code. Having no references to anyone else's code in Granite also means that there are no legal gray areas that could
lead to the project being taken down.

Download and usage
------
We provide pre-built jars on [our website](http://www.granitepowered.org/), so no experience with compiling is necessary,
but you can also compile it yourself assuming you have Git and Maven installed:

1. Clone our repository:

   `git clone https://github.com/GranitePowered/Granite.git`

2. Change directory to your local copy of Granite:

   `cd Granite`

3. Build the entire project:

   `mvn clean install -U`

4. Run the test script:

   `./test.sh`

Want to Help?
------
Join us on IRC: irc.esper.net **#granite**

If you'd like to contribute, make a pull request - we read and consider them all very carefully.
Should you decide to contribute to the project, please read our [contribution guidelines](https://github.com/GraniteTeam/Granite/blob/master/CONTRIBUTING.md).

You can also visit our website at http://www.granitepowered.org/ .

License (MIT)
-------
Copyright (c) 2014-2015 Granite Team

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
