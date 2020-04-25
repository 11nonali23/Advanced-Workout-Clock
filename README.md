# Advanced Workout Clock

This Android Application is meant to generate a clock with some features to make any kind of timed activity more efficent, and to plot the resulting data.

## Features of the app

* The user can create an activity for the chronometer and store the timings he did for the activities.

* The user can create an activity for the timer and schedule one or more timers that are also divided into work and rest.

* I will plot the data stored for chronometer and for timer into some graphs. The graphs types are not chosen yet.

## Front-End

The front end is done with material features and styles for Android.

The app supports swipe and drag with RecyclerView and it has a NestedScrollView in the timer acivities tab. 

## Back-End

The back-end was thought to use MVP pattern. However, since the app is created with viewpgaer and fragments, it's not a good idea to attach a 
stateless (on Destroy) Presenter to a stateful (on Destroy) Fragment. A solution to make the code readable and testable will be find soon.
