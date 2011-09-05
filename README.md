Little Droid Physics
==================================================
By Emir Hasanbegovic
emir@emirweb.com


Preface:
--------

This report assumes that the Little Droid Creator report was read and understood in its entirety. (Found below)

High Level:
-----------

The idea behind droid physics was to offer a simple physics environment that would aid early high
school students to understand some of the properties of physics. In particular, acceleration, projectile
motion and Friction.

User interface:
---------------

The user interacts with simple and familiar android features to run the individual simulations. The
simulation does not include any interaction and provides accurate results that the user is expected to
predict based on the given information. The given information is a question and the input is a number
or several that will dictate the start values of certain pieces of the simulation.

Simulation:
-----------

The simulation is running on the Little Droid Engine which takes care of collision detection and
acceleration and velocity for all items involved in the simulation. This project has made the additions
of acceleration and velocity control through the engine and its objects.

Details:
--------

Acceleration is being tracked discretely. Each MobileObject starts the simulation with a starting
velocity and a starting acceleration. At each interval the object's velocity is increased by the
acceleration.

v(i) = v(i -1) + (delta)t * a

@Override
public float[] getDirectionVector(long timeChange) {
	lastInterval = timeChange;
	float ratio = getRatio(timeChange);
	velocity = Algebra.add(velocity,
	Algebra.multiplyVectorByConstant(gravity, ratio));
	float[] d = Algebra.multiplyVectorByConstant(velocity, ratio);
	return d;
}

v and a are vectors each containing an x and y component that dictates each items behaviour.
To replicate projectile motion, it was simple enough to break down the velocity into speed and
direction. Once the component vector for direction was established, we normalize it and multiply it by
the magnitude. This gives us our initial velocity and the above gravity algorithm can take charge the
rest of the way.

float xDirection = (float) (Math.cos(Math.toRadians(angle)));
float yDirection = (float) (Math.sin(Math.toRadians(angle)));
float[] direction = new float[] { xDirection, yDirection };
direction = Algebra.normalize(direction);
direction = Algebra.multiplyVectorByConstant(direction, power);

Friction was the last implementation and used the following discrete approach:

v(i) = v(i-1) * ( (-c *(delta)t )/ m + 1)

where c is the coefficient of friction and m is the mass of the object.

OpenGL:
-------

To provide a richer experience the renderer takes a background graphic. In OpenGL the background is
moved away from the camera and scaled to fill the screen. The renderer also considers all the images
associated with each individual item and displays it. Further optimization is available by grouping the
similar imaged objects together and drawing them. A similar optimization is available if the developer
who is building the World by adding objects with the same images in groups.

Algorithm:
----------

The collision detection needed to be improved due to the extra work being done by the renderer and the
simulator. When each object is added to the grid, it is hashed into a low collision hash table based on
integer rounding of the x and y position. A object to hash value reference is kept. Together these form
the GridHash Class which allows for get, add and remove functions in 0(1), assuming no collisions.
The following hash function guarantees low to no collisions:

private static final int gridSize = 100000;
private final LinkedList<WorldObject>[] gridSet = new LinkedList[gridSize];

private static int hash(int x, int y) {
	int hash = x * 100 + y;
	if (hash < 0)
	hash += 10000;
	hash = hash % gridSize;
	return Math.abs(hash);
}

This GridHash holds references to the WorldObjects and what grid boxes each of them falls into. So an
item that falls in 4 grid boxes in the world will have a reference in each of the grid boxes. This allows
for a localized collision comparison i.e. Only objects that are close to each other will then have the
collision calculations executed. This greatly reduces the number of collisions that we must consider at
each interval of the simulation.

After these optimizations and additions of the new features in the renderer and the simulation, the
frame rate averages 40 FPS on the NEXUS 1 device and higher on newer faster devices. The FPS is
limited to 60 FPS.

Coding:
-------

The developers can set up an activity by extending GameActivity. This activity will act exactly as the
Activity class if nothing involving the simulation is done. The developer has the option of creating a
World Class as well as any combination of older WorldObjects and the new Generic WorldObject.
When the World Object is built, simply call setWorld(world); to show the world as the activity's
background and setAnimate(true/false); to start/stop the animation. To reset the World simply set the
World again. Every thing else is taken care of.

Coding Acceleration and Gravity (Generic Class):
------------------------------------------------

The constructor for the Generic class takes a position, a starting velocity and an acceleration Vector.
public Generic(float[] acceleration, float[] position, float[] startVelocity);

Example:
	Generic anvil = new Generic(new float[] {0,EARTH_GRAVITY},
	new float[] { 3, height }, new float[] { 0, 0 });
	anvil.setImageId(15);
	anvil.setWaterMark("ANVIL");
	world.add(anvil);

In this case, we are choosing an image that we have added to the renderer previously and setting the
waterMark, which is used to uniquely identify the type or the individual object for comparison upon
collision detection.




Little Droid Creator
==================================================
By Emir Hasanbegovic
emir@emirweb.com


Introduction:
-------------

Little Droid Creator was designed to showcase the dynamic engine on which it runs. This document
will first describe at a high level what the game offers then we will dig down into the technical aspects
of the program and talk about how it offers the high level aspects.

High Level:
-----------

The idea of the application is to allow users to interactively create games/levels/worlds and play with
them without leaving the comfort of the hand-held device. The application provides a creation aspect
where the user is presented with a blank canvas and a selection of interactive objects with which to
create his world. The two main types of objects are Terrain and Mobile. Terrain objects are immobile
and Mobile objects are not. Each has its own set of properties Terrain objects are breakable or can
cause a winning state whereas Mobile objects are movable (react to touch events).
The game play is limited only by the player's imagination. The more individual tools become available
the more the game play grows.

