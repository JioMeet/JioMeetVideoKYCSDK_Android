package com.example.demo.model

data class JoinMeetingState(
    val meetingID: String = "0416591087",
    val meetingPin: String = "C2nyR",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)

