Correspondence for Android
==========================

Correspondence is a collaboration framework. That means that it helps
people and devices to communicate with one another. A Correspondence
application running on one device synchronizes its local data store with
Correspodence applications running on other devices. These could all belong
to the same person, or to different people.

Correspondence Overview
-----------------------

With Correspondence, you get:
* Local device storage
* Synchronization
* Push notification
* User interface updates

This project is the Android client. Other clients include:
* WPF
* Silverlight
* Windows Phone
* ASP.NET MVC (yes, a web site is a Correspondence client)

You can compile your Correspondence model to any of these clients. The
apps that you develop against the common model will collaborate, even across
platforms. Write an Android, Windows Phone, and Web version of your app
and Correspondence will keep them all in sync.

Find more information at http://qedcode.com/correspondence

Building
--------

To build the project, you need to have Eclipse, the Android SDK, and the ADT
plugin. You can find instructions for getting set up here:

http://developer.android.com/tools/sdk/eclipse-adt.html

Once you have the tools, set up your workspace:

1. Create an Eclipse workspace outside of the folder structure where you pulled the code.
2. Add a new Android project to this workspace.
3. Choose the option "based on existing source code".
4. Name the project "query".
5. Uncheck the box "use the default location".
6. Enter the path to library/query within the source code.
7. After the project is created, edit the properties.
8. Select Android.
9. Check the "is library" box.

Repeat these steps for the remaining library projects. The next one to create
is "updatecontrols" located at library/updatecontrols, and then "correspondence"
located at library/correspondence. This time, there is one more step:

10. Inside the Android settings, add a reference to the "query" and "updatecontrols" projects.

The remaining projects are:
* correspondence_binary
    * library/correspondence_binary
    * correspondence, updatecontrols, and query
* honeydolist_model
    * examples/honeydolist/honeydolist_model
    * correspondence, updatecontrols, and query
* honeydolist
    * examples/honeydolist/honeydolist
    * correspondence, correspondence_binary, updatecontrols, query, and honeydolist_model

For that last one, do not check the "is library" box. Now right-click hoenydolist and Run As
Android Application.