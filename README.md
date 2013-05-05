Vexis - Android Client
=====
#####Overview
This is an Android application.

#####Main Goals
* Maintain a solid application that will communicate with a user's websites and their associates.

* Maintain a particular level of professionalism to ensure this application's usage in the business world.

Usage
=====
#####Overview
This is a simple process and will remain as such if instructions are followed carefully.

#####Compilation
This project can be compiled using Eclipse and the lastest SDK (Currently API 17) from the ADT Bundle hosted on Google's website.

#####Exporting & Signing
This project can only be signed, exported, and uploaded to Google Play by the Versidyne Development team.

#####Installing Unsigned
This project can be installed, unsigned, by anyone who wishes to modify or compile this code.  The receiving device must have Non-Market apps enabled in their settings in order to do so successfully.

System Roles
=====
#####Overview
The roles that follow are separated through various Activities and Fragments.  Each role will have its format and place in the order of the system as it runs.  The role will dictate where a particular feature will need to go and from there we can maintain quite a vast sense of universal ability to expand onto other mobile applications.

#####Extensions
Extensions will involve a top level action enabled across the board for all Activities and Fragments.

```java
//Sample Format

// Put text in fragment
switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
	case 1:
		dummyTextView.setText("This is where a user will be able to set up who they're associated with, similar to a buddy or friend's list.");
		break;
	case 2:
		dummyTextView.setText("This is where all GPS data will be displayed and have the ability to be saved to your account.");
		break;
	case 3:
		dummyTextView.setText("This is where the a user will be able to message other users, privately.");
		break;
	case 4:
		dummyTextView.setText("This is where a user will be able to see all notifications associated with their account or website.");
		break;
}
```

#####Libraries
These are files that are available throughout the system at any time.  They work in an object oriented method and contribute in common and general ways (i.e. database interfacing, markup languages, etc).  These files are extremely flexible and should be edited on a common basis.

Note: The loading of these files is going to be more nested in the coming versions.  We will be loading data in namespaces, and this will go into various types of a hierarchy, similar to that of the widely known SplClassLoader.php file.

#####Plugins
Plugins will involve a bottom level action enabled particularly within Activities or Fragments that call for them, which can involve multiple injections.

```java
//Sample Format

// Put text in fragment
switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
	case 1:
		dummyTextView.setText("This is where a user will be able to set up who they're associated with, similar to a buddy or friend's list.");
		break;
	case 2:
		dummyTextView.setText("This is where all GPS data will be displayed and have the ability to be saved to your account.");
		break;
	case 3:
		dummyTextView.setText("This is where the a user will be able to message other users, privately.");
		break;
	case 4:
		dummyTextView.setText("This is where a user will be able to see all notifications associated with their account or website.");
		break;
}
```

Credits
=====
This project has always been built by only one programmer, Alex Gurrola.  One day he hopes this system will no longer require his influence to progress without feature creep.
