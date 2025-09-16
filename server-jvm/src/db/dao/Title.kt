package doreshnikov.db.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TitleTable : IntIdTable() {
    val name = varchar("name", 256).uniqueIndex()
    val description = text("description")
    val chapters = integer("chapters").nullable()
    val image = text("image").nullable()
}

class TitleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TitleEntity>(TitleTable)

    var name by TitleTable.name
    var description by TitleTable.description
    var chapters by TitleTable.chapters
    var image by TitleTable.image
}