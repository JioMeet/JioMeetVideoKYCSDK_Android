package com.example.demo

import android.app.UiModeManager
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.demo.view.ui.CoreLoginView
import com.example.demo.viewModel.AppViewModel
import com.jiomeet.core.CoreApplication
import org.jio.kyc.helper.StringConstants

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
        CoreApplication().recreateModules(this)
        setContent {
            CoreLoginView(onJoinMeetingClick = {
                val joinKycRoomIntent = Intent(this, JoinKYCRoomActivity::class.java)
                joinKycRoomIntent.putExtra(StringConstants.MEETING_ID, viewModel.loginState.value.meetingID)
                joinKycRoomIntent.putExtra(StringConstants.MEETING_PIN, viewModel.loginState.value.meetingPin)
                joinKycRoomIntent.putExtra(StringConstants.GUEST_NAME, viewModel.loginState.value.meetingPin)
                startActivity(joinKycRoomIntent)
            }, viewModel)

        }
    }
}
