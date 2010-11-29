This application is an example in the tutorial:

"Understanding Android's Security Framework"

Presented by William Enck and Patrick McDaniel of the SIIS Lab at Penn State
enck@cse.psu.edu, mcdaniel@cse.psu.edu

Disclaimer: We do not claim that all security flaws have been removed from
these applications. They were developed to demonstrate specific example 
interactions that were the focus of the tutorial.

The suite of applications consists of the following (also the recommended
installation order).

1) FriendTracker

The FriendTracker application contains a Service component that monitors
fake friend locations. The FriendFinder class implements the fake location
tracking, and the nicknames included must correspond to those defined
in the FriendSeed application (described below). Also included is the
FriendProvider Content Provider, which stores the friend location data.
Finally, the FriendTracker service broadcasts an Intent when the phone
is near a friend. Use the emulator control in "manual" mode to send
a location.

2) FriendViewere

The FriendViewer application queries the FriendProvider in FriendTracker.
It displays a listing of all friends and can show locations on a Map.
Note, if you attempt to compile and run this application yourself, you
need to change the android:apiKey value in res/layout/map.xml, as it 
is currently tied to our debug key. See the SDK documentation for more 
details.

3) FriendSeed

The FriendSeed application simply seeds the phone with sample data.
Only run it once, or else you will have duplicates. To see the contacts
in the system Contacts application, use the menu button to show 
all contacts, and not just your "My Contacts" group.
