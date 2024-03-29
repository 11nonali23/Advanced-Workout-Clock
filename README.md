# Advanced Workout Clock

This applications was developed during Covid19 pandemic to encourage people triying HIIT workouts at home. This app features an interval timer with high personalization and a chronometer with charts to track performances.
The app is offically on the play store [Link to app](https://play.google.com/store/apps/details?id=com.andrea.advanced_workout_clock)

## Basic features of the app

* The user can create an activity for the chronometer and store the timings he does for the activities. The timings are visible and deletable.

* The user can create an activity for an interval timer and schedule the timings to do, that are also divided into work and rest. The interval timer can be stopped, paused, resetted and fast forwarded. 

* The data stored for the chronometer and for the timer will be plotted into line charts. 

* The app support 4 languages: English (default), France, Spanish, Italian.

* The app supports different screen size: normal, large, x-large
  
  ## Front-End

The front-end is made by following [material design guidelines](https://material.io/develop/android/) for Android.

You can check a front-end presentation here: https://www.youtube.com/watch?v=GXI3JSY52rQ

* The application supports [ViewPager 2](https://developer.android.com/jetpack/androidx/releases/viewpager2) to show the fragments.

* The item list of the timer supports **swipe and drag callbacks** with RecyclerView and it has a customized [NestedScrollView](https://developer.android.com/reference/androidx/core/widget/NestedScrollView) in the timer acivities tab to optimize the space taken.

* The timer and the chronometer are made using [Material Progress Bar library](https://github.com/zhanghai/MaterialProgressBar)

* The charts are made using [MPChart library](https://github.com/PhilJay/MPAndroidChart)

![chronometer front end](images/chrono_front_end.png) ![timer front end](images/timer_front_end.png)

![timer activities front end](images/timerActivities_fron_end.png) ![charts_front_end](images/charts.png)

## Back-End

The interval timer uses the **Shared Preferences** of the phone in order not to lose the timings already done. 

Every part of the application uses **MVP pattern** in order the separate the development. This make every part testable and highly flexible: a programmer can implement the database ingoring the other components.
Every component of the pattern implements the interface defined in the contract package.

Since I attach a presenter to a fragment, the **presenter need to be almost stateless** beacuse they are on different layers of architecture and lifecycle.
The only un-stateless parts of the presenter are connected to the db.

![MVP sequence_diagram](images/MVP_sequence.png)

### Presenter pseudocode

```
override fun presenterExapmle(args)
{
    if (model == null)
    {   view.notifyUserInternalError(); return   }

    if (checkConditions(args)== ERROR)
    {    view.displayTheError(); return }

    doTheLogic()

    model!!.interactWithModel()

    view.displayResults("R.string.SomeResultString")
}
```

### Database Design Document

![Database Design Doc](images/DB_DOC.jpg)
