package br.com.angelorobson.usermvi.users

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.component
import br.com.angelorobson.usermvi.di.TestComponent
import br.com.angelorobson.usermvi.utils.FileUtils
import br.com.angelorobson.usermvi.utils.TestIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class UsersListFragmentTest {


    private val mockWebServer = MockWebServer()

    @Test
    fun views() {
        val mockResponse = MockResponse()
            .setBody(FileUtils.getJson("json/users/users.json"))
        mockResponse.setResponseCode(200)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario =
            launchFragmentInContainer<UserListFragment>(themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar)

        var idlingResource: TestIdlingResource? = null

        scenario.onFragment { fragment ->
            idlingResource =
                ((fragment.activity!!.component as TestComponent).idlingResource() as TestIdlingResource)
            IdlingRegistry.getInstance().register(idlingResource!!.countingIdlingResource)
            idlingResource!!.increment()
        }

        onView(withId(R.id.loadingIndicator)).check(matches(not(isDisplayed())))
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))

        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
    }


}