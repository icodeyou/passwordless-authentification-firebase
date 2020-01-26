# PASSWORDLESS AUTHENTICATION WITH FIREBASE

## Description
PAF (Passwordless Authentication Firebase) is an open source Android application.
Its purpose is to connect the user with email or phone without asking him any password.
Get a look at the code and see how to implement Firebase Authentication.

The app is combining two providers that don't require any password :
- Email magic link
- Phone Number

A new user is forced to give his email when he signs up. 
The phone number is optional. This authentication method helps the user connecting faster.

## Become a contributor
Ask to be a contributor, and be added to the Firebase project.

## Getting started

Steps 2 and 3 are necessary if you want to access to the Firebase console

1. Clone the project

2. Rename the package (ex : `com.test.login`)
Be careful not to leave any text `com.hobeez.passwordlessfirebase in the project.

3. Configure Firebase
- Get SHA1 and add your project to Firebase, using SHA1 and the new package name.
- Enable email, email link and phone authentication.
- Configure dynamic link in Firebase console, and replace `https://passwordlessfirebase.page.link` with your URL in `AndroidManifest` and `AuhUtil`

4. Install [ktlint](https://github.com/pinterest/ktlint) .
You can use the command :
    
    `curl -sSLO https://github.com/shyiko/ktlint/releases/download/0.31.0/ktlint &&
  chmod a+x ktlint &&
  sudo mv ktlint /usr/local/bin/`
    
    On macOS (or Linux) you can also use brew : `brew install ktlint`.
  
5. Setup the [Ktlint](https://ktlint.github.io/) linter tools with `./gradlew setupProject`
    -  Configures intelliJ code formatter
    -  Adds a git commit prehook to check code format with ktlint
Note : If you use sourcetree, to ensure it works with ktlint, add export PATH=/usr/local/bin:$PATH in .git/hooks/pre-commit

## Environments configuration
#### Build Variants:
Only two build variants are used for this project :
`debug` and `release`

## Git workflow

### Branches

1.  At least 2 branches should be used: `master` and `staging`.
    - "master" is the  branch on the store _(production)_
    - "staging" is the last branch delivered to the client _(pre-production)_
2.  Then, a branch should be created for each feature.
    Features branches' names should be named after the issue ticket number on Github, and a short description of the issue.
    - Ex : #23_fix-border-yellow-button
3.  Before commiting, please run the command `./gradlew ktlintFormat` to run the linter on your code.
4.  Push at least each day, on your feature branch.
5.  Create a pull request before merging the feature branches.
6.  When a feature is finished, tag the last commit after merging the branches

### Tags

The tags should follow the following rules :
- The Major should only be incremented if the product faces considerable changes, and the sprint number is reset to 1. It normally starts at 1
- The Minor can be incremented when a feature is added
- The Build Revision can be incremented for any fixes delivered to the client

For example, if this is the third revision of the 4h feature, the tag should be 1.4.3 :
`git tag -a 1.4.3 9feeb02 -m "Fixed border yellow button"`

### Versioning

TODO : MAKE SURE THIS WORKS

Automatic versioning from Git tags is used on this project.
 
If we create a tag "1.2.0" somewhere, every builds' versionName will be "1.2.0" suffixed with commit hash, and versionCode equal to the list of tags on this git repository.
If the current commit is the one pointed by the last tag, then the commit hash is omitted. 

See `app/build.gradle` configuration for more details. 
 
## Quality

### Continuous Integration
No CI tool is used at the moment.


### Testing
No testing has been made at the moment. Espresso and Mockito can be used to test the app.

#### Code formatting - Ktlint

Again, make sure ktlint is installed and its rules are applied to your IDE. 
To apply those rules and the git commit pre-hook, run gradle task `setupProject` 

#### Detekt 

Detekt is a static code analysis tool for the Kotlin programming language. 
It features lots of metrics like code smell analysis or complexity report. 

Sonarqube is configured to use Detekt as its code analysis tool so you don't have to do anything.
You can also run `detekt` gradle task locally. 

Detekt rules are configured in `tools/default-detekt-config.yml`. 

[More info on its github page](https://github.com/arturbosch/detekt)

#### KDoc - Dokka

TODO : TEST SCRIPT COVERAGE

Functions and classes should be commented in the following files :
- Package Util
- Package Repository

They are commented with KDoc, which is similar to javadoc :
https://kotlinlang.org/docs/reference/kotlin-doc.html

To generate a HTML document which describes all these classes, we use a tool called Dokka :
https://github.com/Kotlin/dokka

Dokka is setup with the following lines in `/build.gradle` :
```
buildscript {
    apply from: "./versions.gradle"

    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.4.2'
    }
}

classpath "org.jetbrains.dokka:dokka-android-gradle-plugin:$versions.dokka"
```

And in `/app/build.gradle` :
```
repositories {
    jcenter() // or maven { url 'https://dl.bintray.com/kotlin/dokka' }
}

apply plugin: 'org.jetbrains.dokka'

dokka {
    outputFormat = 'html'
    outputDirectory = "$buildDir/dokka"
    includeNonPublic = true
    skipEmptyPackages = true
    skipDeprecated = true

    packageOptions {
        prefix = "com.dupuis.webtoonfactory.util"
        reportUndocumented = false
    }

    packageOptions {
        prefix = "com.dupuis.webtoonfactory.data.repository"
        reportUndocumented = false
    }
}
```

To generate the documentation, just type in a terminal : `./gradlew dokka`

To get the coverage of the documentation (only for the files that are specified 
at the beginning of this paragraph), please run the script : `tools/koverage.sh`

You will have access to the documentation coverage of each file. 
The last line if the total documentation coverage.

## Settings

### Android Studio

In the preferences of Android Studio, make sure to have the following settings :

For KTLint :
- Editor -> Code style 
    - Untick "Detect and use file indents for editing"

- Editor -> Code style -> Java
    - Tab "Wrapping and Braces" : Choose "Wrap if long" everywhere
    - Tab "Tabs and indents" : Set "Continuation indent" to 4
    - Tab "Imports" : Tick "Use single class import" and remove all exceptions

- Editor -> Code style -> Kotlin
    - Tab "Wrapping and Braces" : Choose "Wrap if long" everywhere
    - Tab "Tabs and indents" : Set "Continuation indent" to 4
    - Tab "Imports" : Tick "Use single class import" and remove all exceptions
    
For Detekt :
- Editor -> General 
	- Tick "Ensure line feed at file end on Save"

## Technical stack

### MVVM

This project uses Android Architecture ViewModels ([official doc](https://developer.android.com/topic/libraries/architecture/viewmodel)).

In a few words, this is used to:

- separate some business logic (retrieving data from API, computing some results) from UI code (colors, layout, animations).
- keep some data persistent across fragments under the same activity. One of the best example of that is in the Pickup Module.

### Koin

Dependency Injection is provided by Koin.

When creating a new Fragment, declare its viewModel in di.ViewModels.
ui.myScreen.MyViewModel
```
class MyViewModel: ViewModel() {}
class MyViewModelWithParameter(val myKoinClass: MyKoinInjectedClass) {}
```
di.ViewModels
```
val viewModelModules = module {
    viewModel { MyViewModel() }
    viewModel { MyViewModelWithParameter(get()) }
}
```
ui.myScreen.MyFragment
```
class MyFragment: Fragment(), KoinComponent {
  val myViewModel: MyViewModel by viewModel()
}
```

When creating a new Service and Repository, declare them in di.NetworkModule.
data.repository.MyRepository
```
class MyRepository(retrofit: Retrofit): MyService {
  private val myApi: MyApi = retrofit.create(MyApi::class.java)
  override fun myFun(): Single<MyDomainEntity> {
    return myApi.myFun()
  }
}
```
di.NetworkModule
```
val networkModule = module {
  single<MyService> { MyRepository(get()) }
}
```

### UI

The UI will be designed to fit in a screen with a dimension of at least 4.0 inches.

### Architecture

**Packages structure**

- `domain` communication between UI and data
    - `domain.service` interfaces describing available services for UI. These services provide network communication, database storing, computing...
    - `domain.entity` models used by UI layer  
- `di` dependency injection
- `ui` where one package represents a function/screen where all related classes shall be grouped: views (fragments and activities), viewmodels, adapters…

Therefore, UI classes only use Interfaces described under `domain.services` package.
Real implementation is provided by Repositories under `data.repository` package.
Implementations are bound to services using Dependency Injection.

We tend to separate domain entities from data entities. Domain entities are the ones used by UI and ViewModels logic. Data entities are only API wrappers, used for parsing data before/after network requests.

If there is no need to have separate objects (yet), we put the entity under the domain package.

## Credentials

The password for the keystore is : `opensource`
