# SOURCERISE

## Create a new project
- Create a new repository on Github, using template "SourceRise"
- Open it in Android Studio
- Rename package in `AndroidManifest.xml` and `app/build.gradle`
- Delete package `backup` in source directories
- Delete Package `ui/catalog`
- Delete all layout files starting with "backup_"
- Delete file `google-services.json
- clean package `util`
- Commit and tag

## Getting started

1. Import the project in Android Studio :
    - File => Open
    - Select the project directory

2. Install [ktlint](https://github.com/pinterest/ktlint) .
You can use the command :
    
    `curl -sSLO https://github.com/shyiko/ktlint/releases/download/0.31.0/ktlint &&
  chmod a+x ktlint &&
  sudo mv ktlint /usr/local/bin/`
    
    On macOS (or Linux) you can also use brew : `brew install ktlint`.
  
3. Setup the [Ktlint](https://ktlint.github.io/) linter tools with `./gradlew setupProject`
    -  Configures intelliJ code formatter
    -  Adds a git commit prehook to check code format with ktlint
Note : If you use sourcetree, to ensure it works with ktlint, add export PATH=/usr/local/bin:$PATH in .git/hooks/pre-commit

4. Run the app!

5. To run tests and get an unified coverage report :
    - double click on task name like `jacocoTestReportMockDevDebug`, category `reporting`
    - from terminal, run `./gradlew clean jacocoTestReportMockDevDebug`  
    The report is located at `app/build/reports/jacoco/jacocoTestReportMockDevDebug/html/index.html`

## Environments configuration
#### Product flavors:
Build Variants names are composed by three words :
- The connection status : 'mock' or 'connected
- The environment : 'Dev' or 'Staging' or 'Prod'
- The compilation mode : 'Debug' or 'Release'

By default, there are 4 Build Variants : 
- mockDevDebug
- connectedDevDebug
- connectedStagingRelease
- connectedProdRelease

You can change these default build variants by commenting/uncommenting some of these lines in app/build.gradle :
```
def needed = variant.name in [
                'mockDevDebug',             // for development without server
                //'mockStagingDebug',             // for development without server
                //'mockProdDebug',             // for development without server
                'connectedDevDebug',        // for development with server dev
                //'connectedDevRelease',        // to build a dev version with bitrise
                //'connectedStagingDebug',    // for development with server preprod
                'connectedStagingRelease',    // to build a preprod version with bitrise
                //'connectedProdDebug',     // for local builds before beta release
                'connectedProdRelease',     // for deploy to play store with bitrise
        ]
```

## Git workflow

### Branches

1.  At least 2 branches should be used: `master` and `staging`.
    - "master" is the  branch on the store _(production)_
    - "staging" is the last branch delivered to the client _(pre-production)_
2.  Then, a branch should be created for each feature or sprint.
    Features branches' names should be named after JIRA issue ticket number, and a short description of the issue.
    It is good to also prefix it by "sX" for "sprint X". Do not write 'sprint1' inside the name of a feature branch (it might confuse some checks on git or bitrise).
    - Ex : s1-ARP381_fix-border-yellow-button
3.  Before commiting, please run the command `./gradlew ktlintFormat` to run the linter on your code.
4.  Push at least each day, on your feature branch.
    - Documentation for smart commits: [https://confluence.atlassian.com/fisheye/using-smart-commits-298976812.html](https://confluence.atlassian.com/fisheye/using-smart-commits-298976812.html)
5.  Create a pull request before merging the feature branches.
6.  At the end of each sprint :
    - Merge sprint branch into staging branch
    - Tag the last commit

### Tags

The tags should follow the following rules :
- The Major should only be incremented if the product faces considerable changes, and the sprint number is reset to 1. It normally starts at 1
- The Minor is the number of the sprint
- The Build Revision can be incremented for any fixes delivered to the client

For example, if this is the third revision of the sprint 4, the tag should be 1.4.3 :

`git tag -a 1.4.3 9feeb02 -m "Fixed border yellow button"`

### Versioning

Automatic versioning from Git tags is used on this project.
 
If we create a tag "1.2.0" somewhere, every builds' versionName will be "1.2.0" suffixed with commit hash, and versionCode equal to the list of tags on this git repository.
If the current commit is the one pointed by the last tag, then the commit hash is omitted. 

See `app/build.gradle` configuration for more details. 

### Workflow

Here is a graph summing up the git branches and introducing the typical workflow on Bitrise :
```
        GIT BRANCHES             |    BITRISE WORKFLOW
                                 |
