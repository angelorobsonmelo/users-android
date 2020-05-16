package br.com.angelorobson.usermvi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.angelorobson.usermvi.utils.ActivityService

class Activity : AppCompatActivity() {

    private lateinit var activityService: ActivityService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity)
        activityService = applicationContext.component.activityService()
        activityService.onCreate(this)
    }

    override fun onDestroy() {
        activityService.onDestroy(this)
        super.onDestroy()
    }
}
