Android Thread and AsyncTask Examples
===========

`aSyncTaskDemo` is an example using the aSyncTask class instead of threads.  It counts from 0 to 100 (by 5).  Note AsyncTask is depreciated in API 30+

`SimpleThreadDemo` (java) is the same example as aSyncTaskDemo, but uses threads and handlers.  Basically to see the difference in complexity between the two.

`SimpleThreadDemo` (kotlin) is the same example as aSyncTaskDemo, but uses threads and handlers.  Basically to see the difference in complexity between the two.

`SimpleThreadDemo2` is the same example as aSyncTaskDemo and SimpleThreadDemo, but uses the runOnUiThread method.

`ThreadDemo` (java) is example using threads in a more complex way, where an AsyncTask may not work.  It draws when the uses is touching the screen.

`ThreadDemo` (kotlin) is example using threads in a more complex way, where an AsyncTask may not work.  It draws when the uses is touching the screen.  NOTE: Kotlin does threading and locking in a different way, so while this example is a one to one translation, it is different.  Also the java to kotlin translate fails as well.

`eclipse/` has the examples for eclipse, no longer updated.  Otherwise the examples are for android studio.

---

These are example code for University of Wyoming, Cosc 4730 Mobile Programming course and cosc 4735 Advance Mobile Programing course. 
All examples are for Android.

