package doreshnikov.model

import doreshnikov.db.dao.TitleEntity
import doreshnikov.model.Payload.Companion.verify

data class Title(
    val id: Int? = null,
    val name: String = "",
    val description: String = "",
    val chapters: Int? = null,
    val image: String? = null
) : Payload {

    constructor(entity: TitleEntity) : this(
        entity.id.value,
        entity.name,
        entity.description,
        entity.chapters,
        entity.image
    )

    override fun validate() {
        verify(id == null) { "Not supposed to receive already existing title" }
        verify(name.isNotBlank()) { "Name should not be blank" }
        chapters?.let { verify(it >= 0) { "Number of chapters should be non-negative" } }
    }

}
