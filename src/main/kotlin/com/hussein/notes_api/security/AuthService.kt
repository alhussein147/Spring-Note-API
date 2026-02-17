package com.hussein.notes_api.security

import com.hussein.notes_api.database.token.RefreshTokenEntity
import com.hussein.notes_api.database.user.UserEntity
import com.hussein.notes_api.repository.token.RefreshTokenRepository
import com.hussein.notes_api.repository.users.UserRepository
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
  private  val jwtService: JwtService,
  private  val hashEncoder: HashEncoder,
  private  val userRepository: UserRepository,
  private  val refreshTokenRepository: RefreshTokenRepository
) {

    fun register(email: String, password: String): UserEntity {
        return userRepository.save(UserEntity(email = email, hashedPassword = hashEncoder.encode(password)))
    }

    fun login(email: String, password: String): TokenPair {
        // checking if a user already exists
        val user = userRepository.findUserByEmail(email) ?: throw BadCredentialsException("invalid credentials")
        // if that user exists , compare the provided password with the saved hashed password
        if (!hashEncoder.matches(password, user.hashedPassword)) {
            // if the password doesn't match
            throw BadCredentialsException("invalid credentials")
        }
        // if we get to this point , means the user provided the right credentials , so we start by generating new tokens

        val newAccessToken = jwtService.generateAccessToken(userId = user.id.toHexString())
        val newRefreshToken = jwtService.generateRefreshToken(userId = user.id.toHexString())

        // saving the new generated tokens in the user_tokens document
        storeRefreshToken(userId = user.id, newRefreshToken)
        // returning the generated tokens for the user
        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    // when the user refreshes the access token , they will get a new refresh token also
    @Transactional
    fun refresh(refreshToken: String): TokenPair {
        val userId = jwtService.getUserIdFromToken(refreshToken)
        val user =
            userRepository.findById(ObjectId(userId)).orElseThrow { IllegalArgumentException("Invalid refresh token.") }

        val hashed = hashEncoder.hashRefreshToken(refreshToken)
        refreshTokenRepository.findByUserIdAndHashedToken(userId = user.id, hashedToken = hashed)
            ?: throw IllegalArgumentException("invalid refresh token (maybe used or expired)")

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)

        val newRefreshToken = jwtService.generateAccessToken(userId = userId)
        val newAccessToken = jwtService.generateAccessToken(userId)

        storeRefreshToken(userId = user.id, newRefreshToken)

        return TokenPair(accessToken = newAccessToken, refreshToken = newRefreshToken)

    }


    private fun storeRefreshToken(userId: ObjectId, rawRefreshToken: String) {
        val hashed = hashEncoder.hashRefreshToken(refreshToken = rawRefreshToken)

        val expiryMs = jwtService.accessTokenValidityMs
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(
            RefreshTokenEntity(
                userId = userId,
                hashedToken = hashed,
                expiresAt = expiresAt
            )
        )

    }


}