feature1  feature2   feature3    |
     \       |       /           |
      \      |      /  _ _ _ _ _ | _ _ _  dev           
       \     |     /             |
        \    |    /              |
          sprintX                |
             |   _ _ _ _ _ _ _ _ | _ _ _ staging 
             |                   |
          staging                |
             |   _ _ _ _ _ _ _ _ | _ _ _ prod
             |                   |
         production              |
         
``` 
The three Bitrise workflows (`dev`, `staging` and `prod`) are triggered automatically according to this graph.

The fourth Bitrise workflow (`check`) can be be launched manually anytime to run the tests.

## Deploy

### Bitrise CI

Bitrise is used to check merged code and build new releases. 
- Each push to staging branch should trigger a build
- Each push to master branch should trigger a build and deploy to Play Store.
Don't forget to tag before pushing to master branch, so that the app is ready to be deployed on the store with its 'versionCode' incremented.
Don't forget to run Ktlint, Detekt and Jacoco before every merge.

### App signing & deploying to Play Store

Prerequisite: be a member of the project on [https://play.google.com/apps/publish](https://play.google.com/apps/publish).

1. Generate Signed Bundle (`.aab` file), using provided keystore under `keys` folder. Credentials are provided in `signing.gradle` file like following:
```
appSigning = [
        storeFilePath: "./keys/appstud.jks",
        storePassword: "appstud",
        keyAlias     : "appstud",
        keyPassword  : "appstud"
]
```
We use AAB format because Play Store will then generate smaller APK files for users to download.
More on this on [https://developer.android.com/guide/app-bundle/](https://developer.android.com/guide/app-bundle/).

The keystore provided here is only an upload signature file, and application is then re-signed by Google. More on this at [https://developer.android.com/studio/publish/app-signing#app-signing-google-play](https://developer.android.com/studio/publish/app-signing#app-signing-google-play).

Be sure to compile a `connectedProdRelease` variant of this app to use production server endpoints.  

2. Upload this `.aab` archive to Play Store, as a new production version. Google Play Store will then verify your upload signature and sign with its own key.
3. Verify and deploy!
 
## Quality

### Testing

#### Libraries

Espresso and Mockito are used to test the app.

#### Build variant

Tests should always be run under :
 
- `dev` environment (safer)
- `debug` buildtype (release can't be used for instrumented tests)
- `connected` mode (we should not use mocked data to test the network)  

**Therefore, the following variant has to be used to run the tests :**

`connectedDevDebug`

### Quality tools

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

#### Jacoco

Android Studio features a code coverage tool but it works for unit tests only.  

We use Jacoco to create a unified report on unit tests and instrumented (UI) tests.

Specific gradle tasks are set for each application variant so you only have to run `jacocoTestReport<ApplicationVariant>` gradle task to generate a code coverage report.

`Sonarqube` gradle task is also configured to run this task before sending data.  

#### Sonarqube

We use Sonarqube as a dashboard for all quality metrics.

Beware that we can only send data for one applicationVariant at once to Sonarqube, so choose your variant wisely, in `tools/sonar-jacoco-multi-flavors.gradle` file. 

#### KDoc - Dokka

All functions and classes must be commented in the following files :
- Package Util
- Package Repository
- All ViewModel classes (which should always end by "...ViewModel.kt")

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
For this project, it should be superior to 70%

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

**LCE - Load-Content-Error**

For all screens where data are loaded from a server, LCE pattern shall be implemented:

- No modal dialog – UI shall not be blocked
- Loading animation replaces content while server request is being performed
- Once data are gathered, content is displayed
- An error replaces content if data have not been loaded, network is unavailable or any other case

Error shall be contextualised i.e. it shall display the cause of the failure. Best is to display error message returned by the server when available. Moreover, an action shall be available to allow user to load data again.

To use this pattern, new Fragment must be sub-class of `BaseLceFragment` 

**Packages structure**

- `domain` communication between UI and data
    - `domain.service` interfaces describing available services for UI. These services provide network communication, database storing, computing...
    - `domain.entity` models used by UI layer  
- `data` data layer
    - `data.db` database management for persistent data
    - `data.network` network management: API, models…
    - `data.repository` actual implementation for data services
- `di` dependency injection
- `ui` where one package represents a function/screen where all related classes shall be grouped: views (fragments and activities), viewmodels, adapters…


UI shouldn't use any direct class from `data` package. This for separating concerns, and for testing purposes.

Therefore, UI classes only use Interfaces described under `domain.services` package.
Real implementation is provided by Repositories under `data.repository` package.
Implementations are bound to services using Dependency Injection.

We tend to separate domain entities from data entities. Domain entities are the ones used by UI and ViewModels logic. Data entities are only API wrappers, used for parsing data before/after network requests.

If there is no need to have separate objects (yet), we put the entity under the domain package.
