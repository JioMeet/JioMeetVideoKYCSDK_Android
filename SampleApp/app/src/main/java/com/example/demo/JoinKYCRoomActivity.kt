package com.example.demo

import CustomAlertDialogWithImage
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jio.sdksampleapp.R
import com.jiomeet.core.constant.Constant
import com.jiomeet.core.main.models.JMJoinMeetingConfig
import com.jiomeet.core.main.models.JMJoinMeetingData
import com.jiomeet.core.main.models.Speaker
import com.jiomeet.core.utils.BaseUrl
import org.jio.kyc.helper.StringConstants
import org.jio.kyc.model.JioMeetConnectionListener
import org.jio.kyc.model.KycOptions
import org.jio.kyc.view.ui.LaunchKYCCore

class JoinKYCRoomActivity : ComponentActivity() {

    private val jmJoinMeetingConfig =
        JMJoinMeetingConfig(
            userRole = Speaker,
            isInitialAudioOn = true,
            isInitialVideoOn = true,
            isShareScreen = false,
            isShareWhiteBoard = false
        )

    private val jioMeetConnectionListener =
        object : JioMeetConnectionListener {
            override fun onLeaveMeeting() {
                finish()
            }
        }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseUrl.initializedNetworkInformation(this, Constant.Environment.PROD)
        joinVideoCall()
    }

    private fun joinVideoCall() {
        setContent {
            val jmJoinMeetingData =
                JMJoinMeetingData(
                    meetingId = intent.getStringExtra(StringConstants.MEETING_ID) ?: "",
                    meetingPin = intent.getStringExtra(StringConstants.MEETING_PIN) ?: "",
                    displayName = intent.getStringExtra(StringConstants.GUEST_NAME) ?: "",
                    version = "1234",
                    deviceId = "deviceId"
                )

            val kycOption = remember { mutableStateOf(KycOptions.CALL) }
            val switchCameraClicked = remember { mutableStateOf(false) }
            val captureImageClicked = remember { mutableStateOf(false) }
            val leaveMeetingClicked = remember { mutableStateOf(false) }

            val onSwitchCameraClick = {
                switchCameraClicked.value = !switchCameraClicked.value
            }

            val onCaptureImageClick = {
                captureImageClicked.value = !captureImageClicked.value
            }

            val onLeaveMeetingClick = {
                leaveMeetingClicked.value = true
            }

            val showImageDialog = remember { mutableStateOf(ImageResult(false, "")) }
            if (showImageDialog.value.showDialog) {
                CustomAlertDialogWithImage(imagePath = showImageDialog.value.imagePath) {
                    showImageDialog.value = ImageResult(false, "")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    KYCOptionsDropdown(
                        modifier = Modifier.weight(1f),
                        onItemSelected = {
                            kycOption.value = it
                        })
                    IconButton(onClick = { onSwitchCameraClick() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_flip_camera),
                            contentDescription = "Flip camera",
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    IconButton(onClick = { onCaptureImageClick() }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_capture_image),
                            contentDescription = "Capture image",
                            modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                    Button(onClick = { onLeaveMeetingClick() }) {
                        Text(text = "Leave Meeting")
                    }
                }
                Surface(modifier = Modifier.weight(1f)) {
                    LaunchKYCCore(
                        jioMeetConnectionListener = jioMeetConnectionListener,
                        jmJoinMeetingConfig = jmJoinMeetingConfig,
                        jmJoinMeetingData = jmJoinMeetingData,
                        kycOption = kycOption.value,
                        switchCameraState = switchCameraClicked.value,
                        captureImageClick = captureImageClicked.value,
                        onCaptureImage = { imagePath ->
                            println("Captured Image Path $imagePath")
                            showImageDialog.value = ImageResult(true, imagePath)
                        },
                        leaveMeeting = leaveMeetingClicked.value
                    )
                }
            }
        }
    }
}

@Composable
fun KYCOptionsDropdown(
    modifier: Modifier = Modifier,
    onItemSelected: (KycOptions) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(KycOptions.CALL) }
    val items = listOf(KycOptions.CALL, KycOptions.FACE_UI, KycOptions.CARD_UI)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedItem.value,
            modifier = Modifier.padding(16.dp),
            color = Color.White,
            fontSize = 16.sp
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.value,
                            fontSize = 16.sp
                        )
                    },
                    onClick = {
                        selectedItem = item
                        expanded = false
                        onItemSelected(item)
                    }
                )
            }
        }
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                Icons.Filled.ArrowDropDown,
                tint = Color.White,
                contentDescription = "KYC Options"
            )
        }
    }
}

data class ImageResult(
    val showDialog: Boolean,
    val imagePath: String
)