Technical
---------

Structure:

The Code is divided into three sections. Android Activities, OpenGl renderer and the engine (World).
The Android activities handle the flow of the application and communicate the user's actions with the
renderer. The renderer communicates the necessary user actions to the engine in a way it understands as
it requests the world layout and draws it on the screen.

Renderer:

The renderer set up is straight forward, please consult the following [tutorial] (http://blog.jayway.com/2009/12/03/opengl-es-tutorial-for-android-part-i/) to set up a basic OpenGl set up.

OpenGl ES 1.0 was chosen so that it is easy to transfer to all android phones, but it does not have the
necessary support to retrieve the current transformation matrices or projection matrices to allow for
custom features, such as ray casting for touch events. Google offers MatrixTrackingGL, this class
overrides the internal matrix manipulation used by OpenGl and gives getters that allow for current
transformation matrix retrieval.

Activities:

GameActivity is the activity that holds all the information and control for the renderer and hence the
engine. All other activities that either run or create a game extend it and therefore do not have to worry
about the underlying inner workings of the OpenGLSurface that prove to be difficult to set up properly
on Android phones.

Engine:
-------

The engine was the core of the work, the difficulty lied in attempting to make the engine fully dynamic,
ie any shape and size of objects and any juxtaposition of objects, with the idea of adding more control
elements to the world and to is objects.
Rendering a frame:
Each time the renderer requests a new frame, the engine advances the frame by the number of
milliseconds that have passed since the previous frame, this way if the phone slows down or a slower
phone is used, the speed of the game-play does not suffer, but instead the game will seem to jump
frames.

Collision detection:
--------------------

The engine keeps a collection of immovable object in a grid format. The grid is held inside a
HashMap<Integer, HashMap<Integer, WorldObject>> which acts as a dynamic 2 dimensional array
that can grow as it needs to and is only as large as the number of elements on the grid. The mobile
objects consider their previous and next positions, map them to the grid and check only collisions
within the mapped grid. This brings collision detection down to O(1), on average.
The difficulty comes when we need to consider collisions of moving objects with moving obejcts,
because these change every frame, we need to create a structure for their inter collisions. Currently we
rebuild a combination of the above grid with the mobile objects added to it at every iteration of the
frame, which turns out to be very expensive. Some thought needs to go into when and how we can
consider movable object collisions and immovable object collisions separately.

Collision detection math:
-------------------------

Each object is a collection of lines that go in a clockwise direction, objects can have any number of
sides and can be either convex or concave. When considering a collision between two objects, each
point (p2) on object one is used to create a line segment in the direction (d, magnitude included in the
direction) of the movement of the object. The line segment becomes p1 + lambda * d = p1. Each such
line segment is then compared to each line representing object two. For each pair of lines the
intersection point is found, if there is one, then p1 + lambda * d = intersectionPoint is solved for
lambda. Because d includes magnitude, if lambda is between 0 and 1 then a collision has occurred. This
process is repeated with object one and two interchanged, the smallest lambda represents the closest
collision and is the one we consider. At that point the object is moved by lambda * d.

Reflection:

After a collision is detected, if lambda is smaller than 1 then the object still has some inertia that needs
to be considered. The reflection vector is calculated by finding a vector that is theta radians away from
the normal of the line with which it is colliding and pointing away from the line. Once the reflection
vector is calculated, the collision detection method is called recursively with the reflection vector set as
the new direction vector, this is repeated until no collision is detected or the magnitude of the direction
vector is approximately 0.

Dynamic collision detection:

The issue that arises is that the objects may collide with right angles or several objects at once and the
movable object needs to decide which lines to reflect off of and which ones not to. The current
algorithm saves all the collisions that are calculated and considers only the ones that have the same,
lowest, lambda value. If there is only one, then it simply reflects on it and continues, otherwise we take
all the points that we have collided with and create lines from them, we then compare these lines to the
lines that we have collided with, if their direction vectors are the same, we add them (the lines that we
have collided with, not the ones we have just created) to a collection of lines that we will consider and
if they are not then we discard them, this helps catch a combination of squares (as an example) side by
side by not considering the lines that would act as normals to the outer most lines, but instead just
considering the one line they form. Finally once all the considered lines are collected, we then only use
a set of the direction vectors, no repeats, and we reflect off of all of those direction vectors by reflecting
off of them individually and adding their results, this gives us the correct reflection direction, we then
normalize it and multiply it by the magnitude of the reflection/direction vector we were considering to
begin with. We then call collision detection recursively.

Issues:

The biggest issues that still arise are due to floating point arithmetic, rounding errors, where some
lambdas undeservingly get priority over others and the reflections do not look organic. Similarly some
objects get stuck in walls due to the same issues.

Programming:

The World is the engine, each object in the world is a WorldObject interface. There are Terrain interface
objects that do not move and have terrain like properties, breakable and winning (state). The Terrain
interface was mostly created in an attempt to minimize the collision detection overhead by having
object a grid structure that would not change from frame to frame. There are also Mobile interface
objects that have properties such as movable. These are set up in such a way that we may now begin
creating custom objects either dynamically or by hard coding them, and the World engine will handle
them very gracefully. This, along with the full polygon collision detection, is used to allow for a fully
dynamic engine that can later be extended to a full game maker where the world has as few limitations
as possible.

