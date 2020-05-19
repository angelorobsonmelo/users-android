package br.com.angelorobson.usermvi.users

import android.content.res.Resources
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import br.com.angelorobson.usermvi.R
import br.com.angelorobson.usermvi.component
import br.com.angelorobson.usermvi.di.TestComponent
import br.com.angelorobson.usermvi.utils.FileUtils
import br.com.angelorobson.usermvi.utils.TestIdlingResource
import com.learning.fooddataviewer.utils.withRecyclerView
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class UsersListFragmentTest {


    private val mockWebServer = MockWebServer()
    val name = "Leanne Graham"
    val username = "Bret"

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
        mockWebServer.close()
    }


    @Test
    fun userSuccess_RecyclerView_CheckItems() {
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

        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.tvName)).check(
            matches(
                withText(name)
            )
        )

        onView(withRecyclerView(R.id.recyclerView).atPositionOnView(0, R.id.tvUserName)).check(
            matches(
                withText(username)
            )
        )


        IdlingRegistry.getInstance().unregister(idlingResource!!.countingIdlingResource)
        mockWebServer.close()
    }

    @Test
    fun errorEvent_textView_showErrorMessage() {
        val mockResponse = MockResponse()
            .setBody(FileUtils.getJson("json/users/users.json"))
        mockResponse.setResponseCode(400)
        mockWebServer.enqueue(mockResponse)
        mockWebServer.start(8500)

        val scenario =
            launchFragmentInContainer<UserListFragment>(themeResId = R.style.Theme_MaterialComponents_Light_NoActionBar)

        var resources: Resources? = null

        scenario.onFragment { fragment ->
            resources = fragment.resources
        }

        onView(withId(R.id.tvError)).check(matches(withText(resources?.getString(R.string.an_error_occurred))))

        mockWebServer.close()
    }


}