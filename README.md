cricket-scorer
==============

[![javadoc](https://javadoc.io/badge2/com.danielflower.crickam/cricket-scorer/javadoc.svg)](https://javadoc.io/doc/com.danielflower.crickam/cricket-scorer) 

This is a java library for keeping score in cricket matches. It does not provide any UIs for input or rendering,
but rather just provides a model to provide information such as:

* Details about a cricket match (including tests, limited overs, and custom types)
* The innings in a match
* Details of each batter's innings
* Details of partnerships
* Bowling information, broken down into spells

Most objects have a [list of all the balls](https://www.javadoc.io/doc/com.danielflower.crickam/cricket-scorer/latest/com/danielflower/crickam/scorer/Balls.html)
associated with the object, along with the [score](https://www.javadoc.io/doc/com.danielflower.crickam/cricket-scorer/latest/com/danielflower/crickam/scorer/Score.html),
which records detailed information such as balls faced, types of runs (boundaries vs singles vs dots etc), wickets, and extras.

Dependency
----------

````xml
<dependency>
    <groupId>com.danielflower.crickam</groupId>
    <artifactId>cricket-scorer</artifactId>
    <version>RELEASE</version>
</dependency>
````

Using the API
-------------

To build up a model of a cricket match, a [MatchControl](https://www.javadoc.io/doc/com.danielflower.crickam/cricket-scorer/latest/com/danielflower/crickam/scorer/MatchControl.html)
object is created, and then fed a series of events.

All the events have builders, and the [MatchEvents](https://www.javadoc.io/doc/com.danielflower.crickam/cricket-scorer/latest/com/danielflower/crickam/scorer/events/MatchEvents.html)
class contains a convenient set of methods to get access to all the built-in builders.

The following example creates a test match, starts an innings, and bowls an over:

````java
MatchControl control = MatchControl.newMatch(
    MatchEvents.matchStarting(MatchType.ODI).withTeams(ImmutableList.of(nz, eng))
);

control = control.onEvent(MatchEvents.inningsStarting().withBattingTeam(nz))
    .onEvent(MatchEvents.overStarting(eng.battingOrder().get(10)))
    .onEvent(MatchEvents.ballCompleted("0"))
    .onEvent(MatchEvents.ballCompleted("0"))
    .onEvent(MatchEvents.ballCompleted("4"))
    .onEvent(MatchEvents.ballCompleted("W").withDismissal(DismissalType.BOWLED))
    .onEvent(MatchEvents.batterInningsStarting())
    .onEvent(MatchEvents.ballCompleted("1"))
    .onEvent(MatchEvents.ballCompleted("2"))
    .onEvent(MatchEvents.overCompleted());

System.out.println(AsciiScorecardRenderer.toString(control));
````

The output of this program is as follows:

````
NEW ZEALAND vs ENGLAND
======================

ODI

New Zealand Innings (50 overs maximum)
--------------------------------------

BATTER                                                R   M   B 4s 6s     SR
C Munro              b Anderson                       4       4  1  0  100.0
M Guptill            not out                          2       1  0  0  200.0
T Seifert †          not out                          1       1  0  0  100.0
Extras                                                0
TOTAL                (1 wkts; 1.0 overs)              7

Yet to bat: C de Grandhomme, R Taylor, J Neesham, M Santner, T Southee, I Sodhi,
L Ferguson, B Tickner

Fall of wickets: 1-4 (Colin Munro, 0.4 ov)

BOWLING                 O   M   R   W   Econ 0s 4s 6s WD NB
J Anderson              1   0   7   1    7.0  3  1  0  0  0
````

To see the entire example including the configuration of the team line-ups, see
[ReadMeExample.java](https://github.com/danielflower/cricket-scorer/blob/master/src/test/java/e2e/ReadMeExample.java)

More examples:
* [full test match](https://github.com/danielflower/cricket-scorer/blob/master/src/test/java/e2e/TestMatchTest.java)
with [scorecard](https://github.com/danielflower/cricket-scorer/blob/master/src/test/resources/scorecards/sa-vs-eng-test-complete.txt)
* [full T20 match](https://github.com/danielflower/cricket-scorer/blob/master/src/test/java/e2e/T20.java)
with [scorecard](https://github.com/danielflower/cricket-scorer/blob/master/src/test/resources/scorecards/nz-vs-eng-t20i-complete.txt)

Accessing the generated model
-----------------------------

Although the examples above show an ascii scorecard, the main usefulness comes in traversing
the generated model.

To get the current state of the match, call `control.match()` which returns a 
[Match object](https://www.javadoc.io/doc/com.danielflower.crickam/cricket-scorer/latest/com/danielflower/crickam/scorer/package-summary.html)
which gives access to the completed and current innings of the match, which in turn
gives access to much more information about the match state.

You can also use the match control object to find a specific point in time in the match
and access the entire model as at the time of that event.

The following example finds the state of the match after the first single:

````java
MatchControl matchAfterFirstSingle = control.asAt(
    control.eventStream(BallCompletedEvent.class)
        .filter(b -> b.score().batterRuns() == 1)
        .findFirst()
        .get());
Score scoreAfterFirstSingle = matchAfterFirstSingle.currentInnings().score();
System.out.println("The team score after the first single was " +
    scoreAfterFirstSingle.teamRuns() + " runs for " + scoreAfterFirstSingle.wickets());
````

This prints the string `The team score after the first single was 5 runs for 1`.

Note that every match control object is immutable, so you can call `onEvent` on an historical
control instance which essentially forks the match at the given point of time.

This allows you to construct "what if" scenarios, or undo operations that were applied in error. 

Design decisions
----------------

* ***Everything is immutable*** so that things like the full state of the match can be
looked at, and matches can be forked easily.
* The only way to change the model is with events
* Any objects created are with builder classes, which are inner classes of the model object they create
