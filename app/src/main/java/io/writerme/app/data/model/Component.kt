package io.writerme.app.data.model

import io.realm.RealmObject
import io.realm.annotations.Index
import io.realm.annotations.PrimaryKey
import java.util.Date

/**
 * Since Realm doesn't provide polymorphism support
 * all the different component types, such as text,
 * voice records, images, videos, tasks, lists, and
 * links are combined in the following model class
 * as an analog to the 1:N polymorphic relation into
 * the single SQL table
 */
open class Component(): RealmObject() {
    @Index
    @PrimaryKey
    var id: Long = System.currentTimeMillis()

    var noteId: Long = 0

    var content: String = ""

    var isChecked: Boolean = false

    var url: String = ""

    var title: String? = null

    var imageUrl: String? = null

    var time: Date? = null

    var isImage: Boolean = true

    var changeTime: Long = System.currentTimeMillis()

    private var _type: String = ComponentType.Text.value
    var type: ComponentType
        get() {
            return try {
                ComponentType.valueOf(_type)
            } catch (e: IllegalArgumentException) {
                ComponentType.Text
            }
        }
        set(value) {
            _type = value.value
        }

    constructor(note: Note,
                content: String) : this() {
        this.noteId = note.id
        this.content = content
        this.type = ComponentType.Text
    }

    constructor(note: Note,
                content: String,
                isChecked: Boolean) : this() {
        this.noteId = note.id
        this.content = content
        this.type = ComponentType.CheckBox
    }

    constructor(note: Note,
                date: Date,
                description: String) : this() {
        this.noteId = note.id
        this.time = date
        this.content = description
        this.type = ComponentType.Task
    }

    // voice, media (image, video)
    constructor(note: Note,
                url: String,
                type: ComponentType) : this() {
        this.noteId = note.id
        this.url = url
        this.type = type
    }

    constructor(note: Note,
                url: String,
                title: String,
                description: String? = null,
                imageUrl: String? = null) : this() {
        this.noteId = note.id
        this.url = url
        this.title = title
        description?.let { this.content = it }
        this.imageUrl = imageUrl
        this.type = ComponentType.Link
    }
}

enum class ComponentType(val value: String) {
    Text("text"), CheckBox("checkbox"),
    Voice("voice"), Task("task"),
    Link("link"), Media("media")
}