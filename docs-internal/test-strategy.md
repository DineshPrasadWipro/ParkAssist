# Testing strategy

Here is the Android reference regarding tests: https://developer.android.com/training/testing/fundamentals

## Tests pyramid

As stated in the previous article, we should prefer tiny and numerous unit tests.

We will categorize our tests with different annotations corresponding to each level:
- @SmallTest for small unit tests that run on the JVM. That should represent most of the tests in your project.
- @MediumTest for tests that run on the JVM but take a longer time (roughly 1 sec). They can be helpful to test comportment depending on Android framework behavior.
- @LargeTest for tests that run on emulator or device and thus take longer than 1 sec. They should only be functional tests that test User Stories on multiple screens.

We should have a lot of @SmallTest and a limited number of @LargeTest.


## Code Coverage

Android Studio comes with its own coverage report tool integrated to the IDE.
Unfortunately, the reports are quite limited and don't take into account all types of tests.

As we also need the test reports in our CI builds, we will favor Jacoco (Java Code Coverage) and use the corresponding gradle plugin.

TODO: How to expose our reports inside the CI ? Should we use SonarQube ?

In any cases, the global coverage should be close to 100% and most of it should be covered with unit tests.


## How to test ?

### Mocks

[Mockk](https://mockk.io/)

Mockk is a mocking library for Kotlin. We use it to replace both Mockito and PowerMock.
It allows to replace the behaviour of certain classes or components and verify that some methods have been called with the correct parameters.


### Injection

For now on we use [Koin](https://insert-koin.io/) internally but you are free to use the framework you prefer.
Other alternatives include [Dagger](https://dagger.dev/). This one generates code at compile time and is therefore more rapid. Unfortunately, it is also way more complex.
You could also use or rely on flavor builds only, or [Kodein](https://kodein.org/Kodein-DI/?6.3/android).


### On device

If you need to test complex scenarios on a device, your only option is Espresso. You can act on view elements and execute assertions directly on your views.
Please refer to the [corresponding documentation](https://developer.android.com/training/testing/espresso)


### AndroidX Test

Google recently introduced [AndroidX Test libraries](https://developer.android.com/training/testing/set-up-project). Among other things, it allows you to write test code, able to run both on the JVM with Robolectric or on the device with Espresso.
You can fin more documentation on this [just here](http://robolectric.org/androidx_test/) and [an interesting article here](https://medium.com/google-developer-experts/pushing-the-limits-of-androidx-test-3776ff249c71).


### What to test ?

Test EVERYTHING !


#### ViewModel

Mock the services.
Send data and verify that observers are notified.
These should only be small unit tests.


#### View

You will often need to use Android mocks.

In such cases, Robolectric can be a viable option.
If necessary, you can switch to Espresso but it is not the preferred way to go.


#### Activity/Fragment/Services

The rule of thumb is to simply mock every one of your dependencies and simply assert the behavior of your single component or architecture layer.

It can become tedious when using classes deeply linked to the Android framework.
In such cases, it is recommended to extract all the logic in a separate class that takes as parameters everything that has a dependency to the Android framework.
This part becomes easily testable, passing mocks as constructor arguments and the Activity (or Fragment or Service) itself only contains class instantiations and parameters passing.


#### Presenter

The presenter is a concrete example of the previous pain point.

If you use a presenter as we do (you absolutely don't have to), the easiest way is to simply mock both the view and the viewmodel.
You can then post values on the mocked viewmodel and check that your view reflects these changes.
You can also act on the view and verify that the correct methods are called on the presenter.
These should also simply be unit tests.


## Integration tests

To do black box testing of the whole application, you have to couple multiple techniques:

- Change the dependency injection and inject service mocks.
- Use Espresso to automate user actions and verify things on the screen.
- Act on the mocks to simulate service responses.

