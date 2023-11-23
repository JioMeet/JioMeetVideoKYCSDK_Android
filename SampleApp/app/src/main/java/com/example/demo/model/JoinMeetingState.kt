package com.example.demo.model

data class JoinMeetingState(
    val meetingID: String = "8374555361",
    val meetingPin: String = "M7es2",
    val userName: String = "",
    val isMeetingIDError: Boolean = false,
    val isMeetingPinError: Boolean = false,
    val isUserNameError: Boolean = false,
    val isJoinButtonEnabled: Boolean = false
)

