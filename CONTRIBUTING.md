# Contribution guidlines

## The basics
1. Find a task to work on.
2. Fork our repository on GitHub.
3. Create a new branch in your fork. Name it something relevant to the task.
4. Make your changes, and test them to be sure everything works as expected.
5. Commit your changes with a descriptive message.
6. Create a pull request on GitHub, cleary describing what your code does.

## The code
* No tabs. Indent with 4 spaces.
* No trailing whitespace.
* No multiple new-lines in a row.
* No CRLF line endings. Set git's "core.autocrlf" to true.
* No line length limit or midstatement new-lines.
* There needs to a be a new lines at the end of every file.

## How to Git
Once you've forked our repository on GitHub, you can clone your fork to your own machine to make changes:

`git clone git@github.com:YourName/YourFork.git`

The first thing you need to do is create a new branch to work in:

`git checkout -b my-new-branch`

Now it's time to make changes to the code. When you're done you need to stage the changes files:

`git add path/to/my-file.java`

And once all the files are staged you need to commit the changes:

`git commit -m "Description of changes goes here."`

Then push the committed changes to the new branch GitHub:

`git push origin my-new-branch`

You're now ready to create a pull request.

### Resolving pull request merge conflicts.
The first step is to add our repository so that changes can be pulled from it:

`git remote add upstream https://github.com/GraniteTeam/Granite.git`

Then make sure you are in the local master branch:

`git checkout master`

And pull the changes from our repository:

`git pull upstream master`

Push the new changes to your fork as well, so that you won't accidentally start working on old code in the future:

`git push origin master`

Switch back to your branch:

`git checkout my-new-branch`

Then start the rebase operation:

`git rebase master`

This will tell you that there are conflicts, and that the rebase cannot be completed. Use `git status` to see which files are in conflict. Open the files one by one in your favourite text editor to have a look at the conflicts. They will be marked like this:
```
<<<<<<< master
New code in master branch.
=======
Your code.
>>>>>>> my-new-branch
```
The goal is the leave the file exactly the way you want it to look. That means removing the marker lines (`<<<<<<<`, `=======`, and `>>>>>>>`), and manually merging the two chunks of code into a single block. Once all the conflicts are resolved, you can mark them as so:

`git add path/to/conflicted-file.java`

Then you can continue the rebase:

`git rebase --continue`

And finally you can push the new code to GitHub:

`git push -f origin my-new-branch`

Your pull request will now automatically be updated.

## Legal observations
By contributing to this source code repository, the contents of which are licensed under the MIT license, you are agreeing that your submission is compatible with the MIT license, and that you understand you are providing this source code to an open source, MIT licensed repository, and that you have all rights in regards to your submission to authorize its contribution to source code distributed under the MIT license.
