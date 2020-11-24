package com.utsman.data.const

import com.utsman.data.model.Category
import java.lang.IndexOutOfBoundsException

object CategoriesApps {
    val tools = Category.simple {
        name = "Tools & utilities"
        query = "tools"
    }

    val video = Category.simple {
        name = "Video & player"
        query = "video"
    }

    val music = Category.simple {
        name = "Music"
        query = "music"
    }

    val social = Category.simple {
        name = "Social"
        query = "social"
    }

    val shopping = Category.simple {
        name = "Shopping"
        query = "shopping"
    }

    val productivity = Category.simple {
        name = "Productivity"
        query = "productivity"
    }

    val sport = Category.simple {
        name = "Sport"
        query = "sport"
    }

    val system = Category.simple {
        name = "System"
        query = "system"
    }

    val wallpaper = Category.simple {
        name = "Wallpaper & background"
        query = "wallpaper"
    }

    val communication = Category.simple {
        name = "Communication"
        query = "communication"
    }

    val artDesign = Category.simple {
        name = "Art & design"
        query = "art"
    }

    val messaging = Category.simple {
        name = "Chat & messaging"
        query = "messaging"
    }

    val list = listOf(
        tools,
        video,
        music,
        messaging,
        social,
        shopping,
        productivity,
        communication,
        artDesign,
        wallpaper,
        system,
        sport
    )

    object MockPaging {

        fun page(page: Int): List<Category>? {
            val perPage = 2
            return try {
                list.subList(page, page+perPage)
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }
    }
}