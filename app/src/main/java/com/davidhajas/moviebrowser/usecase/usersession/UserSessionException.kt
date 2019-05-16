package com.davidhajas.moviebrowser.usecase.usersession

abstract class UserSessionException : Exception()

// region LoginException

abstract class LoginException : UserSessionException()

class UserDoesNotExistException : LoginException()

class WrongPasswordException : LoginException()

// endregion

// region RegisterException

abstract class RegisterException : UserSessionException()

class UserNameEmptyException : RegisterException()

class UserExistsException : RegisterException()

class PasswordEmptyException : RegisterException()

class PasswordTooShortException : RegisterException()

class PasswordsDifferException : RegisterException()

// endregion