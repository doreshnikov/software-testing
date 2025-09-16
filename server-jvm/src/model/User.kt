package doreshnikov.model

import doreshnikov.db.dao.UserEntity

data class User(
    val id: Int,
    val login: String,
    val passwordHash: String
) {

    constructor(entity: UserEntity) : this(
        entity.id.value,
        entity.login,
        entity.passwordHash
    )

}
