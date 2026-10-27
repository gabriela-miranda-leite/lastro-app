package com.lastro.core.domain.exception

sealed class CoupleException(message: String) : Exception(message) {
    data object AlreadyLinked : CoupleException("User is already linked to a couple")
    data object InviteNotFound : CoupleException("Invite code not found")
    data object InviteExpired : CoupleException("Invite code has expired or was already used")
}
